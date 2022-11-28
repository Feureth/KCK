package pl.feureth.view.desktop.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.feureth.view.desktop.Constants

@Composable
fun VerticalLine(modifier: Modifier = Modifier) {
    Spacer(modifier = Modifier.width(1.dp).fillMaxHeight().background(Constants.Color.Spacer).then(modifier))
}

@Composable
fun HorizontalLine(modifier: Modifier = Modifier) {
    Spacer(modifier = Modifier.height(1.dp).fillMaxWidth().background(Constants.Color.Spacer).then(modifier))
}

@Composable
fun Title(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.h4,
        modifier = Modifier.then(modifier)
    )
}

@Composable
fun TextButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = Modifier.then(modifier)
    ) {
        Text(text)
    }
}

@Composable
fun CheckboxWithLabel(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled
        )
        Text(label, style = MaterialTheme.typography.caption)
    }
}

@Composable
fun Label(text: String) {
    Text(text, style = MaterialTheme.typography.caption)
}

@Composable
fun TextWithLabel(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = Modifier.then(modifier)) {
        Label(label)
        Text(value, style = MaterialTheme.typography.h5)
    }
}

@Composable
fun TextFieldWithLabel(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    error: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = Modifier.then(modifier)) {
        Label(label)
        TextField(
            value = value,
            onValueChange = onValueChange,
            isError = error.isNotEmpty(),
        )
        if (error.isNotEmpty()) {
            Text(error, style = MaterialTheme.typography.caption, color = MaterialTheme.colors.error)
        }
    }
}