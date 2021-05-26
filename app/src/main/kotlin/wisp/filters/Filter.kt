package wisp.filters

import net.dv8tion.jda.api.entities.Message

data class Filter(
    val regex: Regex,
    val handler: FilterHandler,
)

val filters: List<Filter> = listOf(
    biggestOversight
)

@DslMarker
annotation class FilterDsl

typealias FilterHandler = suspend (message: Message) -> Unit

@FilterDsl
class FilterBuilder {
    var regex: Regex? = null
    private var handler: FilterHandler? = null

    fun handler(func: FilterHandler) {
        handler = func
    }

    fun build(): Filter {
        return Filter(
            regex = regex!!,
            handler = handler!!,
        )
    }
}

fun filter(init: FilterBuilder.() -> Unit): Filter = FilterBuilder().apply(init).build()
