package db

import org.jetbrains.exposed.sql.Table

object Heroes : Table() {
    val heroId = integer("hero_id").autoIncrement()
    val name = varchar("name", 256)
    val localizedName = varchar("localized_name", 256)
    val primaryAttribute = varchar("primary_attr", 3)
    val attackType = varchar("attack_type", 10)
    val rolesJson = varchar("roles_json", 512)
    val legs = integer("legs")

    override val primaryKey = PrimaryKey(heroId, name = "PK_Hero_ID")
}
