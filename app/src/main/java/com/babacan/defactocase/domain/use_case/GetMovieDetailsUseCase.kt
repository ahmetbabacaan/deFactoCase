package com.babacan.defactocase.domain.use_case

import com.babacan.defactocase.domain.repository.MoviesRepository
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository,
) {
    suspend operator fun invoke(movieId: String) = moviesRepository.getMovieDetails(movieId)
}
