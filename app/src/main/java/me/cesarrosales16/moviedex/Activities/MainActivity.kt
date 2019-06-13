package me.cesarrosales16.moviedex.Activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.preview_add_movie.*
import me.cesarrosales16.moviedex.Adapters.RVMovieAdapter
import me.cesarrosales16.moviedex.Adapters.RVPreviewAdapter
import me.cesarrosales16.moviedex.Constants.AppConstants
import me.cesarrosales16.moviedex.Fragments.MainContentFragment
import me.cesarrosales16.moviedex.Models.Movie
import me.cesarrosales16.moviedex.Models.MoviePreview
import me.cesarrosales16.moviedex.R
import me.cesarrosales16.moviedex.ViewModel.MovieViewModel
import java.util.ArrayList

class MainActivity : AppCompatActivity(){


    private lateinit var mainContentFragment: MainContentFragment
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.preview_add_movie)
        viewManager = GridLayoutManager(this, 3)
        if (landscape_fragment != null) {
            mainContentFragment = MainContentFragment.newInstance(Movie())
            supportFragmentManager.beginTransaction().replace(R.id.landscape_fragment, mainContentFragment).commit()
            viewManager = LinearLayoutManager(this)

        }
        val MovieViewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)


        fun clearView(){
            MovieViewModel.nuke()
        }
        val recyclerView = rv_preview
        val moviesPreviewAdapter = RVMovieAdapter(movies = AppConstants.emptymovies,
            clickListener = { movie: Movie ->

                if (landscape_fragment != null) {
                    MovieViewModel.fetchMovieByTitle(movie.Title)
                    MovieViewModel.getMovieResult().observe(this, Observer { resultMovie ->
                        MovieViewModel.insert(resultMovie)
                        mainContentFragment = MainContentFragment.newInstance(resultMovie)
                    })
                    supportFragmentManager.beginTransaction().replace(R.id.landscape_fragment, mainContentFragment)
                        .commit()
                } else {
                    startActivity(Intent(this, ContentViewer::class.java).putExtra("TITLE", movie.Title))
                }

            })
        MovieViewModel.getAll().observe(this, Observer { result ->
            moviesPreviewAdapter.changeDataSet(result)
        })

        recyclerView.apply {
            adapter = moviesPreviewAdapter
            this.layoutManager = viewManager
            setHasFixedSize(true)

        }



        bt_search.setOnClickListener {
            clearView()
            val movieNameQuery = et_search.text.toString()
            if (movieNameQuery.isNotEmpty() && movieNameQuery.isNotBlank()) {
                MovieViewModel.fetchMovie(movieNameQuery).observe(this, Observer { result ->

                    for (movie in result) {
                        MovieViewModel.fetchMovieByTitle(movie.Title)
                        MovieViewModel.getMovieResult().observe(this, Observer { resultMovie ->
                            MovieViewModel.insert(resultMovie)
                        })
                    }

                })
            }

        }


    }





}