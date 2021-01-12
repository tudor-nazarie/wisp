package db

import org.jetbrains.exposed.sql.Database

object DbSettings {
    val db by lazy {
        Database.connect("jdbc:sqlite:db.sqlite3", driver = "org.sqlite.JDBC")
    }
}
