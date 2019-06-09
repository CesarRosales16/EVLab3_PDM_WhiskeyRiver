package me.cesarrosales16.moviedex.Fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.movies_list_fragment.view.*
import me.cesarrosales16.moviedex.Adapters.RVMovieAdapter
import me.cesarrosales16.moviedex.Constants.AppConstants
import me.cesarrosales16.moviedex.Models.Movie
import me.cesarrosales16.moviedex.ViewModel.MovieViewModel
import java.lang.RuntimeException
import androidx.appcompat.widget.SearchView
import me.cesarrosales16.moviedex.R


class MainListFragment: Fragment() {

    private lateinit var movieViewModel: MovieViewModel
    private lateinit var moviesAdapter: RVMovieAdapter
    var listenerTool : ClickedMovieListener? = null

    interface ClickedMovieListener{
        fun managePortraitItemClick(movie: Movie)
        fun managedLandscapeItemClick(movie: Movie)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ClickedMovieListener) {
            listenerTool = context
        } else {
            throw RuntimeException("Se necesita una implementación de la interfaz")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listenerTool = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(me.cesarrosales16.moviedex.R.layout.movies_list_fragment, container, false)

        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)

        initRecyclerView(resources.configuration.orientation, view)

        movieViewModel.getAll().observe(this, Observer { result ->
            moviesAdapter.changeDataSet(result)
        })

        return view
    }

    fun initRecyclerView(orientation: Int, container: View) {
        val linearLayoutManager = LinearLayoutManager(this.context)
        val recyclerview  = container.rv_list

        moviesAdapter = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            RVMovieAdapter(movies = AppConstants.emptymovies, clickListener = { movie: Movie -> listenerTool?.managePortraitItemClick(movie)})
        }else {
            RVMovieAdapter(movies = AppConstants.emptymovies, clickListener = { movie: Movie -> listenerTool?.managedLandscapeItemClick(movie)})
        }

        recyclerview.apply {
            adapter = moviesAdapter
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)

        val searchView = menu.findItem(R.id.app_bar_search).actionView as SearchView
        searchView.isSubmitButtonEnabled = true

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //Hace que cuando presiones el botón de sumbit se ejecute lo que pongas aquí
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //Hace que cambie dinamicamente mientras escribis, porque ejecuta lo que pongas aquí cada vez que escribis.
                queryToDatabase(newText?: "N/A")
                return true
            }

        })
    }

    private fun queryToDatabase(query: String) = movieViewModel.getMovieByName("%$query%").observe(this,
        Observer { queryResult -> moviesAdapter.changeDataSet(queryResult)})
}
