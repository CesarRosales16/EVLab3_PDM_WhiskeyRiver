package me.cesarrosales16.moviedex.Activities

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.movie_viewer.*
import me.cesarrosales16.moviedex.Constants.AppConstants
import me.cesarrosales16.moviedex.Fragments.MainContentFragment
import me.cesarrosales16.moviedex.Fragments.MainListFragment
import me.cesarrosales16.moviedex.Models.Movie
import me.cesarrosales16.moviedex.R

class ContentViewer : AppCompatActivity(), MainListFragment.ClickedMovieListener {

    private lateinit var mainFragment: MainListFragment
    private lateinit var mainContentFragment: MainContentFragment
    private var resource = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_movie)
        val MovieViewModel = ViewModelProviders.of(this).get(me.cesarrosales16.moviedex.ViewModel.MovieViewModel::class.java)
        var title :String
        val mIntent = intent
        if (mIntent != null) {
            title= mIntent.getStringExtra("TITLE")
            MovieViewModel.fetchMovieByTitle(title)
            MovieViewModel.getMovieResult().observe(this, Observer {resultMovie ->
                showContent(R.id.portrait_main_place_holder,resultMovie)
            })
        }
        else{
            showContent(R.id.portrait_main_place_holder, Movie())
        }


    }



    private fun changeFragment(id: Int, frag: Fragment){ supportFragmentManager.beginTransaction().replace(id, frag).commit() }

    private fun showContent(id_placeholder: Int, movie: Movie) {
        mainContentFragment = MainContentFragment.newInstance(movie)
        changeFragment(id_placeholder, mainContentFragment)
    }

    override fun managePortraitItemClick(movie: Movie) = showContent(R.id.portrait_main_place_holder, movie)

    override fun managedLandscapeItemClick(movie: Movie) = showContent(R.id.land_main_movieviewer_ph, movie)

}
