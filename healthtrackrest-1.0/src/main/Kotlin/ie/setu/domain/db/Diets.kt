package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Diets : Table("diets"){
    val id = integer("id").autoIncrement().primaryKey()
    val description = varchar("description", 100)
    val food = varchar("food", 25)
    val calories = integer("calories")
    val intake = datetime("intake")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}