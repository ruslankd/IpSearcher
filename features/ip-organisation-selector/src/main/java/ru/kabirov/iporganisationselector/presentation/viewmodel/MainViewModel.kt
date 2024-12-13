package ru.kabirov.iporganisationselector.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kabirov.common.resource.ResourceManager
import ru.kabirov.iporganisationselector.navigation.NavClass
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val ipAddressValidator: IpAddressValidator,
    private val resourceManager: ResourceManager,
) : ViewModel() {
    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _navigator = MutableStateFlow<NavClass>(NavClass.Empty)
    val navigator = _navigator.asStateFlow()

    fun onQueryChange(query: String) {
        _query.value = query.trim()
    }

    fun onSearchClick() = viewModelScope.launch {
        _navigator.value = NavClass.Empty
        delay(500)
        if (ipAddressValidator.isValidIpAddress(_query.value)) {
            _navigator.value = NavClass.Subnet(_query.value.lowercase())
        } else {
            _navigator.value = NavClass.Organisation(_query.value.lowercase())
        }
    }

    fun getResourceString(resId: Int): String {
        return resourceManager.getString(resId)
    }
}