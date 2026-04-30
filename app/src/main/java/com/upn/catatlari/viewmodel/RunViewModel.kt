package com.upn.catatlari.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.upn.catatlari.model.Run

class RunViewModel : ViewModel() {

    private val runListLiveData = MutableLiveData<List<Run>>(emptyList())
    var runHistory: LiveData<List<Run>> = runListLiveData

    fun addRun(run: Run) {
        val currentList = runListLiveData.value.orEmpty().toMutableList()
        currentList.add(0, run)
        runListLiveData.value = currentList
    }
}

    // READ

    // UPDATE

    // DELETE
