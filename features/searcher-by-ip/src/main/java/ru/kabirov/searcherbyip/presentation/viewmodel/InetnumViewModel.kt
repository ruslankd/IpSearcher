package ru.kabirov.searcherbyip.presentation.viewmodel

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
class InetnumViewModel @Inject constructor(
    private val ripeApi: RipeApi
) : ViewModel() {
    var inetnum by mutableStateOf("")
    var org by mutableStateOf("")

    fun init(ipAddress: String) {
        viewModelScope.launch {
            val attributes = ripeApi.baseDtoByIp(ipAddress = ipAddress).objects?.obj?.first()?.attributes?.attribute ?: emptyList()
            inetnum = attributes.find { it.name == "inetnum" }?.value ?: ""
            org = attributes.find { it.name == "descr" }?.value ?: ""
        }
    }
}