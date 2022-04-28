import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.time
import org.postgresql.util.PGobject

class PGEnum<T : Enum<T>>(enumTypeName: String, enumValue: T?) : PGobject() {
    init {
        value = enumValue?.name
        type = enumTypeName
    }
}

enum class WORK_DAYS { ПН, ВТ, СР, ЧТ, ПТ }

object staff : Table("staff") {
    val staff_id = integer("staff_id").autoIncrement().uniqueIndex()
    val name = varchar("name", length = 100)
    val surname = varchar("surname", length = 100)
    val schedule = customEnumeration(
        "schedule", "work_days",
        { value -> WORK_DAYS.valueOf(value as String) },
        { PGEnum("work_days", it) })
    val start_of_shift = time("start_of_shift")
    val end_of_shift = time("end_of_shift")
    val phone_number = varchar("phone_number", length = 12)

    override val primaryKey = PrimaryKey(staff_id)
}

object author : Table("author") {
    val author_id = integer("author_id").autoIncrement().uniqueIndex()
    val name = varchar("name", length = 100)
    val surname = varchar("surname", length = 100)
    val patronymic = varchar("patronymic", length = 100)
    val year_of_birth = integer("year_of_birth")
    val year_of_death = integer("year_of_death")
    val country = varchar("country", length = 100)

    override val primaryKey = PrimaryKey(author_id)
}

object publisher : Table("publisher") {
    val publisher_id = integer("publisher_id").autoIncrement().uniqueIndex()
    val publisher_name = varchar("publisher_name", length = 100)

    override val primaryKey = PrimaryKey(publisher_id)
}

object genre : Table("genre") {
    val genre_id = integer("genre_id").autoIncrement().uniqueIndex()
    val genre_name = varchar("genre_name", length = 100)
    val description = text("description")

    override val primaryKey = PrimaryKey(genre_id)
}

object book_info : Table("book_info") {
    val book_info_id = integer("book_info_id").autoIncrement().uniqueIndex()
    val year_of_writing = integer("year_of_writing")
    val description = text("description")

    override val primaryKey = PrimaryKey(book_info_id)
}

object list_of_authors : Table("list_of_authors") {
    val book_info_id = integer("book_info_id") references book_info.book_info_id
    val author_id = integer("author_id") references author.author_id

    init {
        index(true, book_info_id, author_id)
    }
}

object list_of_publishers : Table("list_of_publishers") {
    val book_info_id = integer("book_info_id").references(book_info.book_info_id, onDelete = ReferenceOption.CASCADE)
    val publisher_id = integer("publisher_id").references(publisher.publisher_id, onDelete = ReferenceOption.RESTRICT)

    init {
        index(true, book_info_id, publisher_id)
    }

}

object list_of_genres : Table("list_of_genres") {
    val book_info_id = integer("book_info_id") references book_info.book_info_id
    val genre_id = integer("genre_id") references genre.genre_id

    init {
        index(true, book_info_id, genre_id)
    }
}

object books : Table("books") {
    val book_id = integer("book_id").autoIncrement().uniqueIndex()
    val book_name = varchar("book_name", length = 100)
    val book_info_id = integer("book_info_id") references book_info.book_info_id
    val num_of_available = integer("num_of_available").check { it greaterEq 0 }
    val num_of_unavailable = integer("num_of_unavailable").check { it greaterEq 0 }

    override val primaryKey = PrimaryKey(book_id)
}

object readers : Table("readers") {
    val reader_id = integer("reader_id").autoIncrement().uniqueIndex()
    val name = varchar("name", length = 100)
    val surname = varchar("surname", length = 100)
    val reader_book_num = integer("reader_book_num").uniqueIndex()
    val phone_number = varchar("phone_number", length = 12)
    val book_id = integer("book_id") references books.book_id
    val staff_id = integer("staff_id") references staff.staff_id
    val date_take = date("date_take")
    val date_return = date("date_return")

    override val primaryKey = PrimaryKey(reader_id)
}