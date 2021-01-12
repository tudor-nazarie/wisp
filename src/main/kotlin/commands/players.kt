package commands

import db.DbSettings
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
            .setTitle(player.profile.personaname, "https://steamcommunity.com/profiles/${player.profile.steamid}/")
            .setDescription("Added notable player ${player.profile.personaname}")
            .setAuthor(
                event.jda.selfUser.name,
                "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                event.jda.selfUser.avatarUrl
            )
            .setThumbnail(player.profile.avatarfull)
            .setColor(Color(167, 39, 20))
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
