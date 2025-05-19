package com.babacan.defactocase.data.mapper

import com.babacan.defactocase.data.model.MovieResponse
import com.babacan.defactocase.domain.model.Movie
import javax.inject.Inject

class MovieMapper @Inject constructor() {
    fun map(movieResponse: MovieResponse) = Movie(
        imdbID = movieResponse.imdbID ?: throw IllegalArgumentException("imdbID is null"),
        poster = movieResponse.poster,
        title = movieResponse.title,
        year = movieResponse.year,
    )
}