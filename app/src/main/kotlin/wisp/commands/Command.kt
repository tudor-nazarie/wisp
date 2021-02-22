package wisp.commands

import net.dv8tion.jda.api.entities.Message

typealias CommandHandler = suspend (message: Message, args: List<String>, prefix: Char, usedAlias: String) -> Unit

data class Command(
    val name: String,
    val aliases: List<String> = emptyList(),
    val description: String,
    val subCommands: List<Command> = emptyList(),
    val restricted: Boolean = false,
    val handler: CommandHandler,
)

val commands: List<Command> = listOf(
    ping,
    shutdown,
    userconfig,
    lastMatch,
)

@DslMarker
private annotation class CommandDsl

@CommandDsl
class Aliases : ArrayList<String>() {
    operator fun String.unaryPlus() {
        add(this)
    }
}

@CommandDsl
class SubCommands : ArrayList<Command>() {
    fun command(init: CommandBuilder.() -> Unit) {
        add(CommandBuilder().apply(init).build())
    }
}

@CommandDsl
class CommandBuilder {
    var name: String? = null
    private var aliases = mutableListOf<String>()
    var description: String? = null
    var restricted: Boolean = false
    private var subCommands = mutableListOf<Command>()
    private var handler: CommandHandler? = null

    fun aliases(init: Aliases.() -> Unit) {
        aliases.addAll(Aliases().apply(init))
    }

    fun subCommands(init: SubCommands.() -> Unit) {
        subCommands.addAll(SubCommands().apply(init))
    }

    fun handler(func: CommandHandler) {
        handler = func
    }

    fun build(): Command {
        return Command(
            name = name!!,
            aliases = aliases,
            description = description!!,
            subCommands = subCommands,
            restricted = restricted,
            handler = handler!!,
        )
    }
}

fun command(init: CommandBuilder.() -> Unit): Command = CommandBuilder().apply(init).build()
