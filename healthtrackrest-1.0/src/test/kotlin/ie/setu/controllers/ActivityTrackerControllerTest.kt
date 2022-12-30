package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import ie.setu.config.DbConfig
import ie.setu.domain.Activity
import ie.setu.helpers.ServerContainer
import ie.setu.utils.jsonToObject
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ActivityTrackerControllerTest {
    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()
    val description = "A Test Desc"
    val duration = 1.1
    val calories = 1
    val started = DateTime("2022-09-10T05:59:27.258Z")
    val userId = 1



    val mapper = jacksonObjectMapper()
        .registerModule(JodaModule())
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

    private fun addActivity (description: String, duration: Double, calories:Int, started: DateTime, userId:Int): HttpResponse<JsonNode> {
        return Unirest.post(origin+ "/api/activities")
            .body("{\"description\":\"$description\", \"duration\":\"$duration\",\"calories\":\"$calories\", \"started\":\"$started\",\"userId\":\"$userId\"}").asJson()
    }

    private fun deleteActivityByActivityId (activityId: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/activities/${activityId}").asString()
    }

    private fun deleteActivityByUserId (userId: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/users/${userId}/activities").asString()
    }

    private fun retrieveActivityByActivityId(id : Int) : HttpResponse<String> {
        return Unirest.get(origin + "/api/activities/${id}").asString()
    }

    private fun retrieveActivityByUserId(id: Int) : HttpResponse<String> {
        return Unirest.get(origin + "/api/users/${id}/activities").asString()
    }

    private fun updateActivity (id: Int, description: String,duration: Double,calories:Int,started:DateTime,userId:Int): HttpResponse<JsonNode> {
        return Unirest.patch(origin + "/api/activities/${id}")
            .body("{\"description\":\"$description\", \"duration\":\"$duration\",\"calories\":\"$calories\", \"started\":\"$started\",\"userId\":\"$userId\"}").asJson()
    }

    @Nested
    inner class ReadActivities{
        @Test
        fun `get all activities from the database returns 200 or 404 response`() {
            val response = Unirest.get(origin + "/api/activities/").asString()
            if (response.status == 200) {
                val retrievedActivities: ArrayList<Activity> = jsonToObject(response.body.toString())
                assertNotEquals(0, retrievedActivities.size)
            }
            else {
                assertEquals(404, response.status)
            }
        }

        @Test
        fun `get activity by id when activity does not exist returns 404 response`() {

            //Arrange - test data for activity id
            val id = Integer.MIN_VALUE

            // Act - attempt to retrieve the non-existent activity from the database
            val retrieveResponse = Unirest.get(origin + "/api/activities/${id}").asString()

            // Assert -  verify return code
            assertEquals(404, retrieveResponse.status)
        }


    }

    @Nested
    inner class CreateActivities{

    }

    @Nested
    inner class UpdateActivities{

    }

    @Nested
    inner class DeleteActivities{

    }
}
