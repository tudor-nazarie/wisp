package wisp.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class Player(
    @SerialName("tracked_until")
    val trackedUntil: String? = null,

    val profile: Profile? = null,

    @SerialName("competitive_rank")
    val competitiveRank: JsonObject? = null,

    @SerialName("solo_competitive_rank")
    val soloCompetitiveRank: JsonObject? = null,

    @SerialName("leaderboard_rank")
    val leaderboardRank: JsonObject? = null,

    @SerialName("rank_tier")
    val rankTier: Long? = null,

    @SerialName("mmr_estimate")
    val mmrEstimate: MmrEstimate? = null
)

@Serializable
data class MmrEstimate(
    val estimate: Long? = null
)

@Serializable
data class Profile(
    @SerialName("account_id")
    val accountID: Long? = null,

    val personaname: String? = null,
    val name: JsonObject? = null,
    val plus: Boolean? = null,
    val cheese: Long? = null,
    val steamid: String? = null,
    val avatar: String? = null,
    val avatarmedium: String? = null,
    val avatarfull: String? = null,
    val profileurl: String? = null,

    @SerialName("last_login")
    val lastLogin: String? = null,

    val loccountrycode: JsonObject? = null,

    @SerialName("is_contributor")
    val isContributor: Boolean? = null
)
