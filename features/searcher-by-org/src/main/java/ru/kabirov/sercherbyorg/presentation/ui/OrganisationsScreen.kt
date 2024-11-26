package ru.kabirov.sercherbyorg.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.ImageLoader
import ru.kabirov.sercherbyorg.R
import ru.kabirov.sercherbyorg.presentation.viewmodel.OrganisationWithFlagUri
import ru.kabirov.sercherbyorg.presentation.viewmodel.OrganisationsViewModel
import ru.kabirov.sercherbyorg.presentation.viewmodel.UiState
import ru.kabirov.uikit.CountryImage
import ru.kabirov.uikit.ErrorMessage
import ru.kabirov.uikit.ProgressIndicator

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
    val emptyOrganisationLabel = viewModel.getResourceString(R.string.organizations_not_found)

    OrganisationsContent(
        modifier = modifier,
        state = state,
        emptyOrganisationLabel = emptyOrganisationLabel,
        imageLoader = viewModel.imageLoader,
        onOrganisationClick = onOrganisationClick
    )
}

@Composable
fun OrganisationsContent(
    state: UiState,
    emptyOrganisationLabel: String,
    onOrganisationClick: (String) -> Unit,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        when (state) {
            is UiState.None -> Unit
            is UiState.Success -> Organisations(
                state = state,
                emptyOrganisationLabel = emptyOrganisationLabel,
                onSubnetClick = onOrganisationClick,
                imageLoader = imageLoader,
                modifier = modifier
            )
            is UiState.Error -> ErrorMessage(state.error.message ?: "", modifier)
            is UiState.Loading -> ProgressIndicator(modifier)
        }
    }
}

@Composable
fun Organisations(
    state: UiState.Success,
    emptyOrganisationLabel: String,
    onSubnetClick: (String) -> Unit,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier,
) {
    val organisations = state.organisations

    Box(modifier = modifier) {
        if (organisations.isEmpty()) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = emptyOrganisationLabel,
                textAlign = TextAlign.Center,
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                itemsIndexed(items = organisations, key = { _, org ->
                    org.id
                }) { index, org ->
                    OrganisationItem(
                        org = org,
                        onSubnetClick = onSubnetClick,
                        imageLoader = imageLoader
                    )

                    if (index < organisations.lastIndex)
                        HorizontalDivider(
                            modifier = Modifier.padding(top = 4.dp),
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                }
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onSubnetClick(org.id)
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = org.name)
            Text(text = "ID: ${org.id}")
        }
        org.flagUri?.let { uri ->
            Spacer(modifier = Modifier.width(2.dp))
            CountryImage(
                modifier = Modifier.size(28.dp),
                uri = uri,
                imageLoader = imageLoader
            )
        }
    }
}