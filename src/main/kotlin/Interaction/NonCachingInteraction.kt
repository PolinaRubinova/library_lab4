package Interaction

import DatabaseInteraction
import author
import books
import genre
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import publisher
import readers
import staff
import java.time.LocalDate

object NonCachingInteraction : DatabaseInteraction {

    override fun getAllGenres() = transaction { genre.selectAll().toList() }

    override fun getBook(book_id: Int) =
        transaction { books.select { books.book_id eq book_id }.toList() }

    override fun addAuthor(name: String, surname: String, patronymic: String,
                           year_of_birth: Int, year_of_death: Int, country: String) {
        transaction {
            author.insert {
                it[author.name] = name
                it[author.surname] = surname
                it[author.patronymic] = patronymic
                it[author.year_of_birth] = year_of_birth
                it[author.year_of_death] = year_of_death
                it[author.country] = country
            }
        }
    }

    override fun addBook(book_name: String, book_info_id: Int,
                         num_of_available: Int, num_of_unavailable: Int) {
        transaction {
            books.insert {
                it[books.book_name] = book_name
                it[books.book_info_id] = book_info_id
                it[books.num_of_available] = num_of_available
                it[books.num_of_unavailable] = num_of_unavailable
            }
        }
    }

    override fun changeStaffPhoneNumber(staff_id: Int, new_phone_number: String) {
        transaction {
            staff.update({ staff.staff_id eq staff_id }) {
                it[phone_number] = new_phone_number
            }
        }
    }

    override fun changeReaderDateReturn(reader_id: Int) {
        transaction {
            readers.update({ readers.reader_id eq reader_id }) {
                it[date_return] = LocalDate.of(2022, 12, 1)
            }
        }
    }

    override fun deletePublisher(pub_id: Int) {
        transaction {
            publisher.deleteWhere { publisher.publisher_id eq pub_id }
        }
    }
}