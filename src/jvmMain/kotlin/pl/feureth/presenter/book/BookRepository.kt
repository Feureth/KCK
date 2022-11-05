package pl.feureth.presenter.book

import pl.feureth.model.Book
import pl.feureth.model.Borrow
import pl.feureth.model.Reader

interface BookRepository {

    fun save(filepath: String)
    fun load(filepath: String)

    fun insert(book: Book)
    fun delete(book: Book)
    fun getBook(id: Long): Book?
    fun getBooks(query: String? = null, isWithdrawn: Boolean? = null, isBorrowed: Boolean? = null): List<Book>

    fun insert(reader: Reader)
    fun delete(reader: Reader)
    fun getReader(id: Long): Reader?
    fun getReaders(query: String? = null, isBlocked: Boolean? = null): List<Reader>

    fun insert(borrow: Borrow)
    fun delete(borrow: Borrow)
    fun getBorrow(id: Long): Borrow?
    fun getBorrows(reader: Reader? = null, book: Book? = null): List<Borrow>

}