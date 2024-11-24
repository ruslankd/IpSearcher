package ru.kabirov.organisation_info.presentation.ui

import android.widget.Toast
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
import ru.kabirov.data.model.Organisation
import ru.kabirov.organisation_info.presentation.viewmodel.OrganisationInfoViewModel
import ru.kabirov.organisation_info.presentation.viewmodel.UiState
import ru.kabirov.uikit.CountryImage

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

    OrganisationInfoContent(modifier = modifier, state = state, imageLoader = viewModel.imageLoader)
}

@Composable
fun OrganisationInfoContent(
    state: UiState,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        when (state) {
            is UiState.None -> Unit
            is UiState.Success -> OrganisationInfo(
                state = state,
                modifier = modifier,
                imageLoader = imageLoader,
            )

            is UiState.Error -> ErrorMessage(state, modifier)
            is UiState.Loading -> ProgressIndicator(modifier)
        }
    }
}

@Composable
fun OrganisationInfo(
    state: UiState.Success,
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
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(items = subnets, key = {
                    it.subnet
                }) {
                    Text(text = it.subnet)
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
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = organisation.name)
        Spacer(modifier = Modifier.weight(1f))
        flagUri?.let {
            CountryImage(
                uri = it,
                imageLoader = imageLoader,
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