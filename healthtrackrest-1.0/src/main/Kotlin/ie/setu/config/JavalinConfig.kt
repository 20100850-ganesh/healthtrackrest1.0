package ie.setu.config

import ie.setu.controllers.DietTrackerController
import ie.setu.controllers.HealthTrackerController
import ie.setu.controllers.MedicinetrackerController
import ie.setu.controllers.SleepTrackerController
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*

class JavalinConfig {

    fun startJavalinService(): Javalin {

        val app = Javalin.create().apply {
            exception(Exception::class.java) { e, _ -> e.printStackTrace() }
            error(404) { ctx -> ctx.json("404 - Not Found") }
        }.start(getAssignedPort())

        registerRoutes(app)
        return app
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
        }
    }
}
