package com.jflavio.layeredarch.data

import com.jflavio.layeredarch.DispatcherProvider
import com.jflavio.layeredarch.data.local.MovieLocalDataSource
import com.jflavio.layeredarch.data.remote.MovieRemoteDataSource
import com.jflavio.layeredarch.domain.Movie
import com.jflavio.layeredarch.domain.MovieRepository
import kotlinx.coroutines.withContext

/**
 * MovieRepositoryImpl
 *
 * @author Jose Flavio - jflavio90@gmail.com
 * @since  24/12/2021
 */
class MovieRepositoryImpl(
    private val localDataSource: MovieLocalDataSource,
    private val remoteDataSource: MovieRemoteDataSource,
    private val timeProvider: TimeProvider,
    private val dispatcherProvider: DispatcherProvider
) : MovieRepository {

    override suspend fun getMovies(): List<Movie> {
        return withContext(dispatcherProvider.io) {
            var localMovies = localDataSource.getMovies()
            // TODO define the update of local data by an Update Policy: another business logic class
            if (localMovies.isEmpty() || timeProvider.timestamp - localDataSource.getLastUpdate() > 60 * 60 * 1000) {
                localMovies = remoteDataSource.getMovies()
            }
            localDataSource.saveMovies(localMovies)
            localDataSource.updateLastUpdate(timeProvider.timestamp)
            localMovies
        }
    }

}