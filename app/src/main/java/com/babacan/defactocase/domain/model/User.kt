package com.babacan.defactocase.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "authentication")
data class User(
    @PrimaryKey(autoGenerate = true)
    val _id: Int = 0,
    val email: String,
    val password: String,
    val isLoggedIn: Boolean = false,
)
