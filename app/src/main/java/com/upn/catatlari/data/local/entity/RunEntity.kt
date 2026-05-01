package com.upn.catatlari.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "runs")
data class RunEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val runDate: String,
    val runDistance: Int,
    val runDuration: Int,
    val userId: Int
)