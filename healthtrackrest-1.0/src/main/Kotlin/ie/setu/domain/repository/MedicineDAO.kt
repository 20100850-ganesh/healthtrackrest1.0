package ie.setu.domain.repository

import ie.setu.domain.Medicine
import ie.setu.domain.db.Medicines
import ie.setu.utils.mapToMedicine
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class MedicineDAO {

    fun getAll(): ArrayList<Medicine> {
        val medicinesList: ArrayList<Medicine> = arrayListOf()
        transaction {
            Medicines.selectAll().map {
                medicinesList.add(mapToMedicine(it)) }
        }
        return medicinesList
    }

    fun findByMedicineId(id: Int): Medicine?{
        return transaction {
            Medicines
                .select() { Medicines.id eq id}
                .map{mapToMedicine(it)}
                .firstOrNull()
        }
    }

    fun findByUserId(userId: Int): List<Medicine>{
        return transaction {
            Medicines
                .select {Medicines.userId eq userId}
                .map {mapToMedicine(it)}
        }
    }

    fun save(medicine1: Medicine){
        transaction {
            Medicines.insert {
                it[description] = medicine1.description
                it[medicine] = medicine1.medicine
                it[intake] = medicine1.intake
                it[day] = medicine1.day
                it[userId] = medicine1.userId
            }
        }
    }

    fun updateByMedicineId(medicineId: Int, medicineDTO: Medicine){
        transaction {
            Medicines.update ({
                Medicines.id eq medicineId}) {
                it[description] = medicineDTO.description
                it[medicine] = medicineDTO.medicine
                it[intake] = medicineDTO.intake
                it[day] = medicineDTO.day
                it[userId] = medicineDTO.userId
            }
        }
    }

    fun deleteByMedicineId (medicineId: Int): Int{
        return transaction{
            Medicines.deleteWhere { Medicines.id eq medicineId }
        }
    }

    fun deleteByUserId (userId: Int): Int{
        return transaction{
            Medicines.deleteWhere { Medicines.userId eq userId }
        }
    }

}