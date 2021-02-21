import config.Config
import db.DbSettings
import di.wispModule
import net.dv8tion.jda.api.JDABuilder
import org.koin.core.context.startKoin

fun main(args: Array<String>) {
    val koin = startKoin {
        modules(wispModule)
    }.koin

    val config =
        if (args.isEmpty())
            Config.read()
        else
            Config.read(args[0])
    koin.declare(config)

    DbSettings.init()

    val jda = JDABuilder.createDefault(config.token)
        .addEventListeners(WispAdapter)
        .build()
}
