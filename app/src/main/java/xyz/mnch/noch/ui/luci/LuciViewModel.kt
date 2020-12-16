package xyz.mnch.noch.ui.luci

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LuciViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is luci Fragment"
    }
    val text: LiveData<String> = _text
}
