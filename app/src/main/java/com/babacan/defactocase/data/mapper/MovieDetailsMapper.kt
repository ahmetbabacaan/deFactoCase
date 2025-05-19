package com.babacan.defactocase.data.mapper

import com.babacan.defactocase.data.model.MovieDetailsResponse
import com.babacan.defactocase.domain.model.MovieDetails
import com.babacan.defactocase.domain.model.Rating
import javax.inject.Inject

class MovieDetailsMapper @Inject constructor(){

    fun map(movieDetailsResponseDao: MovieDetailsResponse) = MovieDetails(
        actors = movieDetailsResponseDao.actors,
        awards = movieDetailsResponseDao.awards,
        boxOffice = movieDetailsResponseDao.boxOffice,
        country = movieDetailsResponseDao.country,
        dVD = movieDetailsResponseDao.dVD,
        director = movieDetailsResponseDao.director,
        genre = movieDetailsResponseDao.genre,
        imdbID = movieDetailsResponseDao.imdbID,
        imdbRating = movieDetailsResponseDao.imdbRating,
        imdbVotes = movieDetailsResponseDao.imdbVotes,
        language = movieDetailsResponseDao.language,
        metascore = movieDetailsResponseDao.metascore,
        plot = movieDetailsResponseDao.plot,
        poster = movieDetailsResponseDao.poster,
        production = movieDetailsResponseDao.production,
        rated = movieDetailsResponseDao.rated,
        ratings = movieDetailsResponseDao.ratings?.map {
            Rating(it.source, it.value)
        },
        released = movieDetailsResponseDao.released,
        response = movieDetailsResponseDao.response,
        runtime = movieDetailsResponseDao.runtime,
        title = movieDetailsResponseDao.title,
        type = movieDetailsResponseDao.type,
        website = movieDetailsResponseDao.website,
        writer = movieDetailsResponseDao.writer,
        year = movieDetailsResponseDao.year
    )
}