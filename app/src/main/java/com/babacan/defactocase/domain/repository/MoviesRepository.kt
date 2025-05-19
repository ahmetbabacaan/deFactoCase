package com.babacan.defactocase.domain.repository

import com.babacan.defactocase.domain.model.Movie
import com.babacan.defactocase.domain.model.MovieDetails


interface MoviesRepository {
    suspend fun searchForMovies(text: String, page: Int): Result<List<Movie>>
    suspend fun getMovieDetails(movieId: String): Result<MovieDetails>
}