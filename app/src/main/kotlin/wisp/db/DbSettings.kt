package wisp.db

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction

object DbSettings {
    val db by lazy {
        Database.connect("jdbc:sqlite:db.sqlite3", driver = "org.sqlite.JDBC")
    }

    fun init() {
        db.transaction {
            SchemaUtils.create(
                Users,
            )
        }
    }
}

fun <T> Database.transaction(statement: Transaction.() -> T): T = transaction(this, statement)
