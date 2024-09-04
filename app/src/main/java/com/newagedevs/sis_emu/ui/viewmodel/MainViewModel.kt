package com.newagedevs.sis_emu.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
) : ViewModel() {
    private val _toast = MutableLiveData<String>()

    val toast: LiveData<String> = _toast

    fun setToast(msg: String) {
        _toast.value = msg
    }

}
