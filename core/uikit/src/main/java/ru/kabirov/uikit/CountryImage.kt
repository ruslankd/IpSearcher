package ru.kabirov.uikit

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun CountryImage(
    uri: String,
    imageLoader: ImageLoader
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(uri)
            .crossfade(true)
            .build(),
        imageLoader = imageLoader,
        contentDescription = stringResource(R.string.flag),
    )
}