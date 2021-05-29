package wisp.utils

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Hero(
    val aliases: String? = null,

    @SerialName("attack_damage_max")
    val attackDamageMax: Long? = null,

    @SerialName("attack_damage_min")
    val attackDamageMin: Long? = null,

    @SerialName("attack_point")
    val attackPoint: Double? = null,

    @SerialName("attack_projectile_speed")
    val attackProjectileSpeed: Long? = null,

    @SerialName("attack_range")
    val attackRange: Long? = null,

    @SerialName("attack_rate")
    val attackRate: Double? = null,

    @SerialName("attr_agility_base")
    val attrAgilityBase: Long? = null,

    @SerialName("attr_agility_gain")
    val attrAgilityGain: Double? = null,

    @SerialName("attr_intelligence_base")
    val attrIntelligenceBase: Long? = null,

    @SerialName("attr_intelligence_gain")
    val attrIntelligenceGain: Double? = null,

    @SerialName("attr_primary")
    val attrPrimary: String? = null,

    @SerialName("attr_strength_base")
    val attrStrengthBase: Long? = null,

    @SerialName("attr_strength_gain")
    val attrStrengthGain: Double? = null,

    @SerialName("base_armor")
    val baseArmor: Long? = null,

    @SerialName("base_attack_speed")
    val baseAttackSpeed: Long? = null,

    @SerialName("base_health_regen")
    val baseHealthRegen: Double? = null,

    @SerialName("base_mana_regen")
    val baseManaRegen: Long? = null,

    @SerialName("base_movement")
    val baseMovement: Long? = null,

    val bio: String? = null,
    val color: String? = null,

    @SerialName("full_name")
    val fullName: String? = null,

    val hype: String? = null,
    val icon: String? = null,
    val id: Long? = null,
    val image: String? = null,

    @SerialName("is_melee")
    val isMelee: Long? = null,

    @SerialName("json_data")
    val jsonData: String? = null,

    val legs: Long? = null,

    @SerialName("localized_name")
    val localizedName: String? = null,

    @SerialName("magic_resistance")
    val magicResistance: Long? = null,

    val material: String? = null,

    @SerialName("media_name")
    val mediaName: String? = null,

    val name: String? = null,
    val portrait: String? = null,

    @SerialName("real_name")
    val realName: String? = null,

    @SerialName("role_levels")
    val roleLevels: String? = null,

    val roles: String? = null,
    val team: String? = null,

    @SerialName("turn_rate")
    val turnRate: Double? = null,

    @SerialName("vision_day")
    val visionDay: Long? = null,

    @SerialName("vision_night")
    val visionNight: Long? = null
)
