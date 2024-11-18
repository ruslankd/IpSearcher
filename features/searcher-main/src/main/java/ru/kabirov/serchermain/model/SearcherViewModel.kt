package ru.kabirov.serchermain.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.kabirov.ripeapi.RipeApi
import ru.kabirov.ripeapi.models.BaseDto
import javax.inject.Inject

@HiltViewModel
class SearcherViewModel @Inject internal constructor(
    ripeApi: RipeApi
) : ViewModel() {
    var state by mutableStateOf<List<String>>(emptyList())

    init {
        viewModelScope.launch {
            state =
                ripeApi.baseDtoByIdOrganisation(idOrganisation = "ORG-JR8-RIPE").objects?.obj?.map {
                    it.primaryKey?.attribute?.first()?.value ?: ""
                } ?: emptyList()
        }
    }
}