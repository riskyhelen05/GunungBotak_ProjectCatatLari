package com.upn.catatlari.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.upn.catatlari.data.local.database.AppDatabase
import com.upn.catatlari.data.local.entity.RunEntity
import kotlinx.coroutines.launch

class RunViewModel(application: Application) : AndroidViewModel(application) {

    private val runDao = AppDatabase.getDatabase(application).runDao()

    fun getRunsByUser(userId: Int): LiveData<List<RunEntity>> {
        return runDao.getRunsByUser(userId)
    }

    fun addRun(run: RunEntity) {
        viewModelScope.launch {
            runDao.insertRun(run)
        }
    }

    fun deleteRun(run: RunEntity) {
        viewModelScope.launch {
            runDao.deleteRun(run)
        }
    }

    fun updateRun(run: RunEntity) {
        viewModelScope.launch {
            runDao.updateRun(run)
        }
    }
}