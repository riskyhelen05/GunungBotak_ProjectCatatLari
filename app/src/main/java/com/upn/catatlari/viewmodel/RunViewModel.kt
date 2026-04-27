package com.upn.catatlari.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.upn.catatlari.model.Run

class RunViewModel : ViewModel() {

    val runList = listOf(
        Run(runDate = "22 Mei 2026", runDistance = 1, runDuration = 3),
        Run(runDate = "23 Mei 2026", runDistance = 1, runDuration = 3),
        Run(runDate = "24 Mei 2026", runDistance = 1, runDuration = 3)
    )

    private val runListLiveData = MutableLiveData(runList) // variabel yang berfungsi mengatur perubahan data
    var runHistory : LiveData<List<Run>> = runListLiveData // variabel yang dipakai untuk dipanggil class lain

    // CREATE
    fun addRun(run: Run) {
//        if (runListLiveData.value == null) {
//
//        }

        val currentList = runListLiveData.value.orEmpty().toMutableList()
        currentList.add(run)
        runListLiveData.value = currentList
    }

    // READ

    // UPDATE

    // DELETE

}