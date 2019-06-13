package me.cesarrosales16.moviedex.Database.Domain

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.cesarrosales16.moviedex.Models.Movie

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: Movie)

    @Query("select * from Movie")
    fun loadAllMovies(): LiveData<List<Movie>>

    @Query("select * from Movie where Title like :name")
    fun searchMovieByName(name: String): LiveData<List<Movie>>

    @Query("DELETE FROM Movie")
    suspend fun nukeTable()
}