package network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Player(
    @Json(name = "tracked_until") val trackedUntil: String,
    @Json(name = "solo_competitive_rank") val soloCompetitiveRank: String?,
    @Json(name = "competitive_rank") val competitiveRank: String?,
    @Json(name = "rank_tier") val rankTier: Int?,
    @Json(name = "leaderboard_rank") val leaderboardRank: Int?,
    @Json(name = "mmr_estimate") val mmrEstimate: MmrEstimate?,
    val profile: Profile,
)

@JsonClass(generateAdapter = true)
data class MmrEstimate(
    val estimate: Int,
    val stdDev: Int?,
    val n: Int?,
)

@JsonClass(generateAdapter = true)
data class Profile(
    @Json(name = "account_id") val accountId: Long,
    val personaname: String,
    val name: String?,
    val plus: Boolean?,
    val cheese: Int?,
    val steamid: String,
    val avatar: String,
    val avatarmedium: String,
    val avatarfull: String,
    val profileurl: String,
    @Json(name = "last_login") val lastLogin: String,
    val loccountrycode: String?,
    @Json(name = "is_contributor") val isContributor: Boolean?,
)

@JsonClass(generateAdapter = true)
data class PlayerHero(
    @Json(name = "hero_id") val heroId: String,
    @Json(name = "last_played") val lastPlayed: Long,
    val games: Int,
    val win: Int,
    @Json(name = "with_games") val withGames: Int,
    @Json(name = "with_win") val withWin: Int,
    @Json(name = "against_games") val againstGames: Int,
    @Json(name = "against_win") val againstWin: Int,
)

@JsonClass(generateAdapter = true)
data class PlayerMatch(
    @Json(name = "match_id") val matchId: Long,
    @Json(name = "player_slot") val playerSlot: Int,
    @Json(name = "radiant_win") val radiantWin: Boolean,
    val duration: Int,
    @Json(name = "game_mode") val gameMode: Int,
    @Json(name = "lobby_type") val lobbyType: Int,
    @Json(name = "hero_id") val heroId: Int,
    @Json(name = "start_time") val startTime: Long,
    val version: Int,
    val kills: Int,
    val deaths: Int,
    val assists: Int,
    val skill: Int?,
    @Json(name = "party_size") val partySize: Int,
) {
    /**
     * Whether the user was on Radiant.
     */
    val radiant: Boolean
        get() = playerSlot in 0..127

    /**
     * Whether the user was on Dire.
     */
    val dire: Boolean
        get() = !radiant

    val direWin: Boolean
        get() = !radiantWin

    /**
     * Whether the player won the match.
     */
    val playerWon: Boolean
        get() = (radiantWin && radiant) || (direWin && dire)
}

@JsonClass(generateAdapter = true)
data class PlayerWinLoss(
    val win: Int,
    val lose: Int
)
