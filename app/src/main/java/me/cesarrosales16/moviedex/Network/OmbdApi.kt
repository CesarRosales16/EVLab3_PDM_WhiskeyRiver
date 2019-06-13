package me.cesarrosales16.moviedex.Network

import kotlinx.coroutines.Deferred
import me.cesarrosales16.moviedex.Models.Movie
import me.cesarrosales16.moviedex.Models.OmbdMovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OmbdApi {

    @GET("/")
    fun getMoviesByCoincidence(@Query("s") query: String): Deferred<Response<OmbdMovieResponse>>

    @GET("/")
    fun getPagedMoviesByCoincidence(@Query("page") page: Int, @Query("s") query: String): Deferred<Response<OmbdMovieResponse>>

    @GET("/")
    fun getMovieByTitle(@Query("t") query: String): Deferred<Response<Movie>>

    @GET("/")
    fun getMovieById(@Query("i") query: String): Deferred<Response<Movie>>
}
