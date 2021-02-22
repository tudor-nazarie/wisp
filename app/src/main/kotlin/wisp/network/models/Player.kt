package wisp.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Player(
    @SerialName("tracked_until") val trackedUntil: String?,
    @SerialName("solo_competitive_rank") val soloCompetitiveRank: String?,
    @SerialName("competitive_rank") val competitiveRank: String?,
    @SerialName("rank_tier") val rankTier: Int?,
    @SerialName("leaderboard_rank") val leaderboardRank: Int?,
    @SerialName("mmr_estimate") val mmrEstimate: MmrEstimate?,
    val profile: Profile,
)

@Serializable
data class MmrEstimate(
    val estimate: Int,
    val stdDev: Int?,
    val n: Int?,
)

@Serializable
data class Profile(
    @SerialName("account_id") val accountId: Long,
    val personaname: String,
    val name: String?,
    val plus: Boolean?,
    val cheese: Int?,
    val steamid: String,
    val avatar: String,
    val avatarmedium: String,
    val avatarfull: String,
    val profileurl: String,
    @SerialName("last_login") val lastLogin: String?,
    val loccountrycode: String?,
    @SerialName("is_contributor") val isContributor: Boolean?,
)


