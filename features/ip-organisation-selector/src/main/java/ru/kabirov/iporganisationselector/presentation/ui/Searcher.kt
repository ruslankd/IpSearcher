package ru.kabirov.iporganisationselector.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import ru.kabirov.iporganisationselector.R

@Composable
fun Searcher(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    hasBackBtn: Boolean = false,
    onBackBtnClick: () -> Unit = {},
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
        leadingIcon = if (hasBackBtn) {
            {
                Icon(
                    modifier = Modifier.clickable { onBackBtnClick() },
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = stringResource(
                        R.string.back_button
                    )
                )
            }
        } else null,
        singleLine = true,
        label = {
            Text(text = "Name or IP address of the organization")
        },
    )
}