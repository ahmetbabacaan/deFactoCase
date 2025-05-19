package com.babacan.defactocase.data.service

import com.babacan.defactocase.data.model.MovieDetailsResponse
import com.babacan.defactocase.data.model.MoviesListResponseDao
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApiService {

    @GET("./")
    suspend fun searchMoviesFor(
        @Query("s") s: String,
        @Query("page") page: Int
    ): Response<MoviesListResponseDao>

    @GET("./")
    suspend fun getMovieDetailsForImdbId(@Query("i") imdbId: String): Response<MovieDetailsResponse>
}