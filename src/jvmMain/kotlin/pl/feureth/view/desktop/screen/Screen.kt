package pl.feureth.view.desktop.screen

sealed class Screen(val data: String? = null) {

    object Start : Screen()

    object BookList : Screen()
    class BookDetails(val id: Long? = null) : Screen(data = id?.toString())

    object ReaderList : Screen()
    class ReaderDetails(val id: Long? = null) : Screen(data = id?.toString())

    object BorrowList : Screen()
    class BorrowDetails(val id: Long? = null) : Screen(data = id?.toString())

}
