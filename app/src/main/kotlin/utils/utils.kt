package utils

import di.getInstance
import dsl.embed
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.SelfUser
import network.models.Player
import network.models.PlayerMatch
import java.awt.Color
import java.time.Instant
import java.time.format.DateTimeFormatter

val String.snowflake: Long
    get() = this.filter { it.isDigit() }.toLong()

private val Long.formattedDate: String
    get() {
        val formatter = getInstance<DateTimeFormatter>()
        val instant = Instant.ofEpochSecond(this)
        return formatter.format(instant)
    }

private val Int.formattedDuration: String
    get() {
        val mins = this / 60
        val secs = this % 60
        return (if (mins < 10) "0" else "") + "$mins" +
                ":" +
                (if (secs < 10) "0" else "") + "$secs"
    }

fun getMatchEmbed(
    selfUser: SelfUser,
    heroName: String,
    heroImage: String,
    player: Player,
    match: PlayerMatch
): MessageEmbed =
    embed {
        author {
            name = selfUser.name
            url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
            iconUrl = selfUser.avatarUrl
        }
        color = Color(167, 39, 20)
        title {
            title = "Match Result" +
                    if (gameModes.containsKey(match.gameMode))
                        " (${gameModes[match.gameMode]}, ${match.duration.formattedDuration})"
                    else
                        " (${match.duration.formattedDuration})"
        }
        thumbnail = heroImage
        description =
            "${player.profile.personaname} " + (if (match.playerWon) "won" else "lost") + " as " + (if (match.radiant) "Radiant" else "Dire") + "."

        fields {
            field {
                name = "Hero Played"
                value = heroName
                inline = true
            }
            field {
                name = "K/D/A"
                value = "${match.kills}/${match.deaths}/${match.assists}"
                inline = true
            }
            field {
                name = "Start Time"
                value = match.startTime.formattedDate
                inline = true
            }
            field {
                name = "DOTABUFF"
                value = "https://www.dotabuff.com/matches/${match.matchId}"
            }
            field {
                name = "OpenDota"
                value = "https://www.opendota.com/matches/${match.matchId}"
            }
        }
    }

val gameModes: Map<Int, String> = mapOf(
    1 to "All Pick",
    2 to "Captains Mode",
    3 to "Random Draft",
    4 to "Single Draft",
    22 to "All Draft",
)
