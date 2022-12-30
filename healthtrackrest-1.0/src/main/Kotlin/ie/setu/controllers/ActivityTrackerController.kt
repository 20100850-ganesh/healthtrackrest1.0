package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.Activity
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.UserDAO
import io.javalin.http.Context

object ActivityTrackerController {
    private val activityDAO = ActivityDAO()
    private val userDao = UserDAO()
    val mapper = jacksonObjectMapper()
        .registerModule(JodaModule())
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

    fun getAllActivities(ctx: Context) {
        val activities = activityDAO.getAll()
        if(activities.size != 0) {
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
        ctx.json(mapper.writeValueAsString(activities))
    }

    fun getActivitiesByUserId(ctx: Context) {
        val user = userDao.findById(ctx.pathParam("user-id").toInt())
        if ( user != null) {
            val activities = activityDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (activities.isNotEmpty()) {
                ctx.json(mapper.writeValueAsString(activities))
                ctx.status(200)
            }
            else{
                ctx.status(404)
            }
        }
    }

    fun getActivitiesByActivityId(ctx: Context) {
        val activity = activityDAO.findByActivityId((ctx.pathParam("activity-id").toInt()))
        if (activity != null){
            ctx.json(mapper.writeValueAsString(activity))
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

    fun addActivity(ctx: Context) {
        val activity = mapper.readValue<Activity>(ctx.body())
        val aid = activityDAO.save(activity)
        if(aid != null){
            activity.id = aid
            ctx.json(activity)
            ctx.status(201)
        }
    }


    fun deleteActivityByActivityId(ctx: Context){
        if(activityDAO.deleteByActivityId(ctx.pathParam("activity-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun deleteActivityByUserId(ctx: Context){
        if (activityDAO.deleteByUserId(ctx.pathParam("user-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun updateActivity(ctx: Context){
        val activity = mapper.readValue<Activity>(ctx.body())
        if((activityDAO.updateByActivityId(
                activityId = ctx.pathParam("activity-id").toInt(),
                activityDTO=activity)) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }
}