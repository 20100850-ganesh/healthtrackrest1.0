package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.Medicine
import ie.setu.domain.repository.MedicineDAO
import ie.setu.domain.repository.UserDAO
import io.javalin.http.Context

object MedicinetrackerController {

    private val medicineDAO = MedicineDAO()
    private val userDao = UserDAO()

    fun getAllMedicines(ctx: Context) {
        //mapper handles the deserialization of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        ctx.json(mapper.writeValueAsString( medicineDAO.getAll() ))
    }

    fun getMedicinesByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val medicines = medicineDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (medicines.isNotEmpty()) {
                //mapper handles the deserialization of Joda date into a String.
                val mapper = jacksonObjectMapper()
                    .registerModule(JodaModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                ctx.json(mapper.writeValueAsString(medicines))
            }
        }
    }

    fun addMedicine(ctx: Context) {
        //mapper handles the serialisation of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val medicine = mapper.readValue<Medicine>(ctx.body())
        medicineDAO.save(medicine)
        ctx.json(medicine)
    }

    fun getMedicinesByMedicineId(ctx: Context) {
        val medicine = medicineDAO.findByMedicineId((ctx.pathParam("medicine-id").toInt()))
        if (medicine != null){
            val mapper = jacksonObjectMapper()
                .registerModule(JodaModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            ctx.json(mapper.writeValueAsString(medicine))
        }
    }

    fun deleteMedicineByMedicineId(ctx: Context){
        medicineDAO.deleteByMedicineId(ctx.pathParam("medicine-id").toInt())
    }

    fun deleteMedicineByUserId(ctx: Context){
        medicineDAO.deleteByUserId(ctx.pathParam("user-id").toInt())
    }

    fun updateMedicine(ctx: Context){
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val medicine = mapper.readValue<Medicine>(ctx.body())
        medicineDAO.updateByMedicineId(
            medicineId = ctx.pathParam("medicine-id").toInt(),
            medicineDTO=medicine)
    }

}