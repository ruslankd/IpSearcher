package ru.kabirov.searcherbyip.presentation.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import ru.kabirov.searcherbyip.R
import ru.kabirov.searcherbyip.presentation.viewmodel.SubnetViewModel
import ru.kabirov.searcherbyip.presentation.viewmodel.UiState
import ru.kabirov.uikit.CountryImage
import ru.kabirov.uikit.ErrorMessage
import ru.kabirov.uikit.ProgressIndicator

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
    val notAffiliatedMessage =
        viewModel.getResourceString(R.string.not_affiliated_with_any_organization)

    SubnetContent(
        modifier = modifier,
        state = state,
        notAffiliatedMessage = notAffiliatedMessage,
        onSubnetClick = onSubnetClick,
        imageLoader = viewModel.imageLoader,
    )
}

@Composable
fun SubnetContent(
    state: UiState,
    notAffiliatedMessage: String,
    onSubnetClick: (String) -> Unit,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        when (state) {
            is UiState.None -> Unit
            is UiState.Success -> SubnetInfo(
                state = state,
                notAffiliatedMessage = notAffiliatedMessage,
                onSubnetClick = onSubnetClick,
                imageLoader = imageLoader,
                modifier = modifier
            )

            is UiState.Error -> ErrorMessage(state.error.message ?: "", modifier)
            is UiState.Loading -> ProgressIndicator(modifier)
        }
    }
}

@Composable
fun SubnetInfo(
    state: UiState.Success,
    notAffiliatedMessage: String,
    onSubnetClick: (String) -> Unit,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val subnet = state.stateData.subnet
    val flagUri = state.stateData.flagUri

    Box(modifier = modifier.padding(16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if (subnet.orgId == null) {
                        Toast
                            .makeText(
                                context,
                                notAffiliatedMessage,
                                Toast.LENGTH_LONG
                            )
                            .show()
                    } else {
                        onSubnetClick(subnet.orgId!!)
                    }
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = subnet.subnet)
                Text(text = subnet.subnetName)
            }
            flagUri?.let {
                Spacer(modifier = Modifier.width(2.dp))
                CountryImage(
                    modifier = Modifier.size(28.dp),
                    uri = it,
                    imageLoader = imageLoader,
                )
            }
        }
    }
}