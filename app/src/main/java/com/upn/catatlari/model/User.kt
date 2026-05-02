package com.upn.catatlari.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Int,
    val nama: String, // Ini wadah baru untuk nama
    val email: String,
    val password: String
) : Parcelable