package pl.feureth.model

data class Book(
    val id: Long,
    val title: String,
    val author: String,
    val isWithdrawn: Boolean = false
)
