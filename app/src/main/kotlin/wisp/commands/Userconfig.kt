package wisp.commands

import wisp.db.DbSettings
import wisp.db.User
import wisp.db.Users
import wisp.db.transaction
import wisp.utils.R
import wisp.utils.embed
import java.time.LocalDateTime

val userconfig: Command = command {
    name = "userconfig"
    description = "Configure user-specific settings"
    subCommands {
        command {
            name = "steam"
            description = "Configure user's Steam ID"
            handler { message, args, prefix, _ ->
                val channel = message.channel

                val snowflake =
                    if (message.mentionedMembers.isNotEmpty())
                        message.mentionedMembers[0].idLong
                    else
                        message.author.idLong

                val user = DbSettings.db.transaction {
                    User.find { Users.discordSnowflake eq snowflake }.firstOrNull()
                }

                if (args.isEmpty()) {
                    channel.sendMessage(
                        embed {
                            title {
                                title = "steam"
                            }
                            color = R.colors.dotaRed
                            description = """
                                Links your Steam account to your Discord account.
                                
                                To unregister, set this to `clear` or `reset`.
                            """.trimIndent()
                            fields {
                                field {
                                    name = "Value"
                                    value = user?.steamId?.toString() ?: "Not set"
                                    inline = true
                                }
                                field {
                                    name = "Example"
                                    value = "`${prefix}userconfig steam 112832481`"
                                    inline = true
                                }
                            }
                        }
                    ).queue()
                    return@handler
                }
                // TODO: 21/02/2021 add some error checking here
                val sId = args[0].toLong()
                DbSettings.db.transaction {
                    if (user == null) {
                        User.new {
                            discordSnowflake = snowflake
                            steamId = sId
                        }
                    } else {
                        user.steamId = sId
                        user.updatedAt = LocalDateTime.now()
                    }
                }
                message.addReaction("✅").queue()
            }
        }
    }
    handler { message, _, prefix, usedAlias ->
        val channel = message.channel

        channel.sendMessage(
            embed {
                title {
                    title = "$prefix$usedAlias <name> [value] (@User)"
                }
                color = R.colors.dotaRed
                description = """
                    Configures user specific settings.
                    
                    You can get more information by using `$prefix$usedAlias <settingname>`, and you can configure a setting with `$prefix$usedAlias <settingname> <value> (@User)`. The user argument is optional. If it is not specified, the author will be used instead.
                """.trimIndent()
                fields {
                    field {
                        name = "Settings:"
                        value = """
                            `steam`
                        """.trimIndent()
                    }
                    field {
                        name = "Examples:"
                        value = """
                            `$prefix$usedAlias steam 112832481`
                        """.trimIndent()
                    }
                }
            }
        ).queue()
    }
}
