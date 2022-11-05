package pl.feureth.model

data class Reader(
    val id: Long,
    val name: String,
    val surname: String,
    val isBlocked: Boolean
) {
    val fullName: String
        get() = "$name $surname"
}
