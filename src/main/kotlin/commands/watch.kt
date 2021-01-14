package commands

import db.DbSettings
import db.NotablePlayers
import di.getInstance
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.dv8tion.jda.api.entities.MessageChannel
import network.OpenDotaService
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

private val watchMutex = Mutex()
private val watchJobs = mutableMapOf<Long, Job>()

private suspend fun pollNextMatch(authorSnowflake: Long, channel: MessageChannel, targetId: Long, lastMatchId: Long) {
    val openDotaService: OpenDotaService = getInstance()
    while (true) {
        delay(60000L)

        val matchesResponse = openDotaService.getPlayerMatches(targetId, 2)
        if (!matchesResponse.isSuccessful) {
            continue
        }
        val matches = matchesResponse.body()!!
        println("Called poll, last match $lastMatchId, matches received: $matches")
        if (matches[0].matchId != lastMatchId) {
            channel.sendMessage(
                "<@$authorSnowflake>, a match you requested to watch has been completed"
            ).queue()
            break
        }
    }
}

val watch = Command(
    listOf("watch"),
    "Watch for the end of player's game"
) { event ->
    val channel = event.channel

    val authorSnowflake = event.message.author.idLong

    val mentions = event.message.mentionedUsers
    if (mentions.isEmpty()) {
        channel.sendMessage(
            "Please specify a player to watch."
        ).queue()
        return@Command
    }

    val targetSnowflake = mentions[0].idLong
    val steamIds = transaction(DbSettings.db) {
        NotablePlayers.select { NotablePlayers.snowflake eq targetSnowflake }
            .map { it[NotablePlayers.steamId] }
    }
    if (steamIds.isEmpty()) {
        channel.sendMessage(
            "This player is not in the database."
        ).queue()
        return@Command
    }
    val targetId = steamIds[0]

    val openDotaService: OpenDotaService = getInstance()
    val playerMatchResponse = openDotaService.getPlayerMatches(targetId, 2)
    if (!playerMatchResponse.isSuccessful) {
        channel.sendMessage(
            "Could not retrieve last match, try again later."
        ).queue()
        return@Command
    }
    val lastMatch = playerMatchResponse.body()!![0]
    val matchId = lastMatch.matchId

    watchMutex.withLock {
        val job: Job = GlobalScope.launch {
            pollNextMatch(authorSnowflake, channel, targetId, matchId)
        }
        watchJobs[targetId] = job
    }
    channel.sendMessage(
        "You will be notified once the player has finished their next game."
    ).queue()
}
