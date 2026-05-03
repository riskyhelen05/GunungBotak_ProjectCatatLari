package com.upn.catatlari.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.upn.catatlari.data.local.entity.UserEntity

@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun login(username: String, password: String): UserEntity?

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserEntity>

    @Query("SELECT * FROM users ORDER BY id DESC LIMIT 1")
    suspend fun getLastUser(): UserEntity?

    @androidx.room.Update
    suspend fun updateUser(user: UserEntity)
}