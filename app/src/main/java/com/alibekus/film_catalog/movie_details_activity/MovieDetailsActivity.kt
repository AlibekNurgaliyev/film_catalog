package com.alibekus.film_catalog.movie_details_activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibekus.film_catalog.R
import com.alibekus.film_catalog.hide
import com.alibekus.film_catalog.main_menu_activity.MainMenuActivity
import com.alibekus.film_catalog.network.Movie
import com.alibekus.film_catalog.presentation.MovieAdapter
import com.alibekus.film_catalog.presentation.MoviesRepository
import com.alibekus.film_catalog.show
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop

const val MOVIE_ID = "extra_movie_id"
const val MOVIE_BACKDROP = "extra_movie_backdrop"
const val MOVIE_TITLE = "extra_movie_title"
const val MOVIE_RELEASE_DATE = "extra_movie_release_date"
const val MOVIE_OVERVIEW = "extra_movie_overview"

class MovieDetails : AppCompatActivity() {
    private lateinit var backdrop: ImageView
    private lateinit var title: TextView
    private lateinit var releaseDate: TextView
    private lateinit var overview: TextView
    private lateinit var suggestedMovieText: TextView
    private lateinit var arrowBackButton:ImageButton

    private lateinit var suggestedMovies: RecyclerView
    private lateinit var suggestedMoviesAdapter: MovieAdapter
    private lateinit var suggestedMoviesLayoutManager: LinearLayoutManager
    private var suggestedMoviesPage = 1
    private var movieId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        bindViews()

        val extras = intent.extras
        if (extras != null) {
            getDetails(extras)
        } else {
            finish()
        }

        getSuggestedMovies()
    }

    private fun bindViews() {
        backdrop = findViewById(R.id.movie_backdrop)
        suggestedMovieText = findViewById(R.id.suggested_movies_text)
        title = findViewById(R.id.movie_title)
        releaseDate = findViewById(R.id.movie_release_date)
        overview = findViewById(R.id.movie_overview)
        arrowBackButton = findViewById(R.id.activity_movie_details_arrow_back)

        arrowBackButton.setOnClickListener {
            val intent = Intent(this,MainMenuActivity::class.java)
            startActivity(intent)
        }

        suggestedMovies = findViewById(R.id.suggested_movies)
        suggestedMoviesLayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        suggestedMovies.layoutManager = suggestedMoviesLayoutManager
        suggestedMoviesAdapter = MovieAdapter(mutableListOf()) { movie -> showDetails(movie) }
        suggestedMovies.adapter = suggestedMoviesAdapter
    }

    private fun getSuggestedMovies() {
        MoviesRepository.getSuggestedMovies(
            movieId,
            suggestedMoviesPage,
            ::onSuggestedMoviesFetched,
            ::onError
        )
    }

    private fun onSuggestedMoviesFetched(movies: List<Movie>) {
        suggestedMoviesAdapter.appendMovies(movies)
        attachSuggestedMoviesOnScrollListener()
    }

    private fun attachSuggestedMoviesOnScrollListener() {
        suggestedMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = suggestedMoviesLayoutManager.itemCount
                val visibleItemCount = suggestedMoviesLayoutManager.childCount
                val firstVisibleItem =
                    suggestedMoviesLayoutManager.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    suggestedMovies.removeOnScrollListener(this)
                    suggestedMoviesPage++
                    getSuggestedMovies()
                }
            }
        })
    }

    private fun onError() {
    }

    private fun showDetails(movie: Movie) {
        val intent = Intent(this, MovieDetails::class.java)
        intent.putExtra(MOVIE_ID, movie.id)
        intent.putExtra(MOVIE_BACKDROP, movie.backdropPath)
        intent.putExtra(MOVIE_TITLE, movie.title)
        intent.putExtra(MOVIE_RELEASE_DATE, movie.releaseDate)
        intent.putExtra(MOVIE_OVERVIEW, movie.overview)
        startActivity(intent)
    }

    private fun getDetails(extras: Bundle) {
        extras.getString(MOVIE_BACKDROP)?.let { backdropPath ->
            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w1280$backdropPath")
                .transform(CenterCrop())
                .into(backdrop)
        }

        movieId = extras.getInt(MOVIE_ID, 0)
        title.text = extras.getString(MOVIE_TITLE, "")
        releaseDate.text = extras.getString(MOVIE_RELEASE_DATE, "")
        overview.text = extras.getString(MOVIE_OVERVIEW, "")
    }
}