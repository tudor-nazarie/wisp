package wisp.db

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object Users : IntIdTable() {
    val discordSnowflake = long("discord_snowflake").index(isUnique = true)
    val steamId = long("steam_id")
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())
}

class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)

    var discordSnowflake by Users.discordSnowflake
    var steamId by Users.steamId
    var createdAt by Users.createdAt
    var updatedAt by Users.updatedAt
}
