package com.babacan.defactocase.data.repository

import com.babacan.defactocase.data.apiCall
import com.babacan.defactocase.data.mapToResult
import com.babacan.defactocase.data.mapper.MovieDetailsMapper
import com.babacan.defactocase.data.service.MoviesApiService
import com.babacan.defactocase.data.mapper.MovieMapper
import com.babacan.defactocase.domain.model.Movie
import com.babacan.defactocase.domain.model.MovieDetails
import com.babacan.defactocase.domain.repository.MoviesRepository
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val moviesApiService: MoviesApiService,
    private val searchMapper: MovieMapper,
    private val movieDetailMapper: MovieDetailsMapper
): MoviesRepository {

    override suspend fun searchForMovies(text: String, page: Int): Result<List<Movie>> {
        return apiCall {
            moviesApiService.searchMoviesFor(text, page)
        }.mapToResult().map {
            it.movieResponses?.map { searchMapper.map(it) } ?: emptyList()
        }
    }

    override suspend fun getMovieDetails(movieId: String): Result<MovieDetails> {
        return apiCall {
            moviesApiService.getMovieDetailsForImdbId(movieId)
        }.mapToResult().map {
            movieDetailMapper.map(it)
        }
    }
}