import org.jetbrains.exposed.sql.ResultRow

interface DatabaseInteraction {

    fun getAllGenres() : List<ResultRow>

    fun getBook(book_id: Int) : List<ResultRow>

    fun addAuthor(name: String, surname: String, patronymic: String,
                  year_of_birth: Int, year_of_death: Int, country: String)

    fun addBook(book_name: String, book_info_id: Int,
                  num_of_available: Int, num_of_unavailable: Int)

    fun changeStaffPhoneNumber(staff_id: Int, new_phone_number: String)

    fun changeReaderDateReturn(reader_id: Int)

    fun deletePublisher(pub_id: Int)
}