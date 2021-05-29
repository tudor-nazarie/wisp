package wisp.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class PlayerMatch(
    @SerialName("match_id")
    val matchID: Long? = null,

    @SerialName("player_slot")
    val playerSlot: Long? = null,

    @SerialName("radiant_win")
    val radiantWin: Boolean? = null,

    val duration: Long? = null,

    @SerialName("game_mode")
    val gameMode: Long? = null,

    @SerialName("lobby_type")
    val lobbyType: Long? = null,

    @SerialName("hero_id")
    val heroID: Long? = null,

    @SerialName("start_time")
    val startTime: Long? = null,

    val version: Long? = null,
    val kills: Long? = null,
    val deaths: Long? = null,
    val assists: Long? = null,
    val skill: JsonObject? = null,

    @SerialName("leaver_status")
    val leaverStatus: Long? = null,

    @SerialName("party_size")
    val partySize: Long? = null
) {
    val radiant: Boolean
        get() = playerSlot in 1L..127

    /**
     * Whether the user was on Dire.
     */
    val dire: Boolean
        get() = !radiant

    val direWin: Boolean
        get() = !radiantWin!!

    /**
     * Whether the player won the match.
     */
    val playerWon: Boolean
        get() = (radiantWin!! && radiant) || (direWin && dire)
}

