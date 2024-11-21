package ru.kabirov.iporganisationselector.model

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.kabirov.iporganisationselector.navigation.NavClass
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val ipAddressValidator: IpAddressValidator,
) : ViewModel() {
    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _navigator = MutableStateFlow<NavClass>(NavClass.Empty)
    val navigator = _navigator.asStateFlow()

    fun onQueryChange(query: String) {
        _query.value = query.trim()
    }

    fun onSearchClick() {
        if (ipAddressValidator.isValidIpAddress(_query.value)) {
            _navigator.value = NavClass.Subnet(_query.value.lowercase())
        } else {
            _navigator.value = NavClass.Organisation(_query.value.lowercase())
        }
    }
}