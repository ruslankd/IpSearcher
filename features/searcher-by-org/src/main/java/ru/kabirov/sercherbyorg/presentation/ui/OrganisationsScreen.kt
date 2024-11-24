package ru.kabirov.sercherbyorg.presentation.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.ImageLoader
import ru.kabirov.sercherbyorg.presentation.viewmodel.OrganisationWithFlagUri
import ru.kabirov.sercherbyorg.presentation.viewmodel.OrganisationsViewModel
import ru.kabirov.sercherbyorg.presentation.viewmodel.UiState
import ru.kabirov.uikit.CountryImage

@Composable
fun OrganisationsScreen(
    query: String,
    viewModel: OrganisationsViewModel = hiltViewModel(
        creationCallback = { factory: OrganisationsViewModel.OrganisationsViewModelFactory ->
            factory.create(query)
        }
    ),
    onOrganisationClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    OrganisationsContent(
        modifier = modifier,
        state = state,
        imageLoader = viewModel.imageLoader,
        onOrganisationClick = onOrganisationClick
    )
}

@Composable
fun OrganisationsContent(
    state: UiState,
    onOrganisationClick: (String) -> Unit,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        when (state) {
            is UiState.None -> Unit
            is UiState.Success -> Organisations(state, onOrganisationClick, imageLoader, modifier)
            is UiState.Error -> ErrorMessage(state, modifier)
            is UiState.Loading -> ProgressIndicator(modifier)
        }
    }
}

@Composable
fun Organisations(
    state: UiState.Success,
    onSubnetClick: (String) -> Unit,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier,
) {
    val organisations = state.organisations

    Box(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = organisations, key = {
                it.id
            }) {
                OrganisationItem(org = it, onSubnetClick = onSubnetClick, imageLoader = imageLoader)
            }
        }
    }
}

@Composable
private fun OrganisationItem(
    org: OrganisationWithFlagUri,
    onSubnetClick: (String) -> Unit,
    imageLoader: ImageLoader,
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            onSubnetClick(org.id)
        }) {
        Column {
            Text(text = org.name)
            Text(text = org.id)
        }
        Spacer(modifier = Modifier.weight(1f))
        org.flagUri?.let { uri ->
            CountryImage(
                uri = uri,
                imageLoader = imageLoader
            )
        }
    }
}

@Composable
private fun ErrorMessage(
    state: UiState.Error,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Toast.makeText(context, state.error.message, Toast.LENGTH_LONG).show()
}

@Composable
private fun ProgressIndicator(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier = modifier
                .height(80.dp)
        )
    }
}