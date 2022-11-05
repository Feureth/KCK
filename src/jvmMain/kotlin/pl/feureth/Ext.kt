package pl.feureth

import java.text.SimpleDateFormat
import java.util.*

val defaultSdf = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())

fun Long.toPrettyDate(): String = defaultSdf.format(Date(this))

fun <T> MutableList<T>.insertOrReplace(item: T, predicate: (item: T) -> Boolean) {
    val existingItem = find { predicate.invoke(it) }
    if (existingItem == null) {
        add(item)
    } else {
        set(indexOf(existingItem), item)
    }
}