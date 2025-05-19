package com.babacan.defactocase.domain.model


data class MovieDetails(
    val actors: String? = null,
    val awards: String? = null,
    val boxOffice: String? = null,
    val country: String? = null,
    val dVD: String? = null,
    val director: String? = null,
    val genre: String? = null,
    val imdbID: String? = null,
    val imdbRating: String? = null,
    val imdbVotes: String? = null,
    val language: String? = null,
    val metascore: String? = null,
    val plot: String? = null,
    val poster: String? = null,
    val production: String? = null,
    val rated: String? = null,
    val ratings: List<Rating>? = null,
    val released: String? = null,
    val response: String? = null,
    val runtime: String? = null,
    val title: String? = null,
    val type: String? = null,
    val website: String? = null,
    val writer: String? = null,
    val year: String? = null
)

data class Rating(
   val source: String?,
   val value: String?
)