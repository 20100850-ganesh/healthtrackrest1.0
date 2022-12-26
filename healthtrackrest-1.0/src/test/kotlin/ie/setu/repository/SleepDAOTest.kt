package ie.setu.repository

import ie.setu.domain.Sleep
import ie.setu.domain.db.Sleeps
import ie.setu.domain.repository.SleepDAO
import ie.setu.helpers.populateSleepTable
import ie.setu.helpers.populateUserTable
import ie.setu.helpers.sleeps
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

private val sleep1 = sleeps[0]
private val sleep2 = sleeps[1]

class SleepDAOTest {

    companion object {
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateSleeps {

        @Test
        fun `multiple sleeps added to table can be retrieved successfully`() {
            transaction {
                val userDAO = populateUserTable()
                val sleepDAO = populateSleepTable()
                //Act & Assert
                assertEquals(2, sleepDAO.getAll().size)
                assertEquals(sleep1, sleepDAO.findBySleepId(sleep1.id))
                assertEquals(sleep2, sleepDAO.findBySleepId(sleep2.id))
            }
        }
    }

    @Nested
    inner class ReadSleeps {

        @Test
        fun `getting all sleeps from a populated table returns all rows`() {
            transaction {
                val userDAO = populateUserTable()
                val sleepDAO = populateSleepTable()
                //Act & Assert
                assertEquals(2, sleepDAO.getAll().size)
            }
        }

        @Test
        fun `get sleep by user id that has no sleeps, results in no record returned`() {
            transaction {
                val userDAO = populateUserTable()
                val sleepDAO = populateSleepTable()
                //Act & Assert
                assertEquals(0, sleepDAO.findByUserId(3).size)
            }
        }

        @Test
        fun `get sleep by user id that exists, results in a correct sleep(s) returned`() {
            transaction {
                val userDAO = populateUserTable()
                val sleepDAO = populateSleepTable()
                //Act & Assert
                assertEquals(sleep1, sleepDAO.findByUserId(1)[0])
                assertEquals(sleep2, sleepDAO.findByUserId(2)[0])
            }
        }

        @Test
        fun `get all sleeps over empty table returns none`() {
            transaction {

                SchemaUtils.create(Sleeps)
                val sleepDAO = SleepDAO()

                //Act & Assert
                assertEquals(0, sleepDAO.getAll().size)
            }
        }

        @Test
        fun `get sleep by sleep id that has no records, results in no record returned`() {
            transaction {
                val userDAO = populateUserTable()
                val sleepDAO = populateSleepTable()
                //Act & Assert
                assertEquals(null, sleepDAO.findBySleepId(4))
            }
        }

        @Test
        fun `get sleep by sleep id that exists, results in a correct activity returned`() {
            transaction {
                val userDAO = populateUserTable()
                val sleepDAO = populateSleepTable()
                //Act & Assert
                assertEquals(sleep1, sleepDAO.findBySleepId(1))
            }
        }
    }

    @Nested
    inner class UpdateSleeps {

        @Test
        fun `updating existing sleep in table results in successful update`() {
            transaction {

                val userDAO = populateUserTable()
                val sleepDAO = populateSleepTable()

                //Act & Assert
                val sleep2updated = Sleep(id = 2, duration = 8.5, phase = "afternoon", day = "Wednesday", starttime = DateTime.now(), userId = 2)
                sleepDAO.updateBySleepId(sleep2updated.id, sleep2updated)
                assertEquals(sleep2updated, sleepDAO.findBySleepId(2))
            }
        }

        @Test
        fun `updating non-existant sleep in table results in no updates`() {
            transaction {

                val userDAO = populateUserTable()
                val sleepDAO = populateSleepTable()

                //Act & Assert
                val sleep4updated = Sleep(id = 4, duration = 7.5, phase = "morning", day = "Wednesday", starttime = DateTime.now(), userId = 2)
                sleepDAO.updateBySleepId(4, sleep4updated)
                assertEquals(null, sleepDAO.findBySleepId(4))
                assertEquals(2, sleepDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class DeleteSleeps {

        @Test
        fun `deleting a non-existant sleep (by id) in table results in no deletion`() {
            transaction {

                val userDAO = populateUserTable()
                val sleepDAO = populateSleepTable()

                //Act & Assert
                assertEquals(2, sleepDAO.getAll().size)
                sleepDAO.deleteBySleepId(4)
                assertEquals(2, sleepDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing sleep (by id) in table results in record being deleted`() {
            transaction {

                val userDAO = populateUserTable()
                val sleepDAO = populateSleepTable()

                //Act & Assert
                assertEquals(2, sleepDAO.getAll().size)
                sleepDAO.deleteBySleepId(sleep2.id)
                assertEquals(1, sleepDAO.getAll().size)
            }
        }


        @Test
        fun `deleting sleeps when none exist for user id results in no deletion`() {
            transaction {

                val userDAO = populateUserTable()
                val sleepDAO = populateSleepTable()

                //Act & Assert
                assertEquals(2, sleepDAO.getAll().size)
                sleepDAO.deleteByUserId(3)
                assertEquals(2, sleepDAO.getAll().size)
            }
        }

        @Test
        fun `deleting sleeps when 1 or more exist for user id results in deletion`() {
            transaction {

                val userDAO = populateUserTable()
                val sleepDAO = populateSleepTable()

                //Act & Assert
                assertEquals(2, sleepDAO.getAll().size)
                sleepDAO.deleteByUserId(1)
                assertEquals(1, sleepDAO.getAll().size)
            }
        }
    }

}