package com.babacan.defactocase.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
@Entity(tableName = "favorites")
data class Movie(
    @PrimaryKey(autoGenerate = true)
    val _id: Int = 0,
    val imdbID: String,
    val poster: String?,
    val title: String?,
    val year: String?,
    val isFavorite: Boolean = false,
    val listName: String = "Favoriler",
) : Parcelable