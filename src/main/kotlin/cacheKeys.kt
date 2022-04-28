sealed interface Key

object AllGenresKey : Key

data class BookKey(private val book_id: Int) : Key

object AllPubKey : Key

data class StaffKey(private val staff_id: Int) : Key

data class ReadersKey(private val reader_id: Int) : Key
