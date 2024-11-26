package ru.kabirov.organisation_info.presentation.ui

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.ImageLoader
import ru.kabirov.data.model.Organisation
import ru.kabirov.organisation_info.R
import ru.kabirov.organisation_info.presentation.viewmodel.OrganisationInfoViewModel
import ru.kabirov.organisation_info.presentation.viewmodel.UiState
import ru.kabirov.uikit.CountryImage
import ru.kabirov.uikit.ErrorMessage
import ru.kabirov.uikit.ProgressIndicator

@Composable
fun OrganisationInfoScreen(
    orgId: String,
    viewModel: OrganisationInfoViewModel = hiltViewModel(
        creationCallback = { factory: OrganisationInfoViewModel.OrganisationInfoViewModelFactory ->
            factory.create(orgId)
        }
    ),
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val subnetsLabel = viewModel.getResourceString(R.string.subnets)
    val emptySubnetsLabel =
        viewModel.getResourceString(R.string.the_organization_does_not_have_any_subnets)

    OrganisationInfoContent(
        modifier = modifier,
        state = state,
        imageLoader = viewModel.imageLoader,
        subnetsLabel = subnetsLabel,
        emptySubnetsLabel = emptySubnetsLabel,
    )
}

@Composable
fun OrganisationInfoContent(
    state: UiState,
    subnetsLabel: String,
    emptySubnetsLabel: String,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        when (state) {
            is UiState.None -> Unit
            is UiState.Success -> OrganisationInfo(
                state = state,
                subnetsLabel = subnetsLabel,
                emptySubnetsLabel = emptySubnetsLabel,
                modifier = modifier,
                imageLoader = imageLoader,
            )

            is UiState.Error -> ErrorMessage(state.error.message ?: "", modifier)
            is UiState.Loading -> ProgressIndicator(modifier)
        }
    }
}

@Composable
fun OrganisationInfo(
    state: UiState.Success,
    subnetsLabel: String,
    emptySubnetsLabel: String,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier,
) {
    val organisation = state.stateData.organisation
    val subnets = state.stateData.subnets
    val flagUri = state.stateData.flagUri

    Box(modifier = modifier) {
        Column(modifier = Modifier.fillMaxSize()) {
            OrganisationTitle(
                organisation = organisation,
                flagUri = flagUri,
                imageLoader = imageLoader,
            )
            if (subnets.isEmpty()) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    text = emptySubnetsLabel,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            } else {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp),
                    text = subnetsLabel,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    itemsIndexed(items = subnets, key = { _, subnet ->
                        subnet.subnet
                    }) { index, subnet ->
                        Column {
                            Text(text = subnet.subnet)
                            Text(text = subnet.subnetName)
                        }

                        if (index < subnets.lastIndex)
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
}

@Composable
private fun OrganisationTitle(
    organisation: Organisation,
    imageLoader: ImageLoader,
    flagUri: String?,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
            .background(
                brush =
                Brush.linearGradient(
                    0.0f to MaterialTheme.colorScheme.primary,
                    1.0f to
                            MaterialTheme.colorScheme.secondary
                                .copy(alpha = 0.6f)
                                .compositeOver(MaterialTheme.colorScheme.primary),
                ),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = organisation.name,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleLarge,
            )
            flagUri?.let {
                Spacer(modifier = Modifier.width(2.dp))
                CountryImage(
                    modifier = Modifier.size(32.dp),
                    uri = it,
                    imageLoader = imageLoader,
                )
            }

        }
    }
}