package ie.setu.config

import ie.setu.controllers.DietTrackerController
import ie.setu.controllers.HealthTrackerController
import ie.setu.controllers.MedicinetrackerController
import ie.setu.controllers.SleepTrackerController
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.plugin.json.JavalinJackson
import io.javalin.plugin.rendering.vue.VueComponent

class JavalinConfig {

    fun startJavalinService(): Javalin {

        val app = Javalin.create {
            //it.registerPlugin(getConfiguredOpenApiPlugin())
            //it.defaultContentType = "application/json"
            //added this jsonMapper for our integration tests - serialise objects to json
            //it.jsonMapper(JavalinJackson(jsonObjectMapper()))
            it.enableWebjars()
        }.apply {
            exception(Exception::class.java) { e, _ -> e.printStackTrace() }
            error(404) { ctx -> ctx.json("404 - Not Found") }
        }.start(getAssignedPort())

        registerRoutes(app)
        return app
    }
    }

    private fun getAssignedPort(): Int {
        val herokuPort = System.getenv("PORT")
        return if (herokuPort != null) {
            Integer.parseInt(herokuPort)
        } else 7000
    }

    private fun registerRoutes(app: Javalin) {
        app.routes {

            path("/api/users") {
                get(HealthTrackerController::getAllUsers)
                post(HealthTrackerController::addUser)
                path("{user-id}"){
                    get(HealthTrackerController::getUserByUserId)
                    delete(HealthTrackerController::deleteUser)
                    patch(HealthTrackerController::updateUser)
                    path("activities"){
                        get(HealthTrackerController::getActivitiesByUserId)
                        delete(HealthTrackerController::deleteActivityByUserId)
                    }
                    path("diets"){
                        get(DietTrackerController::getDietsByUserId)
                        delete(DietTrackerController::deleteDietByUserId)
                    }
                    path("sleeps"){
                        get(SleepTrackerController::getSleepsByUserId)
                        delete(SleepTrackerController::deleteSleepByUserId)
                    }
                    path("medicines"){
                        get(MedicinetrackerController::getMedicinesByUserId)
                        delete(MedicinetrackerController::deleteMedicineByUserId)
                    }
                }
                path("/email/{email}"){
                    get(HealthTrackerController::getUserByEmail)
                }
            }
            path("/api/activities") {
                get(HealthTrackerController::getAllActivities)
                post(HealthTrackerController::addActivity)
                path("{activity-id}") {
                    get(HealthTrackerController::getActivitiesByActivityId)
                    delete(HealthTrackerController::deleteActivityByActivityId)
                    patch(HealthTrackerController::updateActivity)
                }
            }
            path("/api/diets"){
                get(DietTrackerController::getAllDiets)
                post(DietTrackerController::addDiet)
                path("{diet-id}"){
                    get(DietTrackerController::getDietsByDietId)
                    delete(DietTrackerController::deleteDietByDietId)
                    patch(DietTrackerController::updateDiet)
                }
            }
            path("/api/sleeps"){
                get(SleepTrackerController::getAllSleeps)
                post(SleepTrackerController::addSleep)
                path("{sleep-id}"){
                    get(SleepTrackerController::getSleepsBySleepId)
                    delete(SleepTrackerController::deleteSleepBySleepId)
                    patch(SleepTrackerController::updateSleep)
                }
            }
            path("/api/medicines"){
                get(MedicinetrackerController::getAllMedicines)
                post(MedicinetrackerController::addMedicine)
                path("{medicine-id}"){
                    get(MedicinetrackerController::getMedicinesByMedicineId)
                    delete(MedicinetrackerController::deleteMedicineByMedicineId)
                    patch(MedicinetrackerController::updateMedicine)
                }
            }
            // The @routeComponent that we added in layout.html earlier will be replaced
            // by the String inside of VueComponent. This means a call to / will load
            // the layout and display our <home-page> component.
            get("/", VueComponent("<home-page></home-page>"))
            get("/users", VueComponent("<user-overview></user-overview>"))
            get("/users/{user-id}", VueComponent("<user-profile></user-profile>"))
            get("/activities", VueComponent("<activity-overview></activity-overview>"))
            get("/activities/{activity-id}", VueComponent("<activity-profile></activity-profile>"))
            get("/diets", VueComponent("<diet-overview></diet-overview>"))
            get("/diets/{diet-id}", VueComponent("<diet-profile></diet-profile>"))
            get("/sleeps", VueComponent("<sleep-overview></sleep-overview>"))
            get("/sleeps/{sleep-id}", VueComponent("<sleep-profile></sleep-profile>"))
            get("/medicines", VueComponent("<medicine-overview></medicine-overview>"))
            get("/medicines/{medicine-id}", VueComponent("<medicine-profile></medicine-profile>"))

        }
    }
