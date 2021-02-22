package wisp.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlayerMatch(
    @SerialName("match_id") val matchId: Long,
    @SerialName("player_slot") val playerSlot: Int,
    @SerialName("radiant_win") val radiantWin: Boolean,
    val duration: Int,
    @SerialName("game_mode") val gameMode: Int,
    @SerialName("lobby_type") val lobbyType: Int,
    @SerialName("hero_id") val heroId: Int,
    @SerialName("start_time") val startTime: Long,
    val version: Int?,
    val kills: Int,
    val deaths: Int,
    val assists: Int,
    val skill: Int?,
    @SerialName("party_size") val partySize: Int?,
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
