import Interaction.DatabaseInteraction
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.concurrent.CyclicBarrier

private fun connectToDatabase() {
    fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.postgresql.Driver"
        config.jdbcUrl = "jdbc:postgresql://localhost:5432/library"
        config.username = "postgres"
        config.password = "2630"
        config.addDataSourceProperty("cachePrepStmts", "false")
        config.validate()
        return HikariDataSource(config)
    }
    Database.connect(hikari())
}

private val cyclicBarrier = CyclicBarrier(7)

abstract class TestThread(val iterations: Int, val interaction: DatabaseInteraction) : Thread() {
    val times = mutableListOf<Long>()
}

class GetAllGenresThread(iterations: Int, interaction: DatabaseInteraction) : TestThread(iterations, interaction) {
    override fun run() {
        connectToDatabase()
        cyclicBarrier.await()
        while (cyclicBarrier.numberWaiting < 5) {
            val start = System.nanoTime()
            interaction.getAllGenres()
            times.add(System.nanoTime() - start)
        }
        cyclicBarrier.await()
    }
}

class GetBookThread(iterations: Int, interaction: DatabaseInteraction) : TestThread(iterations, interaction) {
    override fun run() {
        connectToDatabase()
        cyclicBarrier.await()
        val b_id = transaction { books.selectAll().map { it[books.book_id] } }
        while (cyclicBarrier.numberWaiting < 5) {
            val start = System.nanoTime()
            interaction.getBook(b_id.random())
            times.add(System.nanoTime() - start)
        }
        cyclicBarrier.await()
    }
}

class AddAuthorThread(iterations: Int, interaction: DatabaseInteraction) : TestThread(iterations, interaction) {
    override fun run() {
        connectToDatabase()
        cyclicBarrier.await()
        repeat(iterations) {
            val start = System.nanoTime()
            interaction.addAuthor("Иван", "Иванов", "Иванович",
                1900, 1990, "СССР")
            times.add(System.nanoTime() - start)
        }
        cyclicBarrier.await()
    }
}

class AddBookThread(iterations: Int, interaction: DatabaseInteraction) : TestThread(iterations, interaction) {
    override fun run() {
        connectToDatabase()
        cyclicBarrier.await()
        val b_i_id = transaction { book_info.selectAll().map { it[book_info.book_info_id] } }
        repeat(iterations) {
            val start = System.nanoTime()
            interaction.addBook("Название Книги", b_i_id.random(), 999, 999)
            times.add(System.nanoTime() - start)
        }
        cyclicBarrier.await()
    }
}

class ChangeStaffPhoneNumberThread(iterations: Int, interaction: DatabaseInteraction) : TestThread(iterations, interaction) {
    override fun run() {
        connectToDatabase()
        cyclicBarrier.await()
        val st_id = transaction { staff.selectAll().map { it[staff.staff_id] } }
        repeat(iterations) {
            val start = System.nanoTime()
            interaction.changeStaffPhoneNumber(st_id.random(), "+79216358488")
            times.add(System.nanoTime() - start)
        }
        cyclicBarrier.await()
    }
}

class ChangeReaderDateReturnThread(iterations: Int, interaction: DatabaseInteraction)
    : TestThread(iterations, interaction) {
    override fun run() {
        connectToDatabase()
        cyclicBarrier.await()
        val r_id = transaction { readers.selectAll().map { it[readers.reader_id] } }
        repeat(iterations) {
            val start = System.nanoTime()
            interaction.changeReaderDateReturn(r_id.random())
            times.add(System.nanoTime() - start)
        }
        cyclicBarrier.await()
    }
}

class DeletePublisherThread(iterations: Int, interaction: DatabaseInteraction) : TestThread(iterations, interaction) {
    override fun run() {
        connectToDatabase()
        cyclicBarrier.await()
        val pub_id = transaction { publisher.selectAll().map { it[publisher.publisher_id] } }
        repeat(iterations) {
            val start = System.nanoTime()
            interaction.deletePublisher(pub_id.random())
            times.add(System.nanoTime() - start)
        }
        cyclicBarrier.await()
    }
}