package ie.setu.domain.repository

import ie.setu.domain.Sleep
import ie.setu.domain.db.Sleeps
import ie.setu.utils.mapToSleep
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class SleepDAO {

    fun getAll(): ArrayList<Sleep> {
        val sleepsList: ArrayList<Sleep> = arrayListOf()
        transaction {
            Sleeps.selectAll().map {
                sleepsList.add(mapToSleep(it)) }
        }
        return sleepsList
    }

    fun findBySleepId(id: Int): Sleep?{
        return transaction {
            Sleeps
                .select() { Sleeps.id eq id}
                .map{mapToSleep(it)}
                .firstOrNull()
        }
    }

    fun findByUserId(userId: Int): List<Sleep>{
        return transaction {
            Sleeps
                .select {Sleeps.userId eq userId}
                .map {mapToSleep(it)}
        }
    }

    fun save(sleep: Sleep){
        transaction {
            Sleeps.insert {
                it[duration] = sleep.duration
                it[phase] = sleep.phase
                it[day] = sleep.day
                it[starttime] = sleep.starttime
                it[userId] = sleep.userId
            }
        }
    }

    fun updateBySleepId(sleepId: Int, sleepDTO: Sleep){
        transaction {
            Sleeps.update ({
                Sleeps.id eq sleepId}) {
                it[duration] = sleepDTO.duration
                it[phase] = sleepDTO.phase
                it[day] = sleepDTO.day
                it[starttime] = sleepDTO.starttime
                it[userId] = sleepDTO.userId
            }
        }
    }

    fun deleteBySleepId (sleepId: Int): Int{
        return transaction{
            Sleeps.deleteWhere { Sleeps.id eq sleepId }
        }
    }

    fun deleteByUserId (userId: Int): Int{
        return transaction{
            Sleeps.deleteWhere {Sleeps.userId eq userId }
        }
    }
}