package commands

import db.DbSettings
import db.Heroes
import db.NotablePlayers
import di.getInstance
import dsl.command
import dsl.embed
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import network.OpenDotaService
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import utils.getMatchEmbed
import utils.snowflake
import java.awt.Color
import java.time.Instant
import java.time.LocalDateTime

val players = command {
    name = "players"
    aliases { +"np" }
    description = "Manage tracked notable players"
    subCommands {
        command {
            name = "add"
            aliases { +"new" }
            description = "Add new notable player"
            examples {
                +("players add <SteamID> @User" to "Add the mentioned user to database with given SteamID")
            }
            handler { args, event -> addNotablePlayer(args, event) }
        }
        command {
            name = "remove"
            aliases {
                +"delete"
                +"rm"
            }
            description = "Remove tracked notable player"
            examples {
                +("players remove @User" to "Remove the mentioned user from database")
            }
            handler { args, event -> deleteNotablePlayer(args, event) }
        }
        command {
            name = "count"
            description = "Count the number of notable players"
            handler { _, event -> countNotablePlayers(event) }
        }
        command {
            name = "purge"
            description = "Delete all tracked notable players"
            handler { _, event -> purgeNotablePlayers(event) }
        }
    }
    handler { _, event ->
        event.message.channel.sendMessage(
            "See `.help players` for usage."
        ).queue()
    }
}

val last = command {
    name = "last"
    aliases { +"l" }
    description = "Get last public match of player"
    handler { _, event ->
        val message = event.message
        val channel = message.channel
        val mentions = message.mentionedUsers
        val author = message.author

        val snowflakes = if (mentions.isNotEmpty()) mentions.map { it.idLong } else listOf(author.idLong)

        val playerData: List<Pair<Long, Long>> = transaction(DbSettings.db) {
            val result = mutableListOf<Pair<Long, Long>>()
            snowflakes.forEach { snowflake ->
                val query = NotablePlayers.select { NotablePlayers.snowflake eq snowflake }
                    .map { it[NotablePlayers.steamId] to it[NotablePlayers.snowflake] }
                if (query.isEmpty()) {
                    channel.sendMessage(
                        "User not in player database."
                    ).queue()
                } else {
                    result.add(query[0])
                }
            }
            result
        }

        val openDotaService: OpenDotaService = getInstance()
        for (datum in playerData) {
            val matchesResponse = openDotaService.getPlayerMatches(datum.first, 20)
            val playerResponse = openDotaService.getPlayer(datum.first)
            if (!matchesResponse.isSuccessful || !playerResponse.isSuccessful) {
                channel.sendMessage(
                    "Could not grab matches for user <@${datum.second}>, Steam ID ${datum.first}, try again later."
                ).queue()
                continue
            }
            val match = matchesResponse.body()!![0]
            val player = playerResponse.body()!!

            val heroes = transaction(DbSettings.db) {
                Heroes.select { Heroes.heroId eq match.heroId }
                    .map { it[Heroes.localizedName] to it[Heroes.name] }
            }
            val heroData = if (heroes.isNotEmpty()) {
                heroes[0].first to "http://dotabase.dillerm.io/dota-vpk/panorama/images/heroes/selection/${heroes[0].second}_png.png"
            } else {
                "Hero not found" to "https://pbs.twimg.com/profile_images/807755806837850112/WSFVeFeQ.jpg"
            }

            channel.sendMessage(
                getMatchEmbed(
                    event.jda.selfUser,
                    heroData.first,
                    heroData.second,
                    player,
                    match
                )
            ).queue()
        }
    }
}

private suspend fun addNotablePlayer(args: List<String>, event: MessageReceivedEvent) {
    val channel = event.message.channel
    if (args.size < 2) {
        // TODO: 25/01/2021 change this message
        channel.sendMessage(
            "Need at least 2 args: steam ID and discord @"
        ).queue()
    }
    val sId = args[0].toLong()
    val s = args[1].snowflake

    val openDotaService: OpenDotaService = getInstance()
    val playerResponse = openDotaService.getPlayer(sId)
    if (!playerResponse.isSuccessful || playerResponse.body()?.profile?.accountId != sId) {
        channel.sendMessage("Could not find player with SteamID $sId.").queue()
        return
    }
    val player = playerResponse.body()!!
    transaction(DbSettings.db) {
        if (!NotablePlayers.exists()) {
            SchemaUtils.create(NotablePlayers)
        }

        NotablePlayers.insert {
            it[steamId] = sId
            it[snowflake] = s
            it[date] = LocalDateTime.now()
        }
    }

    channel.sendMessage(
        embed {
            author {
                name = event.jda.selfUser.name
                url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
                iconUrl = event.jda.selfUser.avatarUrl
            }
            color = Color(167, 39, 20)
            title {
                title = player.profile.personaname
                url = "https://steamcommunity.com/profiles/${player.profile.steamid}/"
            }
            description = "Added notable player ${player.profile.personaname}"
            thumbnail = player.profile.avatarfull
            timestamp = Instant.now()
            fields {
                field {
                    name = "DOTABUFF"
                    value = "https://www.dotabuff.com/players/${player.profile.accountId}"
                }
                field {
                    name = "OpenDota"
                    value = "https://www.opendota.com/players/${player.profile.accountId}"
                }
                field {
                    name = "STRATZ"
                    value = "https://stratz.com/players/${player.profile.accountId}"
                }
            }
        }
    ).queue()
}

private fun deleteNotablePlayer(args: List<String>, event: MessageReceivedEvent) {
    val channel = event.message.channel
    if (args.isEmpty()) {
        channel.sendMessage(
            "Please specify a user to remove"
        ).queue()
    }
    val snowflake = args[0].snowflake
    val count = transaction(DbSettings.db) {
        NotablePlayers.deleteWhere { NotablePlayers.snowflake eq snowflake }
    }
    channel.sendMessage(
        if (count == 0)
            "<@$snowflake> is not in the database."
        else
            "Deleted <@$snowflake> from the database."
    ).queue()
}

private fun countNotablePlayers(event: MessageReceivedEvent) {
    val count = transaction(DbSettings.db) {
        if (!NotablePlayers.exists()) {
            0
        } else {
            NotablePlayers.selectAll().count()
        }
    }
    event.channel.sendMessage("There are currently $count notable players being tracked.").queue()
}

private fun purgeNotablePlayers(event: MessageReceivedEvent) {
    val channel = event.message.channel
    val count = transaction(DbSettings.db) {
        if (!NotablePlayers.exists()) {
            0
        } else {
            NotablePlayers.deleteAll()
        }
    }
    channel.sendMessage("Purged all $count notable players from the database.").queue()
}
