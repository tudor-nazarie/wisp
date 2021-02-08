import config.Config
import db.DbSettings
import di.wispModule
import net.dv8tion.jda.api.JDABuilder
import org.koin.core.context.startKoin

fun main(args: Array<String>) {
    val koin = startKoin {
        modules(wispModule)
    }.koin

    val config = Config.read(
        if (args.isEmpty())
            "config.json"
        else
            args[0]
    )
    if (config == null) {
        println("Could not read config file")
        return
    }
    koin.declare(config)

    DbSettings.init()

    val jda = JDABuilder.createDefault(config.token)
        .addEventListeners(WispAdapter)
        .build()
}
