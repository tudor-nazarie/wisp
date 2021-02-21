package wisp.settings

import com.typesafe.config.ConfigFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.hocon.decodeFromConfig

@Serializable
data class Settings(
    val version: String,
    val token: String,
    val activators: String = ".",
    val owner: Long? = null,
) {
    companion object {
        val settings: Settings by lazy { Hocon.decodeFromConfig(ConfigFactory.load()) }
    }
}