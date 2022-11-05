package pl.feureth.view.desktop

sealed class Screen(val data: String? = null) {

    object Start : Screen()

    object BookList : Screen()
    class BookDetails(val id: Long? = null) : Screen(data = id?.toString())
    class BookEditor(val id: Long? = null) : Screen(data = id?.toString())

    object ReaderList : Screen()
    class ReaderDetails(val id: Long? = null) : Screen(data = id?.toString())
    class ReaderEditor(val id: Long? = null) : Screen(data = id?.toString())

    object BorrowList : Screen()
    class BorrowDetails(val id: Long? = null) : Screen(data = id?.toString())
    class BorrowEditor(val id: Long? = null) : Screen(data = id?.toString())

}