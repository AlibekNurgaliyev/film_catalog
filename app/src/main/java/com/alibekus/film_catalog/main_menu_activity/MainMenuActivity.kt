package com.alibekus.film_catalog.main_menu_activity

import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibekus.film_catalog.R
import com.alibekus.film_catalog.hide
import com.alibekus.film_catalog.movie_details_activity.*
import com.alibekus.film_catalog.network.Movie
import com.alibekus.film_catalog.presentation.MovieAdapter
import com.alibekus.film_catalog.presentation.MoviesRepository
import com.alibekus.film_catalog.show

private const val VISIBLE_THRESHOLD = 10

class MainMenuActivity : AppCompatActivity() {
    private lateinit var internetConnectionErrorMessage: TextView
    private lateinit var progressBar: ProgressBar

    private lateinit var nowPlayingMoviesCaption: TextView
    private lateinit var popularMoviesCaption: TextView
    private lateinit var topRatedMoviesCaption: TextView
    private lateinit var upcomingMoviesCaption: TextView

    private lateinit var nowPlayingMovies: RecyclerView
    private lateinit var nowPlayingMoviesAdapter: MovieAdapter
    private lateinit var nowPlayingMoviesLayoutManager: LinearLayoutManager

    private lateinit var popularMovies: RecyclerView
    private lateinit var popularMoviesAdapter: MovieAdapter
    private lateinit var popularMoviesLayoutManager: LinearLayoutManager

    private lateinit var topRatedMovies: RecyclerView
    private lateinit var topRatedMoviesAdapter: MovieAdapter
    private lateinit var topRatedMoviesLayoutManager: LinearLayoutManager

    private lateinit var upcomingMovies: RecyclerView
    private lateinit var upcomingMoviesAdapter: MovieAdapter
    private lateinit var upcomingMoviesLayoutManager: LinearLayoutManager

    private var nowPlayingMoviesPage = 1
    private var popularMoviesPage = 1
    private var topRatedMoviesPage = 1
    private var upcomingMoviesPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        bindViews()

        getPopularMovies()
        getNowPlayingMovies()
        getTopRatedMovies()
        getUpcomingMovies()

    }

    private fun bindViews() {
        internetConnectionErrorMessage = findViewById(R.id.internet_connection_error)
        progressBar = findViewById(R.id.activity_main_menu_progress_bar)

        nowPlayingMoviesCaption = findViewById(R.id.now_playing_movies_caption)
        popularMoviesCaption = findViewById(R.id.popular_movies_caption)
        topRatedMoviesCaption = findViewById(R.id.top_rated_movies_caption)
        upcomingMoviesCaption = findViewById(R.id.upcoming_movies_caption)

        nowPlayingMovies = findViewById(R.id.now_playing_movies)
        nowPlayingMoviesLayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        nowPlayingMovies.layoutManager = nowPlayingMoviesLayoutManager
        nowPlayingMoviesAdapter = MovieAdapter(mutableListOf()) { movie -> showMovieDetails(movie) }
        nowPlayingMovies.adapter = nowPlayingMoviesAdapter

        popularMovies = findViewById(R.id.popular_movies)
        popularMoviesLayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        popularMovies.layoutManager = popularMoviesLayoutManager
        popularMoviesAdapter = MovieAdapter(mutableListOf()) { movie -> showMovieDetails(movie) }
        popularMovies.adapter = popularMoviesAdapter

        topRatedMovies = findViewById(R.id.top_rated_movies)
        topRatedMoviesLayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        topRatedMovies.layoutManager = topRatedMoviesLayoutManager
        topRatedMoviesAdapter = MovieAdapter(mutableListOf()) { movie -> showMovieDetails(movie) }
        topRatedMovies.adapter = topRatedMoviesAdapter

        upcomingMovies = findViewById(R.id.upcoming_movies)
        upcomingMoviesLayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        upcomingMovies.layoutManager = upcomingMoviesLayoutManager
        upcomingMoviesAdapter = MovieAdapter(mutableListOf()) { movie -> showMovieDetails(movie) }
        upcomingMovies.adapter = upcomingMoviesAdapter
    }

    private fun getNowPlayingMovies() {
        progressBar.show()
        MoviesRepository.getNowPlayingMovies(
            nowPlayingMoviesPage,
            ::onNowPlayingMoviesFetched,
            ::onError
        )
    }

    private fun getPopularMovies() {
        progressBar.show()
        MoviesRepository.getPopularMovies(
            popularMoviesPage,
            ::onPopularMoviesFetched,
            ::onError
        )
    }

    private fun getTopRatedMovies() {
        progressBar.show()
        MoviesRepository.getTopRatedMovies(
            topRatedMoviesPage,
            ::onTopRatedMoviesFetched,
            ::onError
        )
    }

    private fun getUpcomingMovies() {
        progressBar.show()
        MoviesRepository.getUpcomingMovies(
            upcomingMoviesPage,
            ::onUpcomingMoviesFetched,
            ::onError
        )
    }

    private fun extendNowPlayingMoviesScrollListener() {
        nowPlayingMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = nowPlayingMoviesLayoutManager.itemCount
                val visibleItemCount = nowPlayingMoviesLayoutManager.childCount
                val firstVisibleItem =
                    nowPlayingMoviesLayoutManager.findFirstVisibleItemPosition()

                if ((totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_THRESHOLD)) {
                    nowPlayingMovies.removeOnScrollListener(this)
                    nowPlayingMoviesPage++
                    getNowPlayingMovies()
                }
            }
        })
    }

    private fun onNowPlayingMoviesFetched(movies: List<Movie>) {
        nowPlayingMoviesAdapter.appendMovies(movies)
        extendNowPlayingMoviesScrollListener()
        nowPlayingMoviesCaption.show()
        progressBar.hide()
    }

    private fun extendPopularMoviesScrollListener() {
        popularMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = popularMoviesLayoutManager.itemCount
                val visibleItemCount = popularMoviesLayoutManager.childCount
                val firstVisibleItem =
                    popularMoviesLayoutManager.findFirstVisibleItemPosition()

                if ((totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_THRESHOLD)) {
                    popularMovies.removeOnScrollListener(this)
                    popularMoviesPage++
                    getPopularMovies()
                }
            }
        })
    }

    private fun onPopularMoviesFetched(movies: List<Movie>) {
        popularMoviesAdapter.appendMovies(movies)
        extendPopularMoviesScrollListener()
        popularMoviesCaption.show()
        progressBar.hide()
    }

    private fun extendTopRatedMoviesScrollListener() {
        topRatedMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = topRatedMoviesLayoutManager.itemCount
                val visibleItemCount = topRatedMoviesLayoutManager.childCount
                val firstVisibleItem =
                    topRatedMoviesLayoutManager.findFirstVisibleItemPosition()

                if ((totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_THRESHOLD)) {
                    topRatedMovies.removeOnScrollListener(this)
                    topRatedMoviesPage++
                    getTopRatedMovies()
                }
            }

        })
    }

    private fun onTopRatedMoviesFetched(movies: List<Movie>) {
        topRatedMoviesAdapter.appendMovies(movies)
        extendTopRatedMoviesScrollListener()
        topRatedMoviesCaption.show()
        progressBar.hide()
    }

    private fun extendUpcomingMoviesScrollListener() {
        upcomingMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = upcomingMoviesLayoutManager.itemCount
                val visibleItemCount = upcomingMoviesLayoutManager.childCount
                val firstVisibleItem =
                    upcomingMoviesLayoutManager.findFirstVisibleItemPosition()

                if ((totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_THRESHOLD)) {
                    upcomingMovies.removeOnScrollListener(this)
                    upcomingMoviesPage++
                    getUpcomingMovies()
                }
            }
        })
    }

    private fun onUpcomingMoviesFetched(movies: List<Movie>) {
        upcomingMoviesAdapter.appendMovies(movies)
        extendUpcomingMoviesScrollListener()
        upcomingMoviesCaption.show()
        progressBar.hide()
    }

    private fun onError() {
        internetConnectionErrorMessage.show()
    }

    private fun showMovieDetails(movie: Movie) {
        val intent = Intent(this, MovieDetails::class.java)
        intent.putExtra(MOVIE_ID, movie.id)
        intent.putExtra(MOVIE_BACKDROP, movie.backdropPath)
        intent.putExtra(MOVIE_TITLE, movie.title)
        intent.putExtra(MOVIE_RELEASE_DATE, movie.releaseDate)
        intent.putExtra(MOVIE_OVERVIEW, movie.overview)
        startActivity(intent)
    }
}