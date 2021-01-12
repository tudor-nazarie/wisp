import commands.Command
import commands.commands
import commands.errorHandler
import config.Config
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class WispAdapter(private val config: Config) : ListenerAdapter() {
    override fun onReady(event: ReadyEvent) {
        val self = event.jda.selfUser
        println("Logged in as ${self.name}#${self.discriminator}")
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        val content = event.message.contentRaw
        if (event.message.author.isBot || !config.activators.contains(content[0])) {
            return
        }
        val commandString = content.split(Regex("\\s+"))[0].substring(1)
        val command: Command? = try {
            commands.first { it.names.contains(commandString) }
        } catch (e: NoSuchElementException) {
            null
        }
        GlobalScope.launch {
            if (command != null) {
                command.handler(event)
            } else {
                errorHandler(event)
            }
        }
    }
}