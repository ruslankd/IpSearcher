package ru.kabirov.iporganisationselector.presentation.ui.searcher

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.compositeOver

@Composable
fun SearchButton(
    modifier: Modifier = Modifier,
    searchButtonContentDescription: String,
    onClick: () -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    shape: Shape = RoundedCornerShape(36),
) {
    Surface(
        modifier = modifier,
        onClick = onClick,
        shape = shape,
        color = containerColor,
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush =
                    Brush.linearGradient(
                        0.0f to MaterialTheme.colorScheme.primary,
                        1.0f to
                                MaterialTheme.colorScheme.secondary
                                    .copy(alpha = 0.6f)
                                    .compositeOver(MaterialTheme.colorScheme.primary),
                    ),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = searchButtonContentDescription,
            )
        }
    }
}