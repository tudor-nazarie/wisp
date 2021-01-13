import config.Config
import di.wispModule
import net.dv8tion.jda.api.JDABuilder
import org.koin.core.context.startKoin

fun main() {
    val koin = startKoin {
        modules(wispModule)
    }.koin

    val config = Config.read("config.json")
    if (config == null) {
        println("Could not read config file")
        return
    }
    koin.declare(config)

    val jda = JDABuilder.createDefault(config.token)
        .addEventListeners(WispAdapter)
        .build()
}
