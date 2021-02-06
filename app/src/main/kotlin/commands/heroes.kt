package commands

import com.squareup.moshi.Moshi
import db.DbSettings
import db.Heroes
import di.getInstance
import dsl.command
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import network.OpenDotaService
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

val heroes = command {
    name = "heroes"
    aliases { +"hero" }
    description = "Manage the hero cache"
    subCommands {
        command {
            name = "update"
            description = "Update the hero cache"
            handler { _, event -> updateHeroCache(event) }
        }
        command {
            name = "count"
            description = "Count the heroes in the cache"
            handler { _, event -> countHeroCache(event) }
        }
    }
    handler { _, event ->
        event.channel.sendMessage(
            "See `.help heroes` for options"
        ).queue()
    }
}

private suspend inline fun updateHeroCache(event: MessageReceivedEvent) {
    val openDotaService: OpenDotaService = getInstance()
    val moshi: Moshi = getInstance()
    val channel = event.channel

    val heroesResponse = openDotaService.getHeroes()
    if (!heroesResponse.isSuccessful) {
        channel.sendMessage("An error occurred while trying to get the hero list, try again later.").queue()
        return
    }
    val heroes = heroesResponse.body()!!
    val rolesAdapter = moshi.adapter(List::class.java)

    transaction(DbSettings.db) {
        if (!Heroes.exists()) {
            SchemaUtils.create(Heroes)
        }
        Heroes.deleteAll()

        for (hero in heroes) {
            Heroes.insert {
                it[heroId] = hero.id.toInt()
                it[name] = hero.name
                it[localizedName] = hero.localizedName
                it[primaryAttribute] = hero.primaryAttribute
                it[attackType] = hero.attackType
                it[rolesJson] = rolesAdapter.toJson(hero.roles)
                it[legs] = hero.legs
            }
        }
    }
    channel.sendMessage("Successfully imported ${heroes.size} heroes into the cache.").queue()
}

private fun countHeroCache(event: MessageReceivedEvent) {
    val count = transaction(DbSettings.db) {
        if (!Heroes.exists()) {
            0
        } else {
            Heroes.selectAll().count()
        }
    }
    event.channel.sendMessage("There are currently $count heroes in the cache.").queue()
}
