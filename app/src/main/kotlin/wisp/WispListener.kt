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


object WispListener : ListenerAdapter() {
    private val logger = KotlinLogging.logger {}

    override fun onReady(event: ReadyEvent) {
        val self = event.jda.selfUser
        logger.debug { "Logged in as ${self.name}#${self.discriminator}" }
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        val message = event.message
        val contentRaw = message.contentRaw
        val author = message.author
        if (event.author.isBot || !settings.activators.contains(contentRaw[0])) {
            return
        }
        val usedPrefix = contentRaw[0]
        val commandText = contentRaw.substring(1).trim()
        val parts = commandText.split(Regex("\\s+"))

        logger.debug { "Command execution from ${author.name}#${author.discriminator}, activator used: '$usedPrefix' \"$commandText\"" }

        launchCommand(parts[0], commands, parts.drop(1), message, usedPrefix, listOf(parts[0]))
    }

    private fun launchCommand(
        commandString: String,
        commands: List<Command>,
        args: List<String>,
        message: Message,
        usedPrefix: Char,
        commandStack: List<String> = emptyList(),
    ) {
        for (command in commands) {
            val aliases = listOf(command.name) + command.aliases
            if (aliases.contains(commandString)) {
                if (command.subCommands.isEmpty() || args.isEmpty()) {
                    logger.debug {
                        "Launching command \"${command.name}\", args \"$args\", stack \"" +
                                commandStack.fold("$") { acc, s -> "$acc.$s" } + "\""
                    }
                    if (!command.restricted || message.author.idLong == settings.owner) {
                        GlobalScope.launch { command.handler(message, args, usedPrefix, commandString) }
                    } else {
                        message.channel.sendMessage(
                            "You don't have permissions to run that command."
                        ).queue()
                    }
                } else {
                    launchCommand(
                        args[0],
                        command.subCommands,
                        args.drop(1),
                        message,
                        usedPrefix,
                        commandStack + args[0]
                    )
                }
                return
            }
        }
        message.channel.sendMessage(
            "No such command: `$commandString`"
        ).queue()
    }
}