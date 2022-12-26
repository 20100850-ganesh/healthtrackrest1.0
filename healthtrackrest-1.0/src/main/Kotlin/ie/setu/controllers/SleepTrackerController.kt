package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.Sleep
import ie.setu.domain.repository.SleepDAO
import ie.setu.domain.repository.UserDAO
import io.javalin.http.Context

object SleepTrackerController {

    private val sleepDAO = SleepDAO()
    private val userDao = UserDAO()

    fun getAllSleeps(ctx: Context) {
        //mapper handles the deserialization of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        ctx.json(mapper.writeValueAsString( sleepDAO.getAll() ))
    }

    fun getSleepsByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val sleeps = sleepDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (sleeps.isNotEmpty()) {
                //mapper handles the deserialization of Joda date into a String.
                val mapper = jacksonObjectMapper()
                    .registerModule(JodaModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                ctx.json(mapper.writeValueAsString(sleeps))
            }
        }
    }

    fun addSleep(ctx: Context) {
        //mapper handles the serialisation of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val sleep = mapper.readValue<Sleep>(ctx.body())
        sleepDAO.save(sleep)
        ctx.json(sleep)
    }

    fun getSleepsBySleepId(ctx: Context) {
        val sleep = sleepDAO.findBySleepId((ctx.pathParam("sleep-id").toInt()))
        if (sleep != null){
            val mapper = jacksonObjectMapper()
                .registerModule(JodaModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            ctx.json(mapper.writeValueAsString(sleep))
        }
    }

    fun deleteSleepBySleepId(ctx: Context){
        sleepDAO.deleteBySleepId(ctx.pathParam("sleep-id").toInt())
    }

    fun deleteSleepByUserId(ctx: Context){
        sleepDAO.deleteByUserId(ctx.pathParam("user-id").toInt())
    }

    fun updateSleep(ctx: Context){
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val sleep = mapper.readValue<Sleep>(ctx.body())
        sleepDAO.updateBySleepId(
            sleepId = ctx.pathParam("sleep-id").toInt(),
            sleepDTO=sleep)
    }

}