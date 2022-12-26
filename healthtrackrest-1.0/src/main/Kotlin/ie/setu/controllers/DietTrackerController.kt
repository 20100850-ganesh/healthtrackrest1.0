package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.Diet
import ie.setu.domain.repository.DietDAO
import ie.setu.domain.repository.UserDAO
import io.javalin.http.Context

object DietTrackerController{

    private val dietDAO = DietDAO()
    private val userDao = UserDAO()

    fun getAllDiets(ctx: Context) {
        //mapper handles the deserialization of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        ctx.json(mapper.writeValueAsString( dietDAO.getAll() ))
    }

    fun getDietsByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val diets = dietDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (diets.isNotEmpty()) {
                //mapper handles the deserialization of Joda date into a String.
                val mapper = jacksonObjectMapper()
                    .registerModule(JodaModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                ctx.json(mapper.writeValueAsString(diets))
            }
        }
    }

    fun addDiet(ctx: Context) {
        //mapper handles the serialisation of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val diet = mapper.readValue<Diet>(ctx.body())
        dietDAO.save(diet)
        ctx.json(diet)
    }

    fun getDietsByDietId(ctx: Context) {
        val diet = dietDAO.findByDietId((ctx.pathParam("diet-id").toInt()))
        if (diet != null){
            val mapper = jacksonObjectMapper()
                .registerModule(JodaModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            ctx.json(mapper.writeValueAsString(diet))
        }
    }

    fun deleteDietByDietId(ctx: Context){
        dietDAO.deleteByDietId(ctx.pathParam("diet-id").toInt())
    }

    fun deleteDietByUserId(ctx: Context){
        dietDAO.deleteByUserId(ctx.pathParam("user-id").toInt())
    }

    fun updateDiet(ctx: Context){
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val diet = mapper.readValue<Diet>(ctx.body())
        dietDAO.updateByDietId(
            dietId = ctx.pathParam("diet-id").toInt(),
            dietDTO=diet)
    }

}