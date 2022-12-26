package ie.setu.repository

import ie.setu.domain.Medicine
import ie.setu.domain.db.Medicines
import ie.setu.domain.repository.MedicineDAO
import ie.setu.helpers.medicines
import ie.setu.helpers.populateMedicineTable
import ie.setu.helpers.populateUserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

private val medicine1 = medicines[0]
private val medicine2 = medicines[1]

class MedicineDAOTest {

    companion object {
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateMedicines {

        @Test
        fun `multiple medicines added to table can be retrieved successfully`() {
            transaction {
                val userDAO = populateUserTable()
                val medicineDAO = populateMedicineTable()
                //Act & Assert
                assertEquals(2, medicineDAO.getAll().size)
                assertEquals(medicine1, medicineDAO.findByMedicineId(medicine1.id))
                assertEquals(medicine2, medicineDAO.findByMedicineId(medicine2.id))
            }
        }
    }

    @Nested
    inner class ReadMedicines {

        @Test
        fun `getting all medicines from a populated table returns all rows`() {
            transaction {
                val userDAO = populateUserTable()
                val medicineDAO = populateMedicineTable()
                //Act & Assert
                assertEquals(2, medicineDAO.getAll().size)
            }
        }

        @Test
        fun `get medicine by user id that has no medicines, results in no record returned`() {
            transaction {
                val userDAO = populateUserTable()
                val medicineDAO = populateMedicineTable()
                //Act & Assert
                assertEquals(0, medicineDAO.findByUserId(3).size)
            }
        }

        @Test
        fun `get medicine by user id that exists, results in a correct medicine(s) returned`() {
            transaction {
                val userDAO = populateUserTable()
                val medicineDAO = populateMedicineTable()
                //Act & Assert
                assertEquals(medicine1, medicineDAO.findByUserId(1)[0])
                assertEquals(medicine2, medicineDAO.findByUserId(2)[0])
            }
        }

        @Test
        fun `get all medicines over empty table returns none`() {
            transaction {

                SchemaUtils.create(Medicines)
                val medicineDAO = MedicineDAO()

                //Act & Assert
                assertEquals(0, medicineDAO.getAll().size)
            }
        }

        @Test
        fun `get medicine by medicine id that has no records, results in no record returned`() {
            transaction {
                val userDAO = populateUserTable()
                val medicineDAO = populateMedicineTable()
                //Act & Assert
                assertEquals(null, medicineDAO.findByMedicineId(4))
            }
        }

        @Test
        fun `get medicine by medicine id that exists, results in a correct activity returned`() {
            transaction {
                val userDAO = populateUserTable()
                val medicineDAO = populateMedicineTable()
                //Act & Assert
                assertEquals(medicine1, medicineDAO.findByMedicineId(1))
            }
        }
    }

    @Nested
    inner class UpdateMedicines {

        @Test
        fun `updating existing medicine in table results in successful update`() {
            transaction {

                val userDAO = populateUserTable()
                val medicineDAO = populateMedicineTable()

                //Act & Assert
                val medicine2updated = Medicine(id = 2, description = "Gas and acidity relief Tablet", medicine = "PAN-D", day = "Friday", intake = DateTime.now(), userId = 2)
                medicineDAO.updateByMedicineId(medicine2updated.id, medicine2updated)
                assertEquals(medicine2updated, medicineDAO.findByMedicineId(2))
            }
        }

        @Test
        fun `updating non-existant medicine in table results in no updates`() {
            transaction {

                val userDAO = populateUserTable()
                val medicineDAO = populateMedicineTable()

                //Act & Assert
                val medicine4updated = Medicine(id = 4, description = "Gas relief Tablet", medicine = "PAN-D", day = "Thursday", intake = DateTime.now(), userId = 2)
                medicineDAO.updateByMedicineId(4, medicine4updated)
                assertEquals(null, medicineDAO.findByMedicineId(4))
                assertEquals(2, medicineDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class DeleteMedicines {

        @Test
        fun `deleting a non-existant medicine (by id) in table results in no deletion`() {
            transaction {

                val userDAO = populateUserTable()
                val medicineDAO = populateMedicineTable()

                //Act & Assert
                assertEquals(2, medicineDAO.getAll().size)
                medicineDAO.deleteByMedicineId(4)
                assertEquals(2, medicineDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing medicine (by id) in table results in record being deleted`() {
            transaction {

                val userDAO = populateUserTable()
                val medicineDAO = populateMedicineTable()

                //Act & Assert
                assertEquals(2, medicineDAO.getAll().size)
                medicineDAO.deleteByMedicineId(medicine2.id)
                assertEquals(1, medicineDAO.getAll().size)
            }
        }


        @Test
        fun `deleting medicines when none exist for user id results in no deletion`() {
            transaction {

                val userDAO = populateUserTable()
                val medicineDAO = populateMedicineTable()

                //Act & Assert
                assertEquals(2, medicineDAO.getAll().size)
                medicineDAO.deleteByUserId(3)
                assertEquals(2, medicineDAO.getAll().size)
            }
        }

        @Test
        fun `deleting medicines when 1 or more exist for user id results in deletion`() {
            transaction {

                val userDAO = populateUserTable()
                val medicineDAO = populateMedicineTable()

                //Act & Assert
                assertEquals(2, medicineDAO.getAll().size)
                medicineDAO.deleteByUserId(1)
                assertEquals(1, medicineDAO.getAll().size)
            }
        }
    }

}