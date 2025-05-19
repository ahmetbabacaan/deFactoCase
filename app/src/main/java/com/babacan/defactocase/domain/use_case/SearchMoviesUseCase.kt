package com.babacan.defactocase.domain.use_case

import com.babacan.defactocase.domain.repository.MoviesRepository
import javax.inject.Inject

class SearchMoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository
) {

    suspend operator fun invoke(movieId: String, page: Int) =
        moviesRepository.searchForMovies(movieId, page)
}
