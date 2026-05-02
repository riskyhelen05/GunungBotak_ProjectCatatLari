package com.upn.catatlari.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nama: String, // INI KOLOM BARUNYA
    val username: String,
    val password: String
)