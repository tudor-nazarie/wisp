package commands

import config.Config
import di.getInstance
import dsl.command
import kotlin.text.StringBuilder

val help = command {
    name = "help"
    description = "Displays usage information"
    handler { args, event ->
        val channel = event.message.channel
        val config: Config = getInstance()

        if (args.isEmpty()) {
            channel.sendMessage(
                "Configured prefixes: `${config.activators}`. " +
                        "See `${config.activators[0]}help <command>` for more info."
            ).queue()
            return@handler
        }

        val cmd = getCommandFromArgs(args, commands)
        val helpMessage = getHelpMessage(cmd!!, 0, "")
        channel.sendMessage(helpMessage).queue()
    }
}

// TODO: 25/01/2021 this will probably break on some edge case, prove that it is sound
private fun getCommandFromArgs(args: List<String>, commands: List<Command>): Command? {
    if (args.isEmpty()) {
        return null
    }
    val current = args[0]
    for (command in commands) {
        val aliases = listOf(command.name) + command.aliases
        if (aliases.contains(current)) {
            val res = getCommandFromArgs(args.drop(1), command.subCommands)
            return res ?: command
        }
    }
    return null
}

private fun getHelpMessage(command: Command, padding: Int, commandPrefix: String): String {
    val result = StringBuilder()
    if (padding == 0) {
        // means we are in a top level call
        result.append("```\n")
    }
    // TODO: 25/01/2021 figure out a way to remove all this ${prefix} bs
    val prefix = " ".repeat(padding)

    result.append("${prefix}Command: $commandPrefix~~${command.name}~~\n")
    if (command.aliases.isNotEmpty()) {
        result.append("${prefix}Aliases: ${command.aliases.joinToString()}\n")
    }
    result.append("${prefix}Description: ${command.description}\n")

    if (command.examples.isNotEmpty()) {
        result.append("${prefix}Usage examples:\n")
        for (example in command.examples) {
            result.append("${prefix}* ${example.first} -- ${example.second}\n")
        }
    }

    if (command.subCommands.isNotEmpty()) {
        result.append("${prefix}Sub-commands:\n")
        for (subcmd in command.subCommands) {
            result.append(getHelpMessage(subcmd, padding + 2, "$commandPrefix${command.name} "))
        }
    }

    if (padding == 0) {
        result.append("```")
    } else {
        result.append("\n")
    }
    return result.toString()
}
