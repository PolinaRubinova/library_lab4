package Interaction

import AllGenresKey
import AllPubKey
import BookKey
import DatabaseInteraction
import Key
import LruBasedCache
import ReadersKey
import StaffKey
import org.jetbrains.exposed.sql.ResultRow

object CachingInteraction : DatabaseInteraction {

    private val cache = LruBasedCache<Key, List<ResultRow>>(128)

    override fun getAllGenres() =
        cache.getOrPut(AllGenresKey) { NonCachingInteraction.getAllGenres() }

    override fun getBook(book_id: Int) =
        cache.getOrPut(BookKey(book_id)) { NonCachingInteraction.getBook(book_id) }

    override fun addAuthor(name: String, surname: String, patronymic: String,
                           year_of_birth: Int, year_of_death: Int, country: String) {
        NonCachingInteraction.addAuthor(name, surname, patronymic, year_of_birth, year_of_death, country)
    }

    override fun addBook(book_name: String, book_info_id: Int,
                         num_of_available: Int, num_of_unavailable: Int) {
        NonCachingInteraction.addBook(book_name, book_info_id, num_of_available, num_of_unavailable)
    }

    override fun changeStaffPhoneNumber(staff_id: Int, new_phone_number: String) {
        cache.remove(StaffKey(staff_id))
        NonCachingInteraction.changeStaffPhoneNumber(staff_id, new_phone_number)
    }

    override fun changeReaderDateReturn(reader_id: Int) {
        cache.remove(ReadersKey(reader_id))
        NonCachingInteraction.changeReaderDateReturn(reader_id)
    }

    override fun deletePublisher(pub_id: Int) {
        cache.remove(AllPubKey)
        NonCachingInteraction.deletePublisher(pub_id)
    }
}