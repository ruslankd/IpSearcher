package ru.kabirov.organisation_info.presentation.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import ru.kabirov.organisation_info.presentation.viewmodel.OrganisationInfoViewModel
import ru.kabirov.organisation_info.presentation.viewmodel.UiState

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

    OrganisationInfoContent(modifier = modifier, state = state)
}

@Composable
fun OrganisationInfoContent(
    state: UiState,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        when (state) {
            is UiState.None -> Unit
            is UiState.Success -> OrganisationInfo(state, modifier)
            is UiState.Error -> ErrorMessage(state, modifier)
            is UiState.Loading -> ProgressIndicator(modifier)
        }
    }
}

@Composable
fun OrganisationInfo(
    state: UiState.Success,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val organisation = state.organisation

    Box(modifier = modifier) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = organisation.name)
                Spacer(modifier = Modifier.weight(1f))
                organisation.country?.let {
                    Text(text = it)
                }
            }
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