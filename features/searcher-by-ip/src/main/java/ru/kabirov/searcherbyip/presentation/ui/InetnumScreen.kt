package ru.kabirov.searcherbyip.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import ru.kabirov.searcherbyip.presentation.viewmodel.InetnumViewModel

@Composable
fun InetnumScreen(
    ipAddress: String,
    viewModel: InetnumViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.init(ipAddress)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = viewModel.inetnum)
        Text(text = viewModel.org)
    }
}