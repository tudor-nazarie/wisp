package dsl

import commands.Command
import commands.CommandHandler

@DslMarker
annotation class CommandDsl

@CommandDsl
class SubCommands : ArrayList<Command>() {
    fun command(init: CommandBuilder.() -> Unit) {
        add(CommandBuilder().apply(init).build())
    }
}

@CommandDsl
class Aliases : ArrayList<String>() {
    operator fun String.unaryPlus() {
        add(this)
    }
}

@CommandDsl
class CommandBuilder {
    var name: String? = null
    private var aliases = mutableListOf<String>()
    var description: String? = null
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
            handler = handler!!
        )
    }
}

fun command(init: CommandBuilder.() -> Unit): Command = CommandBuilder().apply(init).build()
