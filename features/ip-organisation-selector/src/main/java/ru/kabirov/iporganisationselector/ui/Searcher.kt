package ru.kabirov.iporganisationselector.ui

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.kabirov.iporganisationselector.R

@Composable
fun Searcher(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        modifier = modifier,
        value = query,
        onValueChange = onQueryChange,
        trailingIcon = {
            Icon(
                modifier = Modifier.clickable { onSearchClick() },
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search),
            )
        },
        singleLine = true,
        label = {
            Text(text = "Name or IP address of the organization")
        },
    )
}