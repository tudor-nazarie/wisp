package wisp.utils

import kotlinx.serialization.decodeFromString
import mu.KotlinLogging

object DotaResources {
    private val logger = KotlinLogging.logger {}

    private const val heroesJsonPath = "/json/heroes.json"
    private const val gameModesJsonPath = "/json/game_modes.json"
    private const val lobbyTypesJsonPath = "/json/lobby_types.json"

    val heroes: List<Hero> by lazy {
        val json = DotaResources::class.java.getResource(heroesJsonPath).readText()
        val heroes = formatJson.decodeFromString<List<Hero>>(json)
        logger.debug { "Loaded hero data from '$heroesJsonPath'" }
        return@lazy heroes
    }

    val gameModes: Map<String, String> by lazy {
        val json = DotaResources::class.java.getResource("/json/game_modes.json").readText()
        val gameModes = formatJson.decodeFromString<Map<String, String>>(json)
        logger.debug { "Loaded game modes data from '$gameModesJsonPath'" }
        return@lazy gameModes
    }

    val lobbyTypes: Map<String, String> by lazy {
        val json = DotaResources::class.java.getResource("/json/lobby_types.json").readText()
        val lobbyTypes = formatJson.decodeFromString<Map<String, String>>(json)
        logger.debug { "Loaded lobby types data from '$lobbyTypesJsonPath'" }
        return@lazy lobbyTypes
    }
}