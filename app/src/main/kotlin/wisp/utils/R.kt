package wisp.utils

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import java.awt.Color

object R {
    private val logger = KotlinLogging.logger {}

    val json: Json by lazy {
        Json {
            ignoreUnknownKeys = true
        }
    }

    object colors {
        val dotaRed = Color(167, 39, 14)
    }

    @Suppress("ClassName")
    object dota {
        private const val heroesJsonPath = "/json/heroes.json"
        private const val gameModesJsonPath = "/json/game_modes.json"
        private const val lobbyTypesJsonPath = "/json/lobby_types.json"

        val heroes: List<Hero> by lazy {
            val json = R::class.java.getResource(heroesJsonPath).readText()
            val heroes = R.json.decodeFromString<List<Hero>>(json)
            logger.debug { "Loaded hero data from '$heroesJsonPath'" }
            return@lazy heroes
        }

        val gameModes: Map<String, String> by lazy {
            val json = R::class.java.getResource("/json/game_modes.json").readText()
            val gameModes = R.json.decodeFromString<Map<String, String>>(json)
            logger.debug { "Loaded game modes data from '$gameModesJsonPath'" }
            return@lazy gameModes
        }

        val lobbyTypes: Map<String, String> by lazy {
            val json = R::class.java.getResource("/json/lobby_types.json").readText()
            val lobbyTypes = R.json.decodeFromString<Map<String, String>>(json)
            logger.debug { "Loaded lobby types data from '$lobbyTypesJsonPath'" }
            return@lazy lobbyTypes
        }
    }
}