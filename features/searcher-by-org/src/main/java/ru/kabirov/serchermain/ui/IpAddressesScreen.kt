package ru.kabirov.serchermain.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import ru.kabirov.serchermain.model.SearcherViewModel

@Composable
fun IpAddressesScreen(
    query: String,
    viewModel: SearcherViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
//    LaunchedEffect(Unit) {
//        viewModel.init(query)
//    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(viewModel.state) {
            Text(text = it)
        }
    }
}