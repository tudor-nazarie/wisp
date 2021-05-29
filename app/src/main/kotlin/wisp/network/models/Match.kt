package wisp.network.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject

@Serializable
data class Match(
    @SerialName("match_id")
    val matchID: Long? = null,

    @SerialName("barracks_status_dire")
    val barracksStatusDire: Long? = null,

    @SerialName("barracks_status_radiant")
    val barracksStatusRadiant: Long? = null,

    val chat: List<Chat>? = null,
    val cluster: Long? = null,
    val cosmetics: Map<String, Long>? = null,

    @SerialName("dire_score")
    val direScore: Long? = null,

    @SerialName("dire_team_id")
    val direTeamID: JsonObject? = null,

    @SerialName("draft_timings")
    val draftTimings: List<DraftTiming>? = null,

    val duration: Long? = null,
    val engine: Long? = null,

    @SerialName("first_blood_time")
    val firstBloodTime: Long? = null,

    @SerialName("game_mode")
    val gameMode: Long? = null,

    @SerialName("human_players")
    val humanPlayers: Long? = null,

    val leagueid: Long? = null,

    @SerialName("lobby_type")
    val lobbyType: Long? = null,

    @SerialName("match_seq_num")
    val matchSeqNum: Long? = null,

    @SerialName("negative_votes")
    val negativeVotes: Long? = null,

    val objectives: List<Objective>? = null,

    @SerialName("picks_bans")
    val picksBans: List<PicksBan>? = null,

    @SerialName("positive_votes")
    val positiveVotes: Long? = null,

    @SerialName("radiant_gold_adv")
    val radiantGoldAdv: List<Long>? = null,

    @SerialName("radiant_score")
    val radiantScore: Long? = null,

    @SerialName("radiant_team_id")
    val radiantTeamID: JsonObject? = null,

    @SerialName("radiant_win")
    val radiantWin: Boolean? = null,

    @SerialName("radiant_xp_adv")
    val radiantXPAdv: List<Long>? = null,

    val skill: JsonObject? = null,

    @SerialName("start_time")
    val startTime: Long? = null,

    val teamfights: List<Teamfight>? = null,

    @SerialName("tower_status_dire")
    val towerStatusDire: Long? = null,

    @SerialName("tower_status_radiant")
    val towerStatusRadiant: Long? = null,

    val version: Long? = null,

    @SerialName("replay_salt")
    val replaySalt: Long? = null,

    @SerialName("series_id")
    val seriesID: Long? = null,

    @SerialName("series_type")
    val seriesType: Long? = null,

    val players: List<MatchPlayer>? = null,
    val patch: Long? = null,

    @SerialName("all_word_counts")
    val allWordCounts: Map<String, Long>? = null,

    @SerialName("my_word_counts")
    val myWordCounts: MyWordCounts? = null,

    val comeback: Long? = null,
    val stomp: Long? = null,

    @SerialName("replay_url")
    val replayURL: String? = null
)

@Serializable
data class Chat(
    val time: Long? = null,
    val type:   ChatType? = null,
    val key: String? = null,
    val slot: Long? = null,

    @SerialName("player_slot")
    val playerSlot: Long? = null
)

@Serializable
enum class ChatType(val value: String) {
    BuybackLog("buyback_log"),
    Chat("chat"),
    Chatwheel("chatwheel");

    companion object : KSerializer<ChatType> {
        override val descriptor: SerialDescriptor
            get() {
                return PrimitiveSerialDescriptor("wisp.network.models.ChatType", PrimitiveKind.STRING)
            }

        override fun deserialize(decoder: Decoder): ChatType = when (val value = decoder.decodeString()) {
            "buyback_log" -> BuybackLog
            "chat" -> Chat
            "chatwheel" -> Chatwheel
            else -> throw IllegalArgumentException("ChatType could not parse: $value")
        }

        override fun serialize(encoder: Encoder, value: ChatType) {
            return encoder.encodeString(value.value)
        }
    }
}

@Serializable
data class DraftTiming(
    val order: Long? = null,
    val pick: Boolean? = null,

    @SerialName("active_team")
    val activeTeam: Long? = null,

    @SerialName("hero_id")
    val heroID: Long? = null,

    @SerialName("player_slot")
    val playerSlot: JsonObject? = null,

    @SerialName("extra_time")
    val extraTime: Long? = null,

    @SerialName("total_time_taken")
    val totalTimeTaken: Long? = null
)

@Serializable
class MyWordCounts()

@Serializable
data class Objective(
    val time: Long? = null,
    val type: ObjectiveType? = null,
    val slot: Long? = null,
    val key: Key? = null,

    @SerialName("player_slot")
    val playerSlot: Long? = null,

    val team: Long? = null,
    val unit: String? = null
)

@Serializable
sealed class Key {
    class IntegerValue(val value: Long) : Key()
    class StringValue(val value: String) : Key()
}

@Serializable
enum class ObjectiveType(val value: String) {
    BuildingKill("building_kill"),
    ChatMessageAegis("CHAT_MESSAGE_AEGIS"),
    ChatMessageCourierLost("CHAT_MESSAGE_COURIER_LOST"),
    ChatMessageFirstblood("CHAT_MESSAGE_FIRSTBLOOD"),
    ChatMessageRoshanKill("CHAT_MESSAGE_ROSHAN_KILL");

    companion object : KSerializer<ObjectiveType> {
        override val descriptor: SerialDescriptor
            get() {
                return PrimitiveSerialDescriptor("wisp.network.models.ObjectiveType", PrimitiveKind.STRING)
            }

        override fun deserialize(decoder: Decoder): ObjectiveType = when (val value = decoder.decodeString()) {
            "building_kill" -> BuildingKill
            "CHAT_MESSAGE_AEGIS" -> ChatMessageAegis
            "CHAT_MESSAGE_COURIER_LOST" -> ChatMessageCourierLost
            "CHAT_MESSAGE_FIRSTBLOOD" -> ChatMessageFirstblood
            "CHAT_MESSAGE_ROSHAN_KILL" -> ChatMessageRoshanKill
            else -> throw IllegalArgumentException("ObjectiveType could not parse: $value")
        }

        override fun serialize(encoder: Encoder, value: ObjectiveType) {
            return encoder.encodeString(value.value)
        }
    }
}

@Serializable
data class PicksBan(
    @SerialName("is_pick")
    val isPick: Boolean? = null,

    @SerialName("hero_id")
    val heroID: Long? = null,

    val team: Long? = null,
    val order: Long? = null
)

@Serializable
data class MatchPlayer(
    @SerialName("match_id")
    val matchID: Long? = null,

    @SerialName("player_slot")
    val playerSlot: Long? = null,

    @SerialName("ability_targets")
    val abilityTargets: AbilityTargets? = null,

    @SerialName("ability_upgrades_arr")
    val abilityUpgradesArr: List<Long>? = null,

    @SerialName("ability_uses")
    val abilityUses: Map<String, Long>? = null,

    @SerialName("account_id")
    val accountID: Long? = null,

    val actions: Map<String, Long>? = null,

    @SerialName("additional_units")
    val additionalUnits: JsonObject? = null,

    val assists: Long? = null,

    @SerialName("backpack_0")
    val backpack0: Long? = null,

    @SerialName("backpack_1")
    val backpack1: Long? = null,

    @SerialName("backpack_2")
    val backpack2: Long? = null,

    @SerialName("backpack_3")
    val backpack3: JsonObject? = null,

    @SerialName("buyback_log")
    val buybackLog: List<Chat>? = null,

    @SerialName("camps_stacked")
    val campsStacked: Long? = null,

    @SerialName("connection_log")
    val connectionLog: JsonArray? = null,

    @SerialName("creeps_stacked")
    val creepsStacked: Long? = null,

    val damage: Map<String, Long>? = null,

    @SerialName("damage_inflictor")
    val damageInflictor: Map<String, Long>? = null,

    @SerialName("damage_inflictor_received")
    val damageInflictorReceived: Map<String, Long>? = null,

    @SerialName("damage_taken")
    val damageTaken: Map<String, Long>? = null,

    @SerialName("damage_targets")
    val damageTargets: DamageTargets? = null,

    val deaths: Long? = null,
    val denies: Long? = null,

    @SerialName("dn_t")
    val dnT: List<Long>? = null,

    @SerialName("firstblood_claimed")
    val firstbloodClaimed: Long? = null,

    val gold: Long? = null,

    @SerialName("gold_per_min")
    val goldPerMin: Long? = null,

    @SerialName("gold_reasons")
    val goldReasons: Map<String, Long>? = null,

    @SerialName("gold_spent")
    val goldSpent: Long? = null,

    @SerialName("gold_t")
    val goldT: List<Long>? = null,

    @SerialName("hero_damage")
    val heroDamage: Long? = null,

    @SerialName("hero_healing")
    val heroHealing: Long? = null,

    @SerialName("hero_hits")
    val heroHits: Map<String, Long>? = null,

    @SerialName("hero_id")
    val heroID: Long? = null,

    @SerialName("item_0")
    val item0: Long? = null,

    @SerialName("item_1")
    val item1: Long? = null,

    @SerialName("item_2")
    val item2: Long? = null,

    @SerialName("item_3")
    val item3: Long? = null,

    @SerialName("item_4")
    val item4: Long? = null,

    @SerialName("item_5")
    val item5: Long? = null,

    @SerialName("item_neutral")
    val itemNeutral: Long? = null,

    @SerialName("item_uses")
    val itemUses: Map<String, Long>? = null,

    @SerialName("kill_streaks")
    val killStreaks: Map<String, Long>? = null,

    val killed: Map<String, Long>? = null,

    @SerialName("killed_by")
    val killedBy: Map<String, Long>? = null,

    val kills: Long? = null,

    @SerialName("kills_log")
    val killsLog: List<KillsLogElement>? = null,

    @SerialName("lane_pos")
    val lanePos: Map<String, Map<String, Long>>? = null,

    @SerialName("last_hits")
    val lastHits: Long? = null,

    @SerialName("leaver_status")
    val leaverStatus: Long? = null,

    val level: Long? = null,

    @SerialName("lh_t")
    val lhT: List<Long>? = null,

    @SerialName("life_state")
    val lifeState: Map<String, Long>? = null,

    @SerialName("max_hero_hit")
    val maxHeroHit: MaxHeroHit? = null,

    @SerialName("multi_kills")
    val multiKills: Map<String, Long>? = null,

    @SerialName("net_worth")
    val netWorth: Long? = null,

    val obs: Map<String, Map<String, Long>>? = null,

    @SerialName("obs_left_log")
    val obsLeftLog: List<ObsLeftLogElement>? = null,

    @SerialName("obs_log")
    val obsLog: List<ObsLeftLogElement>? = null,

    @SerialName("obs_placed")
    val obsPlaced: Long? = null,

    @SerialName("party_id")
    val partyID: Long? = null,

    @SerialName("party_size")
    val partySize: Long? = null,

    @SerialName("performance_others")
    val performanceOthers: JsonObject? = null,

    @SerialName("permanent_buffs")
    val permanentBuffs: List<PermanentBuff>? = null,

    val pings: Long? = null,

    @SerialName("pred_vict")
    val predVict: Boolean? = null,

    val purchase: Map<String, Long>? = null,

    @SerialName("purchase_log")
    val purchaseLog: List<KillsLogElement>? = null,

    val randomed: Boolean? = null,
    val repicked: JsonObject? = null,

    @SerialName("roshans_killed")
    val roshansKilled: Long? = null,

    @SerialName("rune_pickups")
    val runePickups: Long? = null,

    val runes: Map<String, Long>? = null,

    @SerialName("runes_log")
    val runesLog: List<RunesLog>? = null,

    val sen: Map<String, Map<String, Long>>? = null,

    @SerialName("sen_left_log")
    val senLeftLog: List<ObsLeftLogElement>? = null,

    @SerialName("sen_log")
    val senLog: List<ObsLeftLogElement>? = null,

    @SerialName("sen_placed")
    val senPlaced: Long? = null,

    val stuns: Double? = null,

    @SerialName("teamfight_participation")
    val teamfightParticipation: Double? = null,

    val times: List<Long>? = null,

    @SerialName("tower_damage")
    val towerDamage: Long? = null,

    @SerialName("towers_killed")
    val towersKilled: Long? = null,

    @SerialName("xp_per_min")
    val xpPerMin: Long? = null,

    @SerialName("xp_reasons")
    val xpReasons: Map<String, Long>? = null,

    @SerialName("xp_t")
    val xpT: List<Long>? = null,

    @SerialName("radiant_win")
    val radiantWin: Boolean? = null,

    @SerialName("start_time")
    val startTime: Long? = null,

    val duration: Long? = null,
    val cluster: Long? = null,

    @SerialName("lobby_type")
    val lobbyType: Long? = null,

    @SerialName("game_mode")
    val gameMode: Long? = null,

    @SerialName("is_contributor")
    val isContributor: Boolean? = null,

    val patch: Long? = null,
    val isRadiant: Boolean? = null,
    val win: Long? = null,
    val lose: Long? = null,

    @SerialName("total_gold")
    val totalGold: Long? = null,

    @SerialName("total_xp")
    val totalXP: Long? = null,

    @SerialName("kills_per_min")
    val killsPerMin: Double? = null,

    val kda: Long? = null,
    val abandons: Long? = null,

    @SerialName("neutral_kills")
    val neutralKills: Long? = null,

    @SerialName("tower_kills")
    val towerKills: Long? = null,

    @SerialName("courier_kills")
    val courierKills: Long? = null,

    @SerialName("lane_kills")
    val laneKills: Long? = null,

    @SerialName("hero_kills")
    val heroKills: Long? = null,

    @SerialName("observer_kills")
    val observerKills: Long? = null,

    @SerialName("sentry_kills")
    val sentryKills: Long? = null,

    @SerialName("roshan_kills")
    val roshanKills: Long? = null,

    @SerialName("necronomicon_kills")
    val necronomiconKills: Long? = null,

    @SerialName("ancient_kills")
    val ancientKills: Long? = null,

    @SerialName("buyback_count")
    val buybackCount: Long? = null,

    @SerialName("observer_uses")
    val observerUses: Long? = null,

    @SerialName("sentry_uses")
    val sentryUses: Long? = null,

    @SerialName("lane_efficiency")
    val laneEfficiency: Double? = null,

    @SerialName("lane_efficiency_pct")
    val laneEfficiencyPct: Long? = null,

    val lane: Long? = null,

    @SerialName("lane_role")
    val laneRole: Long? = null,

    @SerialName("is_roaming")
    val isRoaming: Boolean? = null,

    @SerialName("purchase_time")
    val purchaseTime: Map<String, Long>? = null,

    @SerialName("first_purchase_time")
    val firstPurchaseTime: Map<String, Long>? = null,

    @SerialName("item_win")
    val itemWin: Map<String, Long>? = null,

    @SerialName("item_usage")
    val itemUsage: Map<String, Long>? = null,

    @SerialName("purchase_ward_observer")
    val purchaseWardObserver: Long? = null,

    @SerialName("purchase_ward_sentry")
    val purchaseWardSentry: Long? = null,

    @SerialName("purchase_tpscroll")
    val purchaseTpscroll: Long? = null,

    @SerialName("actions_per_min")
    val actionsPerMin: Long? = null,

    @SerialName("life_state_dead")
    val lifeStateDead: Long? = null,

    @SerialName("rank_tier")
    val rankTier: Long? = null,

    val cosmetics: List<Cosmetic>? = null,
    val benchmarks: Benchmarks? = null,
    val personaname: String? = null,
    val name: JsonObject? = null,

    @SerialName("last_login")
    val lastLogin: String? = null
)

@Serializable
data class AbilityTargets(
    @SerialName("hoodwink_acorn_shot")
    val hoodwinkAcornShot: HoodwinkAcornShot? = null,

    @SerialName("satyr_soulstealer_mana_burn")
    val satyrSoulstealerManaBurn: SatyrSoulstealerManaBurn? = null,

    @SerialName("doom_bringer_doom")
    val doomBringerDoom: Map<String, Long>? = null,

    @SerialName("dark_troll_warlord_ensnare")
    val darkTrollWarlordEnsnare: DarkTrollWarlordEnsnare? = null,

    @SerialName("phantom_assassin_stifling_dagger")
    val phantomAssassinStiflingDagger: Map<String, Long>? = null,

    @SerialName("phantom_assassin_phantom_strike")
    val phantomAssassinPhantomStrike: Map<String, Long>? = null,

    @SerialName("dark_willow_cursed_crown")
    val darkWillowCursedCrown: DarkWillowCursedCrown? = null,

    @SerialName("vengefulspirit_magic_missile")
    val vengefulspiritMagicMissile: Map<String, Long>? = null,

    @SerialName("vengefulspirit_nether_swap")
    val vengefulspiritNetherSwap: Map<String, Long>? = null,

    @SerialName("zuus_arc_lightning")
    val zuusArcLightning: JakiroDualBreath? = null,

    @SerialName("zuus_lightning_bolt")
    val zuusLightningBolt: JakiroDualBreath? = null,

    @SerialName("abyssal_underlord_firestorm")
    val abyssalUnderlordFirestorm: AbyssalUnderlordFirestorm? = null,

    @SerialName("juggernaut_omni_slash")
    val juggernautOmniSlash: JuggernautOmniSlash? = null,

    @SerialName("juggernaut_swift_slash")
    val juggernautSwiftSlash: JakiroDualBreath? = null,

    @SerialName("rubick_fade_bolt")
    val rubickFadeBolt: JakiroDualBreath? = null,

    @SerialName("rubick_telekinesis")
    val rubickTelekinesis: Map<String, Long>? = null,

    @SerialName("rubick_spell_steal")
    val rubickSpellSteal: JakiroDualBreath? = null,

    @SerialName("jakiro_dual_breath")
    val jakiroDualBreath: JakiroDualBreath? = null
)

@Serializable
data class AbyssalUnderlordFirestorm(
    @SerialName("npc_dota_hero_abyssal_underlord")
    val npcDotaHeroAbyssalUnderlord: Long? = null
)

@Serializable
data class DarkTrollWarlordEnsnare(
    @SerialName("npc_dota_hero_rubick")
    val npcDotaHeroRubick: Long? = null,

    @SerialName("npc_dota_hero_jakiro")
    val npcDotaHeroJakiro: Long? = null,

    @SerialName("npc_dota_hero_juggernaut")
    val npcDotaHeroJuggernaut: Long? = null,

    @SerialName("npc_dota_hero_doom_bringer")
    val npcDotaHeroDoomBringer: Long? = null
)

@Serializable
data class DarkWillowCursedCrown(
    @SerialName("npc_dota_hero_zuus")
    val npcDotaHeroZuus: Long? = null,

    @SerialName("npc_dota_hero_abyssal_underlord")
    val npcDotaHeroAbyssalUnderlord: Long? = null,

    @SerialName("npc_dota_hero_juggernaut")
    val npcDotaHeroJuggernaut: Long? = null,

    @SerialName("npc_dota_hero_jakiro")
    val npcDotaHeroJakiro: Long? = null
)

@Serializable
data class HoodwinkAcornShot(
    @SerialName("npc_dota_hero_juggernaut")
    val npcDotaHeroJuggernaut: Long? = null,

    @SerialName("npc_dota_hero_jakiro")
    val npcDotaHeroJakiro: Long? = null,

    @SerialName("npc_dota_hero_abyssal_underlord")
    val npcDotaHeroAbyssalUnderlord: Long? = null,

    @SerialName("npc_dota_hero_dark_willow")
    val npcDotaHeroDarkWillow: Long? = null,

    @SerialName("npc_dota_hero_hoodwink")
    val npcDotaHeroHoodwink: Long? = null
)

@Serializable
data class JakiroDualBreath(
    @SerialName("npc_dota_hero_dark_willow")
    val npcDotaHeroDarkWillow: Long? = null,

    @SerialName("npc_dota_hero_phantom_assassin")
    val npcDotaHeroPhantomAssassin: Long? = null,

    @SerialName("npc_dota_hero_hoodwink")
    val npcDotaHeroHoodwink: Long? = null,

    @SerialName("npc_dota_hero_vengefulspirit")
    val npcDotaHeroVengefulspirit: Long? = null,

    @SerialName("npc_dota_hero_doom_bringer")
    val npcDotaHeroDoomBringer: Long? = null
)

@Serializable
data class JuggernautOmniSlash(
    @SerialName("npc_dota_hero_dark_willow")
    val npcDotaHeroDarkWillow: Long? = null,

    @SerialName("npc_dota_hero_phantom_assassin")
    val npcDotaHeroPhantomAssassin: Long? = null,

    @SerialName("npc_dota_hero_doom_bringer")
    val npcDotaHeroDoomBringer: Long? = null
)

@Serializable
data class SatyrSoulstealerManaBurn(
    @SerialName("npc_dota_hero_juggernaut")
    val npcDotaHeroJuggernaut: Long? = null,

    @SerialName("npc_dota_hero_jakiro")
    val npcDotaHeroJakiro: Long? = null
)

@Serializable
data class Benchmarks(
    @SerialName("gold_per_min")
    val goldPerMin: Map<String, Double>? = null,

    @SerialName("xp_per_min")
    val xpPerMin: Map<String, Double>? = null,

    @SerialName("kills_per_min")
    val killsPerMin: Map<String, Double>? = null,

    @SerialName("last_hits_per_min")
    val lastHitsPerMin: Map<String, Double>? = null,

    @SerialName("hero_damage_per_min")
    val heroDamagePerMin: Map<String, Double>? = null,

    @SerialName("hero_healing_per_min")
    val heroHealingPerMin: Map<String, Double>? = null,

    @SerialName("tower_damage")
    val towerDamage: Map<String, Double>? = null,

    @SerialName("stuns_per_min")
    val stunsPerMin: Map<String, Double>? = null,

    val lhten: Map<String, Double>? = null
)

@Serializable
data class Cosmetic(
    @SerialName("item_id")
    val itemID: Long? = null,

    val name: String? = null,
    val prefab: Prefab? = null,

    @SerialName("creation_date")
    val creationDate: String? = null,

    @SerialName("image_inventory")
    val imageInventory: String? = null,

    @SerialName("image_path")
    val imagePath: String? = null,

    @SerialName("item_description")
    val itemDescription: String? = null,

    @SerialName("item_name")
    val itemName: String? = null,

    @SerialName("item_rarity")
    val itemRarity: String? = null,

    @SerialName("item_type_name")
    val itemTypeName: String? = null,

    @SerialName("used_by_heroes")
    val usedByHeroes: String? = null
)

@Serializable
enum class Prefab(val value: String) {
    Courier("courier"),
    Ward("ward"),
    Wearable("wearable");

    companion object : KSerializer<Prefab> {
        override val descriptor: SerialDescriptor
            get() {
                return PrimitiveSerialDescriptor("wisp.network.models.Prefab", PrimitiveKind.STRING)
            }

        override fun deserialize(decoder: Decoder): Prefab = when (val value = decoder.decodeString()) {
            "courier" -> Courier
            "ward" -> Ward
            "wearable" -> Wearable
            else -> throw IllegalArgumentException("Prefab could not parse: $value")
        }

        override fun serialize(encoder: Encoder, value: Prefab) {
            return encoder.encodeString(value.value)
        }
    }
}

@Serializable
data class DamageTargets(
    @SerialName("hoodwink_bushwhack")
    val hoodwinkBushwhack: Map<String, Long>? = null,

    @SerialName("null")
    val damageTargetsNull: Map<String, Long>? = null,

    @SerialName("hoodwink_acorn_shot")
    val hoodwinkAcornShot: Map<String, Long>? = null,

    @SerialName("hoodwink_sharpshooter")
    val hoodwinkSharpshooter: Map<String, Long>? = null,

    val maelstrom: Maelstrom? = null,

    @SerialName("doom_bringer_scorched_earth")
    val doomBringerScorchedEarth: DoomBringerScorchedEarth? = null,

    @SerialName("satyr_soulstealer_mana_burn")
    val satyrSoulstealerManaBurn: SatyrSoulstealerManaBurn? = null,

    @SerialName("doom_bringer_doom")
    val doomBringerDoom: Map<String, Long>? = null,

    @SerialName("centaur_khan_war_stomp")
    val centaurKhanWarStomp: DarkWillowCursedCrown? = null,

    @SerialName("doom_bringer_infernal_blade")
    val doomBringerInfernalBlade: Map<String, Long>? = null,

    @SerialName("overwhelming_blink")
    val overwhelmingBlink: DarkWillowCursedCrown? = null,

    val bfury: Map<String, Long>? = null,

    @SerialName("dark_willow_shadow_realm")
    val darkWillowShadowRealm: Map<String, Long>? = null,

    @SerialName("dark_willow_bramble_maze")
    val darkWillowBrambleMaze: Map<String, Long>? = null,

    @SerialName("dark_willow_bedlam")
    val darkWillowBedlam: Map<String, Long>? = null,

    @SerialName("vengefulspirit_magic_missile")
    val vengefulspiritMagicMissile: Map<String, Long>? = null,

    @SerialName("vengefulspirit_wave_of_terror")
    val vengefulspiritWaveOfTerror: Map<String, Long>? = null,

    @SerialName("zuus_arc_lightning")
    val zuusArcLightning: JakiroDualBreath? = null,

    @SerialName("zuus_static_field")
    val zuusStaticField: JakiroDualBreath? = null,

    @SerialName("zuus_thundergods_wrath")
    val zuusThundergodsWrath: JakiroDualBreath? = null,

    @SerialName("zuus_lightning_bolt")
    val zuusLightningBolt: JakiroDualBreath? = null,

    @SerialName("abyssal_underlord_firestorm")
    val abyssalUnderlordFirestorm: JakiroDualBreath? = null,

    @SerialName("juggernaut_blade_fury")
    val juggernautBladeFury: JakiroDualBreath? = null,

    @SerialName("juggernaut_omni_slash")
    val juggernautOmniSlash: JakiroDualBreath? = null,

    @SerialName("rubick_fade_bolt")
    val rubickFadeBolt: JakiroDualBreath? = null,

    @SerialName("rubick_telekinesis")
    val rubickTelekinesis: RubickTelekinesis? = null,

    @SerialName("ethereal_blade")
    val etherealBlade: EtherealBlade? = null,

    @SerialName("jakiro_dual_breath")
    val jakiroDualBreath: JakiroDualBreath? = null,

    @SerialName("jakiro_liquid_fire")
    val jakiroLiquidFire: JakiroDualBreath? = null,

    @SerialName("jakiro_ice_path")
    val jakiroIcePath: JakiroDualBreath? = null,

    @SerialName("jakiro_macropyre")
    val jakiroMacropyre: JakiroDualBreath? = null,

    @SerialName("jakiro_liquid_ice")
    val jakiroLiquidIce: JakiroDualBreath? = null
)

@Serializable
data class DoomBringerScorchedEarth(
    @SerialName("npc_dota_hero_juggernaut")
    val npcDotaHeroJuggernaut: Long? = null,

    @SerialName("npc_dota_hero_jakiro")
    val npcDotaHeroJakiro: Long? = null,

    @SerialName("npc_dota_hero_rubick")
    val npcDotaHeroRubick: Long? = null,

    @SerialName("npc_dota_hero_abyssal_underlord")
    val npcDotaHeroAbyssalUnderlord: Long? = null,

    @SerialName("npc_dota_hero_doom_bringer")
    val npcDotaHeroDoomBringer: Long? = null,

    @SerialName("npc_dota_hero_dark_willow")
    val npcDotaHeroDarkWillow: Long? = null
)

@Serializable
data class EtherealBlade(
    @SerialName("npc_dota_hero_phantom_assassin")
    val npcDotaHeroPhantomAssassin: Long? = null
)

@Serializable
data class Maelstrom(
    @SerialName("npc_dota_hero_juggernaut")
    val npcDotaHeroJuggernaut: Long? = null
)

@Serializable
data class RubickTelekinesis(
    @SerialName("npc_dota_hero_dark_willow")
    val npcDotaHeroDarkWillow: Long? = null,

    @SerialName("npc_dota_hero_doom_bringer")
    val npcDotaHeroDoomBringer: Long? = null
)

@Serializable
data class KillsLogElement(
    val time: Long? = null,
    val key: String? = null
)

@Serializable
data class MaxHeroHit(
    val type: MaxHeroHitType? = null,
    val time: Long? = null,
    val max: Boolean? = null,
    val inflictor: String? = null,
    val unit: String? = null,
    val key: String? = null,
    val value: Long? = null,
    val slot: Long? = null,

    @SerialName("player_slot")
    val playerSlot: Long? = null
)

@Serializable
enum class MaxHeroHitType(val value: String) {
    MaxHeroHit("max_hero_hit");

    companion object : KSerializer<MaxHeroHitType> {
        override val descriptor: SerialDescriptor
            get() {
                return PrimitiveSerialDescriptor("wisp.network.models.MaxHeroHitType", PrimitiveKind.STRING)
            }

        override fun deserialize(decoder: Decoder): MaxHeroHitType = when (val value = decoder.decodeString()) {
            "max_hero_hit" -> MaxHeroHit
            else -> throw IllegalArgumentException("MaxHeroHitType could not parse: $value")
        }

        override fun serialize(encoder: Encoder, value: MaxHeroHitType) {
            return encoder.encodeString(value.value)
        }
    }
}

@Serializable
data class ObsLeftLogElement(
    val time: Long? = null,
    val type: ObsLeftLogType? = null,
    val key: String? = null,
    val slot: Long? = null,
    val attackername: String? = null,
    val x: Long? = null,
    val y: Long? = null,
    val z: Long? = null,
    val entityleft: Boolean? = null,
    val ehandle: Long? = null,

    @SerialName("player_slot")
    val playerSlot: Long? = null
)

@Serializable
enum class ObsLeftLogType(val value: String) {
    ObsLeftLog("obs_left_log"),
    ObsLog("obs_log"),
    SenLeftLog("sen_left_log"),
    SenLog("sen_log");

    companion object : KSerializer<ObsLeftLogType> {
        override val descriptor: SerialDescriptor
            get() {
                return PrimitiveSerialDescriptor("wisp.network.models.ObsLeftLogType", PrimitiveKind.STRING)
            }

        override fun deserialize(decoder: Decoder): ObsLeftLogType = when (val value = decoder.decodeString()) {
            "obs_left_log" -> ObsLeftLog
            "obs_log" -> ObsLog
            "sen_left_log" -> SenLeftLog
            "sen_log" -> SenLog
            else -> throw IllegalArgumentException("ObsLeftLogType could not parse: $value")
        }

        override fun serialize(encoder: Encoder, value: ObsLeftLogType) {
            return encoder.encodeString(value.value)
        }
    }
}

@Serializable
data class PermanentBuff(
    @SerialName("permanent_buff")
    val permanentBuff: Long? = null,

    @SerialName("stack_count")
    val stackCount: Long? = null
)

@Serializable
data class RunesLog(
    val time: Long? = null,
    val key: Long? = null
)

@Serializable
data class Teamfight(
    val start: Long? = null,
    val end: Long? = null,

    @SerialName("last_death")
    val lastDeath: Long? = null,

    val deaths: Long? = null,
    val players: List<TeamfightPlayer>? = null
)

@Serializable
data class TeamfightPlayer(
    @SerialName("deaths_pos")
    val deathsPos: Map<String, Map<String, Long>>? = null,

    @SerialName("ability_uses")
    val abilityUses: Map<String, Long>? = null,

    @SerialName("ability_targets")
    val abilityTargets: MyWordCounts? = null,

    @SerialName("item_uses")
    val itemUses: Map<String, Long>? = null,

    val killed: Map<String, Long>? = null,
    val deaths: Long? = null,
    val buybacks: Long? = null,
    val damage: Long? = null,
    val healing: Long? = null,

    @SerialName("gold_delta")
    val goldDelta: Long? = null,

    @SerialName("xp_delta")
    val xpDelta: Long? = null,

    @SerialName("xp_start")
    val xpStart: Long? = null,

    @SerialName("xp_end")
    val xpEnd: Long? = null
)
