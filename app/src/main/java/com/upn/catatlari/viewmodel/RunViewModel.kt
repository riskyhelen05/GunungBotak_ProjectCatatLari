package com.upn.catatlari.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.upn.catatlari.data.local.database.AppDatabase
import com.upn.catatlari.data.local.entity.RunEntity
import kotlinx.coroutines.launch

class RunViewModel(application: Application) : AndroidViewModel(application) {

    private val runDao = AppDatabase.getDatabase(application).runDao()

    val runHistory: LiveData<List<RunEntity>> = runDao.getAllRuns()

    fun addRun(run: RunEntity) {
        viewModelScope.launch {
            runDao.insertRun(run)
        }
    }
}

    // READ

    // UPDATE

    // DELETE
