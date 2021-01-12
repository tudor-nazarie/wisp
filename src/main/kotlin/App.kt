import config.Config
import di.wispModule
import net.dv8tion.jda.api.JDABuilder
import org.koin.core.context.startKoin

fun main() {
    startKoin {
        modules(wispModule)
    }

    val config = Config.read("config.json")
    if (config == null) {
        println("Could not read config file")
        return
    }

    val jda = JDABuilder.createDefault(config.token)
        .addEventListeners(WispAdapter(config))
        .build()
}