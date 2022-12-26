package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Medicines: Table("medicines") {
    val id = integer("id").autoIncrement().primaryKey()
    val description = varchar("description", 100)
    val medicine = varchar("medicine", 25)
    val day = varchar("day", 20)
    val intake = datetime("intake")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}