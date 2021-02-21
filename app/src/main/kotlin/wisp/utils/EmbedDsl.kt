package wisp.utils

import net.dv8tion.jda.api.entities.MessageEmbed
import java.awt.Color
import java.time.temporal.TemporalAccessor

@DslMarker
annotation class EmbedDsl

data class EmbedAuthor(val name: String?, val url: String? = null, val iconUrl: String? = null)

@EmbedDsl
class EmbedAuthorBuilder {
    var name: String? = null
    var url: String? = null
    var iconUrl: String? = null

    fun build(): EmbedAuthor = EmbedAuthor(name, url, iconUrl)
}

data class EmbedTitle(val title: String?, val url: String? = null)

@EmbedDsl
class EmbedTitleBuilder {
    var title: String? = null
    var url: String? = null

    fun build(): EmbedTitle = EmbedTitle(title, url)
}

data class EmbedFooter(val text: String?, val iconUrl: String? = null)

@EmbedDsl
class EmbedFooterBuilder {
    var text: String? = null
    var iconUrl: String? = null

    fun build(): EmbedFooter = EmbedFooter(text, iconUrl)
}

data class EmbedField(val name: String?, val value: String?, val inline: Boolean = false)

@EmbedDsl
class EmbedFieldBuilder {
    var name: String? = null
    var value: String? = null
    var inline: Boolean = false

    fun build(): EmbedField = EmbedField(name, value, inline)
}

@EmbedDsl
class EmbedFields : ArrayList<EmbedField>() {
    fun field(init: EmbedFieldBuilder.() -> Unit) {
        add(EmbedFieldBuilder().apply(init).build())
    }
}

@EmbedDsl
class EmbedBuilder {
    private var author: EmbedAuthor? = null
    var color: Color? = null
    private var title: EmbedTitle? = null
    var thumbnail: String? = null
    var description: String? = null
    var timestamp: TemporalAccessor? = null
    var image: String? = null
    private var footer: EmbedFooter? = null
    private var fields = mutableListOf<EmbedField>()

    fun author(init: EmbedAuthorBuilder.() -> Unit) {
        author = EmbedAuthorBuilder().apply(init).build()
    }

    fun title(init: EmbedTitleBuilder.() -> Unit) {
        title = EmbedTitleBuilder().apply(init).build()
    }

    fun footer(init: EmbedFooterBuilder.() -> Unit) {
        footer = EmbedFooterBuilder().apply(init).build()
    }

    fun fields(init: EmbedFields.() -> Unit) {
        fields.addAll(EmbedFields().apply(init))
    }

    fun build(): MessageEmbed {
        val builder = net.dv8tion.jda.api.EmbedBuilder()
            .setTitle(title?.title, title?.url)
            .setDescription(description)
            .setTimestamp(timestamp)
            .setColor(color)
            .setThumbnail(thumbnail)
            .setImage(image)
            .setAuthor(author?.name, author?.url, author?.iconUrl)
            .setFooter(footer?.text, footer?.iconUrl)
        fields.forEach { field ->
            builder.addField(field.name, field.value, field.inline)
        }
        return builder.build()
    }
}

fun embed(init: EmbedBuilder.() -> Unit): MessageEmbed = EmbedBuilder().apply(init).build()
