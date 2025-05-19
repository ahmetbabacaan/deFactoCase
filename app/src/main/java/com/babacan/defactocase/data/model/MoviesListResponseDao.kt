package com.babacan.defactocase.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


data class MoviesListResponseDao(
    @SerializedName("Response") val success: Boolean?,
    @SerializedName("Search") val movieResponses: List<MovieResponse>?,
    @SerializedName("totalResults") val totalResults: Int?
)

@Parcelize
data class MovieResponse(
    @SerializedName("imdbID") val imdbID: String? = null,
    @SerializedName("Poster") val poster: String? = null,
    @SerializedName("Title") val title: String? = null,
    @SerializedName("Year") val year: String? = null,
    @SerializedName("Type") val type: String? = null
) : Parcelable