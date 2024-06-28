package com.castroll.noctua.ui.laboratories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.castroll.noctua.data.remote.model.Lab
import com.castroll.noctua.data.remote.repository.LabRepository
import kotlinx.coroutines.launch

class LaboratoriesViewModel : ViewModel() {
    private val _laboratories = MutableLiveData<List<Lab>>()
    val laboratories: LiveData<List<Lab>> = _laboratories

    init {
        fetchLaboratories()
    }

    private fun fetchLaboratories() {
        viewModelScope.launch {
            try {
                val fetchedLabs = LabRepository().fetchLabs()
                _laboratories.value = fetchedLabs
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}