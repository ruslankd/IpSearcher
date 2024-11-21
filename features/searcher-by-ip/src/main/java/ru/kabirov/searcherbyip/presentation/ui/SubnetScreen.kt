package ru.kabirov.searcherbyip.presentation.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.kabirov.searcherbyip.R
import ru.kabirov.searcherbyip.presentation.UiState
import ru.kabirov.searcherbyip.presentation.viewmodel.SubnetViewModel

@Composable
fun SubnetScreen(
    ipAddress: String,
    viewModel: SubnetViewModel = hiltViewModel(
        creationCallback = { factory: SubnetViewModel.SubnetViewModelFactory ->
            factory.create(ipAddress)
        }
    ),
    onSubnetClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SubnetContent(modifier = modifier, state = state, onSubnetClick = onSubnetClick)

}

@Composable
fun SubnetContent(
    state: UiState,
    onSubnetClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        when (state) {
            is UiState.None -> Unit
            is UiState.Success -> SubnetInfo(state, onSubnetClick, modifier)
            is UiState.Error -> ErrorMessage(state, modifier)
            is UiState.Loading -> ProgressIndicator(state, modifier)
        }
    }
}

@Composable
fun SubnetInfo(
    state: UiState.Success,
    onSubnetClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val subnet = state.subnet

    Box(modifier = modifier) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (subnet.orgId == null) {
                    Toast
                        .makeText(
                            context,
                            context.getString(R.string.not_affiliated_with_any_organization),
                            Toast.LENGTH_LONG
                        )
                        .show()
                } else {
                    onSubnetClick(subnet.orgId!!)
                }
            }) {
            Text(text = subnet.subnet)
            Text(text = subnet.subnetName)
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
    state: UiState.Loading,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier = modifier
                .height(80.dp)
        )
    }
}