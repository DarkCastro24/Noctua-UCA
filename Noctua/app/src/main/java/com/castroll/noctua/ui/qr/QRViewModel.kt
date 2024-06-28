package com.example.frontend_noctua_uca.ui.qr

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class QRViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "00117322"
    }
    val text: LiveData<String> = _text
}
