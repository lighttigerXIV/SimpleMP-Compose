package com.lighttigerxiv.simple.mp.compose.backend.requests

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

fun getDiscogsRetrofit(): DiscogsRequests {

    val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
    val httpClient = OkHttpClient.Builder().addInterceptor(logging).build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.discogs.com/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .client(httpClient)
        .build()

    return retrofit.create(DiscogsRequests::class.java)
}

interface DiscogsRequests{
    @Headers("Content-Type: application/json")
    @GET("database/search")
    fun getArtistCover(
        @Header("Authorization") token: String,
        @Query("q") artist: String,
        @Query("type") type: String = "artist"
    ): Call<String>
}