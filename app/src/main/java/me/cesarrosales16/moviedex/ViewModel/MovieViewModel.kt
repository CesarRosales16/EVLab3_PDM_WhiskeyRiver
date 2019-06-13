package me.cesarrosales16.moviedex.ViewModel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import me.cesarrosales16.moviedex.Database.MainDatabase
import me.cesarrosales16.moviedex.Models.Movie
import me.cesarrosales16.moviedex.Models.MoviePreview
import me.cesarrosales16.moviedex.Network.ApiFactory
import me.cesarrosales16.moviedex.Repository.MovieRepository

class MovieViewModel(val app: Application) : AndroidViewModel(app) {

    private val repository: MovieRepository

    init {
        val movieDao = MainDatabase.getDatabase(app).movieDao()
        repository = MovieRepository(movieDao, ApiFactory.ombdApi)
    }

    private val scope = CoroutineScope(Dispatchers.IO)



    private val movieResult = MutableLiveData<Movie>()

    fun fetchMovie(name: String):LiveData<MutableList<MoviePreview>> {
        var movieslist = MutableLiveData<MutableList<MoviePreview>>()
        scope.launch {
            val response=repository.retrieveMoviesByCoincidenceAsync(name).await()
            if(response.isSuccessful){
                when(response.code()){
                    200->movieslist.postValue(response.body()?.Search?.toMutableList()?:arrayListOf(MoviePreview(Title = "Dummy 1"), MoviePreview(Title = "Dummy 2")))
                }
            }else{
                Toast.makeText(app, "Ocurrio un error", Toast.LENGTH_LONG).show()
            }
        }
        return movieslist
    }

    //fun getMovieListVM(): = movieslist

    fun fetchMovieByTitle(name: String){
        scope.launch {
            val response=repository.retrieveMovieByTitleAsync(name).await()
            if(response.isSuccessful) with(response){
                when(this.code()){
                    200->movieResult.postValue(this.body())
                }
            }else{
                Toast.makeText(app, "Ocurrio un error", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun getMovieResult(): LiveData<Movie> = movieResult


    fun insert(movie: Movie) = scope.launch {
        repository.insert(movie)
    }

    fun getAll():LiveData<List<Movie>> = repository.getAllfromRoomDB()

    fun getMovieByName(name: String): LiveData<List<Movie>> = repository.getMovieByName(name)

    //fun cancelAllRequests() = Dispatchers.IO.cancel()
     fun nuke(){
        scope.launch { repository.nuke() }
    }
}
