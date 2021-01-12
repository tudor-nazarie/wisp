package commands

import com.squareup.moshi.Moshi
import db.DbSettings
import db.Heroes
import di.getInstance
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import network.OpenDotaService
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

val heroes = Command("heroes", "Manage the hero cache") { event ->
    val content = event.message.contentRaw
    val tokens = content.split(Regex("\\s+"))

    when (tokens[1]) {
        "update" -> updateHeroCache(event)
        "count" -> countHeroCache(event)
    }
}

private suspend fun updateHeroCache(event: MessageReceivedEvent) {
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
        SchemaUtils.create(Heroes)
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
        SchemaUtils.create(Heroes)

        Heroes.selectAll().toList().size
    }
    event.channel.sendMessage("There are currently $count heroes in the cache.").queue()
}
