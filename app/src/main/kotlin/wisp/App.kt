package wisp

import mu.KotlinLogging
import net.dv8tion.jda.api.JDABuilder
import wisp.db.DbSettings
import wisp.settings.Settings.Companion.settings
import wisp.utils.R

private val logger = KotlinLogging.logger {}

fun main() {
    logger.debug { "Starting up Wisp bot version ${R.constants.VERSION}" }
    logger.debug { "Configured activators are \"${settings.activators}\"" }

    DbSettings.init()

    val jda = JDABuilder
        .createDefault(settings.token)
        .addEventListeners(WispListener)
        .build()
}
