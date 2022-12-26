package ie.setu.repository

import ie.setu.domain.Diet
import ie.setu.domain.db.Diets
import ie.setu.domain.repository.DietDAO
import ie.setu.helpers.diets
import ie.setu.helpers.populateDietTable
import ie.setu.helpers.populateUserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

private val diet1 = diets[0]
private val diet2 = diets[1]

class DietDAOTest {
    companion object {
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateDiets {

        @Test
        fun `multiple diets added to table can be retrieved successfully`() {
            transaction {
                val userDAO = populateUserTable()
                val dietDAO = populateDietTable()
                //Act & Assert
                assertEquals(2, dietDAO.getAll().size)
                assertEquals(diet1, dietDAO.findByDietId(diet1.id))
                assertEquals(diet2, dietDAO.findByDietId(diet2.id))
            }
        }
    }

    @Nested
    inner class ReadDiets {

        @Test
        fun `getting all diets from a populated table returns all rows`() {
            transaction {
                val userDAO = populateUserTable()
                val dietDAO = populateDietTable()
                //Act & Assert
                assertEquals(2, dietDAO.getAll().size)
            }
        }

        @Test
        fun `get diet by user id that has no diets, results in no record returned`() {
            transaction {
                val userDAO = populateUserTable()
                val dietDAO = populateDietTable()
                //Act & Assert
                assertEquals(0, dietDAO.findByUserId(3).size)
            }
        }

        @Test
        fun `get diet by user id that exists, results in a correct diet(s) returned`() {
            transaction {
                val userDAO = populateUserTable()
                val dietDAO = populateDietTable()
                //Act & Assert
                assertEquals(diet1, dietDAO.findByUserId(1)[0])
                assertEquals(diet2, dietDAO.findByUserId(2)[0])
            }
        }

        @Test
        fun `get all diets over empty table returns none`() {
            transaction {

                SchemaUtils.create(Diets)
                val dietDAO = DietDAO()

                //Act & Assert
                assertEquals(0, dietDAO.getAll().size)
            }
        }

        @Test
        fun `get diet by diet id that has no records, results in no record returned`() {
            transaction {
                val userDAO = populateUserTable()
                val dietDAO = populateDietTable()
                //Act & Assert
                assertEquals(null, dietDAO.findByDietId(4))
            }
        }

        @Test
        fun `get diet by diet id that exists, results in a correct activity returned`() {
            transaction {
                val userDAO = populateUserTable()
                val dietDAO = populateDietTable()
                //Act & Assert
                assertEquals(diet1, dietDAO.findByDietId(1))
            }
        }
    }

    @Nested
    inner class UpdateDiets {

        @Test
        fun `updating existing diet in table results in successful update`() {
            transaction {

                val userDAO = populateUserTable()
                val dietDAO = populateDietTable()

                //Act & Assert
                val diet2updated = Diet(id = 2, description = "a veggie extra-large pizza", food = "veg pizza", calories = 321, intake = DateTime.now(), userId = 2)
                dietDAO.updateByDietId(diet2updated.id, diet2updated)
                assertEquals(diet2updated, dietDAO.findByDietId(2))
            }
        }

        @Test
        fun `updating non-existant diet in table results in no updates`() {
            transaction {

                val userDAO = populateUserTable()
                val dietDAO = populateDietTable()

                //Act & Assert
                val diet4updated = Diet(id = 4, description = "a veg large pizza", food = "veg pizza", calories = 241, intake = DateTime.now(), userId = 3)
                dietDAO.updateByDietId(4, diet4updated)
                assertEquals(null, dietDAO.findByDietId(4))
                assertEquals(2, dietDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class DeleteDiets {

        @Test
        fun `deleting a non-existant diet (by id) in table results in no deletion`() {
            transaction {

                val userDAO = populateUserTable()
                val dietDAO = populateDietTable()

                //Act & Assert
                assertEquals(2, dietDAO.getAll().size)
                dietDAO.deleteByDietId(4)
                assertEquals(2, dietDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing diet (by id) in table results in record being deleted`() {
            transaction {

                val userDAO = populateUserTable()
                val dietDAO = populateDietTable()

                //Act & Assert
                assertEquals(2, dietDAO.getAll().size)
                dietDAO.deleteByDietId(diet2.id)
                assertEquals(1, dietDAO.getAll().size)
            }
        }


        @Test
        fun `deleting diets when none exist for user id results in no deletion`() {
            transaction {

                val userDAO = populateUserTable()
                val dietDAO = populateDietTable()

                //Act & Assert
                assertEquals(2, dietDAO.getAll().size)
                dietDAO.deleteByUserId(3)
                assertEquals(2, dietDAO.getAll().size)
            }
        }

        @Test
        fun `deleting diets when 1 or more exist for user id results in deletion`() {
            transaction {

                val userDAO = populateUserTable()
                val dietDAO = populateDietTable()

                //Act & Assert
                assertEquals(2, dietDAO.getAll().size)
                dietDAO.deleteByUserId(1)
                assertEquals(1, dietDAO.getAll().size)
            }
        }
    }
}