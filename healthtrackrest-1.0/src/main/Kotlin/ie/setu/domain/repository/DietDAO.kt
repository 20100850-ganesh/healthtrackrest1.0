package ie.setu.domain.repository

import ie.setu.domain.Diet
import ie.setu.domain.db.Diets
import ie.setu.utils.mapToDiet
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class DietDAO {

    fun getAll(): ArrayList<Diet> {
        val dietsList: ArrayList<Diet> = arrayListOf()
        transaction {
            Diets.selectAll().map {
                dietsList.add(mapToDiet(it)) }
        }
        return dietsList
    }

    fun findByDietId(id: Int): Diet?{
        return transaction {
            Diets
                .select() { Diets.id eq id}
                .map{mapToDiet(it)}
                .firstOrNull()
        }
    }

    fun findByUserId(userId: Int): List<Diet>{
        return transaction {
            Diets
                .select {Diets.userId eq userId}
                .map {mapToDiet(it)}
        }
    }

    fun save(diet: Diet){
        transaction {
            Diets.insert {
                it[description] = diet.description
                it[food] = diet.food
                it[intake] = diet.intake
                it[calories] = diet.calories
                it[userId] = diet.userId
            }
        }
    }

    fun updateByDietId(dietId: Int, dietDTO: Diet){
        transaction {
            Diets.update ({
                Diets.id eq dietId}) {
                it[description] = dietDTO.description
                it[food] = dietDTO.food
                it[intake] = dietDTO.intake
                it[calories] = dietDTO.calories
                it[userId] = dietDTO.userId
            }
        }
    }

    fun deleteByDietId (dietId: Int): Int{
        return transaction{
            Diets.deleteWhere { Diets.id eq dietId }
        }
    }

    fun deleteByUserId (userId: Int): Int{
        return transaction{
            Diets.deleteWhere { Diets.userId eq userId }
        }
    }
}