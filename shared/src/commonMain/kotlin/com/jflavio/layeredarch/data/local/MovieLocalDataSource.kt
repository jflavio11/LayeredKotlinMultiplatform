package com.jflavio.layeredarch.data.local

import com.jflavio.layeredarch.MoviesDb
import com.jflavio.layeredarch.domain.Movie

/**
 * MovieLocalDataSource
 *
 * @author Jose Flavio - jflavio90@gmail.com
 * @since  13/03/2022
 */
class MovieLocalDataSource(private val moviesDb: MoviesDb) {

    suspend fun getLastUpdate(): Long {
        return moviesDb.updatesTableQueries.getLastUpdate("movies").executeAsOne().time
    }

    fun updateLastUpdate(timestamp: Long) {
        moviesDb.updatesTableQueries.insertLastUpdate(timestamp, "movies")
    }

    fun saveMovies(list: List<Movie>) {
        list.forEach { movie ->
            moviesDb.movieTableQueries.insert(
                id = movie.id.toLong(),
                name = movie.name,
                poster_url = movie.posterUrl,
                overview = movie.overview
            )
        }
    }

    fun getMovies(): List<Movie> {
        return moviesDb.movieTableQueries.selectAll().executeAsList().map {
            Movie(
                id = it.id.toString(),
                name = it.name,
                overview = it.overview,
                posterUrl = it.poster_url
            )
        }
    }
}