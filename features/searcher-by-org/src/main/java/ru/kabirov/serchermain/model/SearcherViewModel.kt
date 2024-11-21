package ru.kabirov.serchermain.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.kabirov.ripeapi.RipeApi
import javax.inject.Inject

@HiltViewModel
class SearcherViewModel @Inject internal constructor() : ViewModel() {
    var state by mutableStateOf<List<String>>(emptyList())

//    fun init(query: String) {
//        viewModelScope.launch {
//            state =
//                ripeApi.baseDtoByNameOrganisation(name = query).objects?.obj?.map {
//                    it.attributes?.attribute?.get(1)?.value ?: ""
//                } ?: emptyList()
//        }
//    }
}