package com.upn.catatlari.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.upn.catatlari.data.local.entity.RunEntity

@Dao
interface RunDao {

    @Insert
    suspend fun insertRun(run: RunEntity)

    @Query("SELECT * FROM runs ORDER BY id DESC")
    fun getAllRuns(): LiveData<List<RunEntity>>

}