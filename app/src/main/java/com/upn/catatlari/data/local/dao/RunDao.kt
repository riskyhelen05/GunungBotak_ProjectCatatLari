package com.upn.catatlari.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.upn.catatlari.data.local.entity.RunEntity

@Dao
interface RunDao {

    @Query("SELECT * FROM runs WHERE userId = :userId")
    fun getRunsByUser(userId: Int): LiveData<List<RunEntity>>

    @Delete
    suspend fun deleteRun(run: RunEntity)

    @Update
    suspend fun updateRun(run: RunEntity)

    @Insert
    suspend fun insertRun(run: RunEntity)
}