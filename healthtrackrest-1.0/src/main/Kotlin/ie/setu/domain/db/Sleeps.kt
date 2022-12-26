package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Sleeps : Table("sleeps"){
    val id = integer("id").autoIncrement().primaryKey()
    val duration = double("duration")
    val phase = varchar("phase", 25)
    val day = varchar("day", 20)
    val starttime = datetime("starttime")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}