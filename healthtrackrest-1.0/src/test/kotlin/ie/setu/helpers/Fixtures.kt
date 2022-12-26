package ie.setu.helpers

import ie.setu.domain.*
import ie.setu.domain.db.*
import ie.setu.domain.repository.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.joda.time.DateTime

val nonExistingEmail = "112233445566778testUser@xxxxx.xx"
val validName = "Test User 1"
val validEmail = "testuser1@test.com"
val updatedName = "Updated Name"
val updatedEmail = "Updated Email"

val users = arrayListOf<User>(
    User(name = "Alice Wonderland", email = "alice@wonderland.com", id = 1),
    User(name = "Bob Cat", email = "bob@cat.ie", id = 2),
    User(name = "Mary Contrary", email = "mary@contrary.com", id = 3),
    User(name = "Carol Singer", email = "carol@singer.com", id = 4)
)

val activities = arrayListOf<Activity>(
    Activity(id = 1, description = "Running", duration = 22.0, calories = 230, started = DateTime.now(), userId = 1),
    Activity(id = 2, description = "Hopping", duration = 10.5, calories = 80, started = DateTime.now(), userId = 1),
    Activity(id = 3, description = "Walking", duration = 12.0, calories = 120, started = DateTime.now(), userId = 2)
)

val diets = arrayListOf<Diet>(
    Diet(id = 1, description = "a chicken burger", food = "chicken burger", calories = 230, intake = DateTime.now(), userId = 1),
    Diet(id = 2, description = "a veg large pizza", food = "veg pizza", calories = 241, intake = DateTime.now(), userId = 2)
)

val sleeps = arrayListOf<Sleep>(
    Sleep(id=1, duration = 8.0, phase = "night", day = "Monday", starttime = DateTime.now(), userId = 1),
    Sleep(id = 2, duration = 6.5, phase = "morning", day = "Wednesday", starttime = DateTime.now(), userId = 2)
)

val medicines = arrayListOf<Medicine>(
    Medicine(id = 1, description = "Fever Tablet", medicine = "Paracetemol", day = "Sunday", intake = DateTime.now(), userId = 1),
    Medicine(id = 2, description = "Gas relief Tablet", medicine = "PAN-D", day = "Thursday", intake = DateTime.now(), userId = 2)
)

fun populateUserTable(): UserDAO {
    SchemaUtils.create(Users)
    val userDAO = UserDAO()
    userDAO.save(users.get(0))
    userDAO.save(users.get(1))
    userDAO.save(users.get(2))
    return userDAO
}
fun populateActivityTable(): ActivityDAO {
    SchemaUtils.create(Activities)
    val activityDAO = ActivityDAO()
    activityDAO.save(activities.get(0))
    activityDAO.save(activities.get(1))
    activityDAO.save(activities.get(2))
    return activityDAO
}

fun populateDietTable(): DietDAO{
    SchemaUtils.create(Diets)
    val dietDAO = DietDAO()
    dietDAO.save(diets.get(0))
    dietDAO.save(diets.get(1))
    return dietDAO
}

fun populateSleepTable(): SleepDAO{
    SchemaUtils.create(Sleeps)
    val sleepDAO = SleepDAO()
    sleepDAO.save(sleeps.get(0))
    sleepDAO.save(sleeps.get(1))
    return sleepDAO
}

fun populateMedicineTable(): MedicineDAO{
    SchemaUtils.create(Medicines)
    val medicineDAO = MedicineDAO()
    medicineDAO.save(medicines.get(0))
    medicineDAO.save(medicines.get(1))
    return  medicineDAO
}
