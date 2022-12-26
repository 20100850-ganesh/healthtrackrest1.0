package ie.setu.utils

import ie.setu.domain.*
import ie.setu.domain.db.*
import org.jetbrains.exposed.sql.ResultRow

fun mapToUser(it: ResultRow) = User(
    id = it[Users.id],
    name = it[Users.name],
    email = it[Users.email]
)

fun mapToActivity(it: ResultRow) = Activity(
    id = it[Activities.id],
    description = it[Activities.description],
    duration = it[Activities.duration],
    started = it[Activities.started],
    calories = it[Activities.calories],
    userId = it[Activities.userId]
)

fun mapToDiet(it: ResultRow) = Diet(
    id = it[Diets.id],
    description = it[Diets.description],
    food = it[Diets.food],
    calories = it[Diets.calories],
    intake = it[Diets.intake],
    userId = it[Diets.userId]
)

fun mapToSleep(it: ResultRow) = Sleep(
    id = it[Sleeps.id],
    duration = it[Sleeps.duration],
    phase = it[Sleeps.phase],
    day = it[Sleeps.day],
    starttime = it[Sleeps.starttime],
    userId = it[Sleeps.userId]
)

fun mapToMedicine(it: ResultRow) = Medicine(
    id = it[Medicines.id],
    description = it[Medicines.description],
    medicine = it[Medicines.medicine],
    day = it[Medicines.day],
    intake = it[Medicines.intake],
    userId = it[Medicines.userId]
)