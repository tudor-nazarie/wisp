import commands.Command
import commands.commands
import config.Config
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object WispAdapter : ListenerAdapter(), KoinComponent {
    private val config: Config by inject()

    override fun onReady(event: ReadyEvent) {
        val self = event.jda.selfUser
        println("Logged in as ${self.name}#${self.discriminator}")
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        val content = event.message.contentRaw
        if (event.message.author.isBot || !config.activators.contains(content[0])) {
            return
        }
        val args = content.substring(1).split(Regex("\\s+"))
        launchCommand(args[0], commands, args.drop(1), event)
    }

    private fun launchCommand(
        commandString: String,
        commands: List<Command>,
        args: List<String>,
        event: MessageReceivedEvent
    ) {
        for (command in commands) {
            val aliases = listOf(command.name) + command.aliases
            if (aliases.contains(commandString)) {
                if (command.subCommands.isEmpty()) {
                    GlobalScope.launch {
                        command.handler(args, event)
                    }
                } else {
                    launchCommand(args[0], command.subCommands, args.drop(1), event)
                }
                return
            }
        }
        // TODO: 25/01/2021 show a help command example
        event.channel.sendMessage(
            "No such command: `$commandString`."
        ).queue()
    }
}
