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
import ru.kabirov.data.model.Organisation
import ru.kabirov.sercherbyorg.presentation.viewmodel.OrganisationsViewModel
import ru.kabirov.sercherbyorg.presentation.viewmodel.UiState

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
        onOrganisationClick = onOrganisationClick
    )
}

@Composable
fun OrganisationsContent(
    state: UiState,
    onOrganisationClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        when (state) {
            is UiState.None -> Unit
            is UiState.Success -> Organisations(state, onOrganisationClick, modifier)
            is UiState.Error -> ErrorMessage(state, modifier)
            is UiState.Loading -> ProgressIndicator(modifier)
        }
    }
}

@Composable
fun Organisations(
    state: UiState.Success,
    onSubnetClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val organisations = state.organisations

    Box(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = organisations, key = {
                it.id
            }) {
                OrganisationItem(org = it, onSubnetClick = onSubnetClick)
            }
        }
    }
}

@Composable
private fun OrganisationItem(
    org: Organisation,
    onSubnetClick: (String) -> Unit,
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
        org.country?.let { country ->
            Text(text = country)
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