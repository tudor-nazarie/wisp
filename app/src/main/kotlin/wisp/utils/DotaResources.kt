package wisp.utils

import kotlinx.serialization.decodeFromString

object DotaResources {
    val heroes: List<Hero> by lazy {
        val json = DotaResources::class.java.getResource("/json/heroes.json").readText()
        formatJson.decodeFromString(json)
    }

    val gameModes: Map<String, String> by lazy {
        val json = DotaResources::class.java.getResource("/json/game_modes.json").readText()
        formatJson.decodeFromString(json)
    }

    val lobbyTypes: Map<String, String> by lazy {
        val json = DotaResources::class.java.getResource("/json/lobby_types.json").readText()
        formatJson.decodeFromString(json)
    }
}