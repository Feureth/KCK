package pl.feureth.view.desktop.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun <T> ListSelectorField(
    label: String,
    selected: T,
    selectedTitle: String?,
    list: List<T>,
    itemContent: @Composable (item: T, onClick: (T) -> Unit) -> Unit,
    itemFilter: (item: T, query: String) -> Boolean,
    onSelected: (T) -> Unit,
    error: String,
    modifier: Modifier = Modifier,
) {

    var selectorOpened by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val filteredList = list.filter { itemFilter.invoke(it, searchQuery) }

    Column(modifier = Modifier.then(modifier)) {
        Label(label)
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
            if (selected == null) {
                Text("Nie wybrano", style = MaterialTheme.typography.h5)
            } else {
                Text(selectedTitle ?: "???", style = MaterialTheme.typography.h5)
            }
            if (selectorOpened.not()) {
                TextButton("Wybierz", onClick = { selectorOpened = true })
            } else {
                TextButton("Anuluj", onClick = { selectorOpened = false })
            }
        }
        AnimatedVisibility(visible = selectorOpened) {
            val shape by remember { mutableStateOf(RoundedCornerShape(8.dp)) }
            Column(
                modifier = Modifier.padding(vertical = 8.dp)
                    .shadow(4.dp, shape)
                    .background(Color.White, shape)
                    .border(1.dp, Color.LightGray, shape)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Wyszukaj...") },
                    modifier = Modifier.fillMaxWidth()
                )
                Column {
                    filteredList.take(3).forEach {
                        itemContent.invoke(it) { item ->
                            onSelected.invoke(item)
                            selectorOpened = false
                        }
                    }
                }
            }
        }
        if (error.isNotEmpty()) {
            Text(error, style = MaterialTheme.typography.caption, color = Color.Red)
        }
    }
}