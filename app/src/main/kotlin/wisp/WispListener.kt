package wisp

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mu.KotlinLogging
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import wisp.commands.Command
import wisp.commands.commands
import wisp.settings.Settings.Companion.settings

private val logger = KotlinLogging.logger {}

object WispListener : ListenerAdapter() {
    override fun onReady(event: ReadyEvent) {
        val self = event.jda.selfUser
        logger.debug { "Logged in as ${self.name}#${self.discriminator}" }
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        val message = event.message
        val contentRaw = message.contentRaw
        if (event.author.isBot || !settings.activators.contains(contentRaw[0])) {
            return
        }
        val usedPrefix = contentRaw[0]
        val parts = contentRaw.substring(1).trim().split(Regex("\\s+"))
        launchCommand(parts[0], commands, parts.drop(1), message, usedPrefix)
    }

    private fun launchCommand(
        commandString: String,
        commands: List<Command>,
        args: List<String>,
        message: Message,
        usedPrefix: Char,
    ) {
        for (command in commands) {
            val aliases = listOf(command.name) + command.aliases
            if (aliases.contains(commandString)) {
                if (command.subCommands.isEmpty() || args.isEmpty()) {
                    if (!command.restricted || message.author.idLong == settings.owner) {
                        GlobalScope.launch { command.handler(message, args, usedPrefix, commandString) }
                    } else {
                        message.channel.sendMessage(
                            "You don't have permissions to run that command."
                        ).queue()
                    }
                } else {
                    launchCommand(args[0], command.subCommands, args.drop(1), message, usedPrefix)
                }
                return
            }
        }
        message.channel.sendMessage(
            "No such command: `$commandString`"
        ).queue()
    }
}