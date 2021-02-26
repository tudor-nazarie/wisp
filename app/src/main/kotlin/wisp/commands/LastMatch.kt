package wisp.commands

import mu.KotlinLogging
import wisp.db.DbSettings
import wisp.db.User
import wisp.db.Users
import wisp.db.transaction
import wisp.network.openDotaService
import wisp.utils.R
import wisp.utils.embed
import java.util.*

private val logger = KotlinLogging.logger {}

val lastMatch = command {
    name = "last"
    aliases {
        +"l"
        +"lm"
        +"lastgame"
        +"lastmatch"
    }
    description = "Get last public match"
    handler { message, _, prefix, _ ->
        val channel = message.channel
        channel.sendTyping().queue()

        val snowflake =
            if (message.mentionedUsers.isEmpty())
                message.author.idLong
            else
                message.mentionedUsers[0].idLong
        val user = DbSettings.db.transaction {
            User.find { Users.discordSnowflake eq snowflake }.firstOrNull()
        }
        if (user == null) {
            channel.sendMessage(
                "Your Steam isn't linked. Try `${prefix}userconfig steam` to see how to do that"
            ).queue()
            return@handler
        }

        logger.debug { "Grabbing last match of player ID '${user.steamId}'" }

        val playerMatchesResponse = openDotaService.getPlayerMatches(user.steamId, 1)
        if (!playerMatchesResponse.isSuccessful) {
            channel.sendMessage(
                "An error occurred while trying to get match data, try again later."
            ).queue()
            return@handler
        }
        val lastMatch = playerMatchesResponse.body()!![0]
        val lastMatchId = lastMatch.matchId

        logger.debug { "Grabbing match info for game ID '$lastMatchId'" }

        val matchResponse = openDotaService.getMatch(lastMatchId)
        if (!matchResponse.isSuccessful) {
            channel.sendMessage(
                "An error occurred while trying to get match data, try again later."
            ).queue()
            return@handler
        }
        val match = matchResponse.body()!!
        val player = match.players.first { it.accountId == user.steamId }

        val winStatus = if (player.win != 0) "Won" else "Lost"
        val gameMode = R.dota.gameModes[match.gameMode.toString()]
        val lobbyType =
            if (R.dota.lobbyTypes[match.lobbyType.toString()] == "Normal")
                ""
            else
                R.dota.lobbyTypes[match.lobbyType.toString()] + " "
        val heroName = R.dota.heroes.first { it.id == player.heroId }

        val desc = """
            $winStatus a $lobbyType**$gameMode** match as ${heroName.localizedName} in ${match.duration.prettyTime}.
            See match details at [DOTABUFF](https://www.dotabuff.com/matches/${match.matchId}), [OpenDota](https://www.opendota.com/matches/${match.matchId}), [STRATZ](https://www.stratz.com/match/${match.matchId}).
        """.trimIndent()

        val hero = R.dota.heroes.first { it.id == player.heroId }

        // prolly a bad idea but idc
        val thumbnailUrl =
            if (player.radiant)
                "https://raw.githubusercontent.com/mdiller/MangoByte/b735081b70e5e0aa7d69567821538213a6ca9dc0/resource/images/radiant.png"
            else
                "https://raw.githubusercontent.com/mdiller/MangoByte/b735081b70e5e0aa7d69567821538213a6ca9dc0/resource/images/dire.png"

        channel.sendMessage(
            embed {
                author {
                    name = player.personaname ?: "Anonymous"
                    url = "https://www.opendota.com/players/${player.accountId}"
                    iconUrl =
                        "https://dotabase.dillerm.io/dota-vpk/${hero.icon}"
                }
                color = R.colors.dotaRed
                description = desc
                thumbnail = thumbnailUrl
                fields {
                    field {
                        name = "Damage"
                        value = """
                            KDA: **${player.kills}**/**${player.deaths}**/**${player.assists}**
                            Hero Damage: ${player.heroDamage}
                            Hero Healing: ${player.heroHealing}
                            Tower Damage: ${player.towerDamage}
                        """.trimIndent()
                        inline = true
                    }
                    field {
                        name = "Economy"
                        value = """
                            Net Worth: ${player.netWorth}
                            Last Hits: ${player.lastHits}
                            Denies: ${player.denies}
                            Level: ${player.level}
                        """.trimIndent()
                        inline = true
                    }
                }
                footer {
                    text = "${match.matchId}"
                }
                timestamp = Date(match.startTime * 1000).toInstant()
            }
        ).queue()
    }
}

private val Int.prettyTime: String
    get() {
        val mins = this / 60
        val secs = this % 60
        return (if (mins != 0) "$mins minutes" else "0 minutes") +
                (if (secs != 0) ", $secs seconds" else "")
    }
