package commands

import db.DbSettings
import db.Heroes
import db.NotablePlayers
import di.getInstance
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import network.OpenDotaService
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.awt.Color
import java.time.Instant
import java.time.LocalDateTime

val players = Command(
    listOf("np", "players"),
    "Managed tracked notable players"
) { event ->
    val content = event.message.contentRaw
    val tokens = content.split(Regex("\\s+"))

    when (tokens[1]) {
        "new" -> {
            val steamId = tokens[2]
            val mentions = event.message.mentionedUsers
            // TODO: 12/01/2021 add error handling
            if (mentions.size > 0) {
                val snowflake = mentions[0].idLong
                addNotablePlayer(event, steamId.toLong(), snowflake)
            }
        }
        "count" -> countNotablePlayers(event)
        "purge" -> purgeNotablePlayers(event)
    }
}

val last = Command(
    listOf("last", "l"),
    "Get last public match of player",
) { event ->
    val channel = event.message.channel
    val mentions = event.message.mentionedUsers
    if (mentions.size == 0) {
        channel.sendMessage("Please specify a player.").queue()
        return@Command
    }
    val snowflake = mentions[0].idLong
    val steamIds = transaction(DbSettings.db) {
        NotablePlayers.select { NotablePlayers.snowflake eq snowflake }.map { it[NotablePlayers.steamId] }
    }
    if (steamIds.isEmpty()) {
        // TODO: 12/01/2021 @ the user not in the list
        channel.sendMessage("This user is not in the notable player list.").queue()
        return@Command
    }
    val steamId = steamIds[0]

    val openDotaService: OpenDotaService = getInstance()
    val matchesResponse = openDotaService.getPlayerMatches(steamId, 20)
    val playerResponse = openDotaService.getPlayer(steamId)
    if (!matchesResponse.isSuccessful || !playerResponse.isSuccessful) {
        channel.sendMessage("Could not grab user's matches, try again later.").queue()
        return@Command
    }
    val match = matchesResponse.body()!![0]
    val player = playerResponse.body()!!

    val heroes = transaction(DbSettings.db) {
        Heroes.select { Heroes.heroId eq match.heroId }.map { it[Heroes.localizedName] to it[Heroes.name] }
    }
    val heroData = if (heroes.isNotEmpty()) {
        heroes[0].first to "http://dotabase.dillerm.io/dota-vpk/panorama/images/heroes/selection/${heroes[0].second}_png.png"
    } else {
        "Hero not found" to "https://pbs.twimg.com/profile_images/807755806837850112/WSFVeFeQ.jpg"
    }

    val description =
        "${player.profile.personaname} " + (if (match.playerWon) "won" else "lost") + " as " + (if (match.radiant) "Radiant" else "Dire") + "."
    // TODO: 12/01/2021 add game mode field
    channel.sendMessage(
        EmbedBuilder()
            .setAuthor(
                event.jda.selfUser.name,
                "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                event.jda.selfUser.avatarUrl
            )
            .setColor(Color(167, 39, 20))
            .setTitle("Match Result")
            .setThumbnail(heroData.second)
            .setDescription(description)
            .addField("Hero Played", heroData.first, true)
            .addField("K/D/A", "${match.kills}/${match.deaths}/${match.assists}", true)
            .addField("DOTABUFF", "https://www.dotabuff.com/matches/${match.matchId}", false)
            .addField("OpenDota", "https://www.opendota.com/matches/${match.matchId}", false)
            .build()
    ).queue()
}

private suspend fun addNotablePlayer(event: MessageReceivedEvent, sId: Long, s: Long) {
    val openDotaService: OpenDotaService = getInstance()
    val channel = event.message.channel
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
        EmbedBuilder()
            .setAuthor(
                event.jda.selfUser.name,
                "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                event.jda.selfUser.avatarUrl
            )
            .setColor(Color(167, 39, 20))
            .setTitle(player.profile.personaname, "https://steamcommunity.com/profiles/${player.profile.steamid}/")
            .setDescription("Added notable player ${player.profile.personaname}")
            .setThumbnail(player.profile.avatarfull)
            .setTimestamp(Instant.now())
            .addField("DOTABUFF", "https://www.dotabuff.com/players/${player.profile.accountId}", false)
            .addField("OpenDota", "https://www.opendota.com/players/${player.profile.accountId}", false)
            .addField("STRATZ", "https://stratz.com/players/${player.profile.accountId}", false)
            .build()
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
