package pl.feureth.model

data class Borrow(
    val id: Long,
    val reader: Reader,
    val book: Book,
    val startTime: Long = System.currentTimeMillis(),
    val endTime: Long? = null
)
