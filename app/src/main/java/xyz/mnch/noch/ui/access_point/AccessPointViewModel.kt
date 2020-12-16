package xyz.mnch.noch.ui.access_point

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AccessPointViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is accessPoint Fragment"
    }
    val text: LiveData<String> = _text
}
