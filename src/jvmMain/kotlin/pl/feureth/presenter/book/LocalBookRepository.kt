package pl.feureth.presenter.book

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import pl.feureth.insertOrReplace
import pl.feureth.model.Book
import pl.feureth.model.Borrow
import pl.feureth.model.Reader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import kotlin.random.Random

class LocalBookRepository : BookRepository {

    private val books = mutableListOf<Book>()
    private val readers = mutableListOf<Reader>()
    private val borrows = mutableListOf<Borrow>()

    private var isSavingEnabled = true

    init {
        load()
//        populate()
    }

    override fun save(filepath: String) {
        val file = File(filepath)
        if (file.exists().not()) file.createNewFile()
        val writer = FileWriter(file)

        val fileFormat = FileFormat(books, readers, borrows)
        val json = Gson().toJson(fileFormat)

        writer.write(json)
        writer.close()
    }

    private fun save() {
        if (isSavingEnabled) save(DEFAULT_FILE)
    }

    override fun load(filepath: String) {
        val fileFormat = try {
            val file = File(filepath)
            val reader = FileReader(file)
            val json = reader.readText()
            Gson().fromJson(json, object : TypeToken<FileFormat>() {}.type)
        } catch (e: Exception) {
            FileFormat(emptyList(), emptyList(), emptyList())
        }

        books.clear()
        books.addAll(fileFormat.books)
        readers.clear()
        readers.addAll(fileFormat.readers)
        borrows.clear()
        borrows.addAll(fileFormat.borrows)
    }

    private fun load() = load(DEFAULT_FILE)

    override fun insert(book: Book) {
        val _book = if (book.id == 0L) {
            val lastId = books.maxOfOrNull { it.id } ?: 0L
            book.copy(id = lastId + 1)
        } else book
        books.insertOrReplace(_book) { it.id == _book.id }
        borrows.filter { it.book.id == _book.id }.forEach {
            insert(it.copy(book = _book))
        }
        save()
    }

    override fun insert(reader: Reader) {
        val _reader = if (reader.id == 0L) {
            val lastId = readers.maxOfOrNull { it.id } ?: 0L
            reader.copy(id = lastId + 1)
        } else reader
        readers.insertOrReplace(_reader) { it.id == _reader.id }
        borrows.filter { it.reader.id == _reader.id }.forEach {
            insert(it.copy(reader = _reader))
        }
        save()
    }

    override fun insert(borrow: Borrow) {
        val _borrow = if (borrow.id == 0L) {
            val lastId = borrows.maxOfOrNull { it.id } ?: 0L
            borrow.copy(id = lastId + 1)
        } else borrow
        borrows.insertOrReplace(_borrow) { it.id == _borrow.id }
        save()
    }

    override fun delete(book: Book) {
        books.remove(book)
        save()
    }

    override fun delete(reader: Reader) {
        readers.remove(reader)
        save()
    }

    override fun delete(borrow: Borrow) {
        borrows.remove(borrow)
        save()
    }

    override fun getBook(id: Long): Book? = books.find { it.id == id }

    override fun getBooks(query: String?, isWithdrawn: Boolean?, isBorrowed: Boolean?): List<Book> =
        books.filter {
            query == null || it.id.toString().contains(query, ignoreCase = true) ||
                    it.title.contains(query, ignoreCase = true) || it.author.contains(query, ignoreCase = true)
        }
            .filter { isWithdrawn == null || it.isWithdrawn == isWithdrawn }
            .filter { book ->
                when (isBorrowed) {
                    null -> true
                    true -> borrows.any { it.endTime == null && it.book == book }
                    false -> borrows.filter { it.book == book }.all { it.endTime != null }
                }
            }

    override fun getReader(id: Long): Reader? = readers.find { it.id == id }

    override fun getReaders(query: String?, isBlocked: Boolean?): List<Reader> =
        readers.filter {
            query == null || it.id.toString().contains(query, ignoreCase = true) ||
                    it.name.contains(query, ignoreCase = true) || it.surname.contains(query, ignoreCase = true)
        }.filter { isBlocked == null || it.isBlocked == isBlocked }

    override fun getBorrow(id: Long): Borrow? = borrows.find { it.id == id }

    override fun getBorrows(reader: Reader?, book: Book?): List<Borrow> =
        borrows.filter { reader == null || it.reader == reader }
            .filter { book == null || it.book == book }

    private fun populate() {

        isSavingEnabled = false

        insert(Book(id = 0, title = "Bicz Boży", author = "Jacek Piekara"))
        insert(Book(id = 0, title = "Harry Potter: Więzień Azkabanu", author = "J. K. Rowling"))
        insert(Book(id = 0, title = "Psychologia dla bystrzaków", author = "George Cash"))
        insert(Book(id = 0, title = "Marsjanin", author = "Andy Weir"))
        insert(Book(id = 0, title = "Gildia Magów", author = "Trudi Canavan"))
        insert(Book(id = 0, title = "Shrek: Komiks", author = "Dreamworks"))
        insert(Book(id = 0, title = "Bezpieczeństwo w sieci", author = "Sekurak"))
        insert(Book(id = 0, title = "UX/UI w praktyce", author = "Jan Mariusz Nowak"))
        insert(Book(id = 0, title = "Podstawy DataScience", author = "Joe Don"))
        insert(Book(id = 0, title = "Szczeka, merda, mówi", author = "Marta Buniok-Badzio"))
        insert(Book(id = 0, title = "Trening siłowy", author = "Maria Curie"))
        insert(Book(id = 0, title = "Manga: Podręcznik rysowania", author = "Xin Kong Lee"))
        insert(
            Book(
                id = 0,
                title = "Programowanie gier przy użyciu środowiska Unity i języka C#",
                author = "Jack Johnson"
            )
        )

        insert(Reader(id = 0, name = "Jan", surname = "Kowalski", isBlocked = false))
        insert(Reader(id = 0, name = "Agnieszka", surname = "Książkowa", isBlocked = false))
        insert(Reader(id = 0, name = "Paweł", surname = "Nijaki", isBlocked = true))
        insert(Reader(id = 0, name = "Ryszard", surname = "Wiśniewski", isBlocked = false))
        insert(Reader(id = 0, name = "Dorota", surname = "Wasilak", isBlocked = false))
        insert(Reader(id = 0, name = "Grzegorz", surname = "Brzęczyszczykiewicz", isBlocked = false))
        insert(Reader(id = 0, name = "Michael", surname = "Scott", isBlocked = true))

        for (i in 1..20) insert(randomBorrow(endTime = true))
        for (i in 1..5) insert(randomBorrow(endTime = false))

        isSavingEnabled = true
        save()
    }

    private fun randomBorrow(endTime: Boolean): Borrow {
        val availableBooks = if (endTime.not())
            books.filter { book -> borrows.any { it.endTime == null && it.book.id == book.id }.not() } else books
        var borrow: Borrow
        do {
            borrow = Borrow(
                id = 0,
                reader = readers.random(),
                book = availableBooks.random(),
                startTime = System.currentTimeMillis() - (1000L * 60 * Random.nextInt(60 * 72, 60 * 144)),
                endTime = if (endTime)
                    System.currentTimeMillis() - (1000L * 60 * Random.nextInt(60, 60 * 48))
                else null
            )
        } while (getBorrows(reader = borrow.reader, book = borrow.book).isNotEmpty())
        return borrow
    }

    companion object {
        const val DEFAULT_FILE = "C:\\Users\\Admin\\Desktop\\database.library"
    }

    private data class FileFormat(
        val books: List<Book>,
        val readers: List<Reader>,
        val borrows: List<Borrow>,
    )

}