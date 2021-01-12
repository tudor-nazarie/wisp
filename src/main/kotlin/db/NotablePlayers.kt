package db

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.LocalDateTime

object NotablePlayers : Table() {
    val steamId: Column<Long> = long("steam_id")
    val snowflake: Column<Long> = long("snowflake")
    val date: Column<LocalDateTime> = datetime("date")
    override val primaryKey = PrimaryKey(snowflake, name = "PK_NotablePlayers_Snowflake")
}
