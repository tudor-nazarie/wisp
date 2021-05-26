package wisp.filters

const val oversightText =
    "The biggest oversight with Dark Willow is that she's unbelievably sexy. I can't go on a hour of my day without thinking about plowing that tight wooden ass. I'd kill a man in cold blood just to spend a minute with her crotch grinding against my throbbing manhood as she whispers terribly dirty things to me in her geographically ambiguous accent."

val biggestOversight = filter {
    regex = "biggest\\s+oversight".toRegex(RegexOption.IGNORE_CASE)
    handler { message ->
        val channel = message.channel

        message.reply(oversightText).queue()
    }
}
