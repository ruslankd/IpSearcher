package ru.kabirov.iporganisationselector.presentation.ui.searcher

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun Searcher(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    hasBackBtn: Boolean = false,
    onBackBtnClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    searchButtonContentDescription: String,
    backButtonContentDescription: String,
    placeholderText: String,
) {
    Row(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SearchBar(
            modifier = Modifier.weight(1f),
            value = query,
            onValueChange = onQueryChange,
            placeholderText = placeholderText,
            leadingIcon = if (hasBackBtn) {
                {
                    Icon(
                        modifier = Modifier.clickable { onBackBtnClick() },
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = backButtonContentDescription
                    )
                }
            } else null,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                onSearchClick()
            }),
        )

        SearchButton(
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .width(IntrinsicSize.Max),
            onClick = onSearchClick,
            searchButtonContentDescription = searchButtonContentDescription,
        )
    }
}