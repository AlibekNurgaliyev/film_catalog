package com.alibekus.film_catalog.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val API_KEY = "d7416bbafa8240f7469779d19f715dfe"

interface Api {

    @GET("movie/now_playing")
    fun getNowPlayingMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int
    ): Call<GetMoviesResponse>

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int
    ): Call<GetMoviesResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int
    ): Call<GetMoviesResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int
    ): Call<GetMoviesResponse>

    @GET("movie/{movie_id}/similar")
    fun getSuggestedMovies(
        @Path(value = "movie_id", encoded = true) movie_id: String,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int
    ): Call<GetMoviesResponse>
}