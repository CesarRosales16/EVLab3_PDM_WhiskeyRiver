package me.cesarrosales16.moviedex.Repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Deferred
import me.cesarrosales16.moviedex.Database.Domain.MovieDao
import me.cesarrosales16.moviedex.Models.Movie
import me.cesarrosales16.moviedex.Models.OmbdMovieResponse
import me.cesarrosales16.moviedex.Network.OmbdApi
import retrofit2.Response

class MovieRepository(private val movieDao: MovieDao, private val api: OmbdApi){

    fun retrieveMoviesByNameAsync(name:String):Deferred<Response<OmbdMovieResponse>> = api.getMoviesByName(name)

    fun retrieveMoviesByTitleAsync(name:String):Deferred<Response<Movie>> = api.getMovieByTitle(name)

    @WorkerThread
    suspend fun insert(movie: Movie) = movieDao.insertMovie(movie)

    fun getAllfromRoomDB():LiveData<List<Movie>> = movieDao.loadAllMovies()

    fun getMovieByName(name: String) = movieDao.searchMovieByName(name)
}