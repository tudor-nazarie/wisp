package wisp.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Match(
    @SerialName("match_id") val matchId: Long,
    @SerialName("barracks_status_dire") val barracksStatusDire: Int,
    @SerialName("barracks_status_radiant") val barracksStatusRadiant: Int,
    val cluster: Int,
    val cosmetics: Map<Int, Int>,
    @SerialName("dire_score") val direScore: Int,
    @SerialName("start_time") val startTime: Long,
    val duration: Int,
    @SerialName("first_blood_time") val firstBloodTime: Int,
    @SerialName("game_mode") val gameMode: Int,
    @SerialName("human_players") val humanPlayers: Int,
    @SerialName("leagueid") val leagueId: Int,
    @SerialName("lobby_type") val lobbyType: Int,
    @SerialName("match_seq_num") val matchSeqNum: Long,
    val patch: Int,
    val players: List<MatchPlayer>,
)

@Serializable
data class MatchPlayer(
    @SerialName("player_slot") val playerSlot: Int,
    @SerialName("account_id") val accountId: Long? = null,
    val kills: Int,
    val deaths: Int,
    val assists: Int,
    @SerialName("hero_damage") val heroDamage: Int,
    @SerialName("tower_damage") val towerDamage: Int,
    @SerialName("hero_healing") val heroHealing: Int,
    @SerialName("hero_id") val heroId: Int,
    val level: Int,
    @SerialName("net_worth") val netWorth: Int,
    @SerialName("last_hits") val lastHits: Int,
    val denies: Int,
    val personaname: String? = null,
    val win: Int,
) {
    val radiant: Boolean = playerSlot in 0..127

    val dire: Boolean = !radiant
}
