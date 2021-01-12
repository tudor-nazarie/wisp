package network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Hero(
    val id: String,
    val name: String,
    @Json(name = "localized_name") val localizedName: String,
    @Json(name = "primary_attr") val primaryAttribute: String,
    @Json(name = "attack_type") val attackType: String,
    val roles: List<String>,
    val legs: Int,
)
