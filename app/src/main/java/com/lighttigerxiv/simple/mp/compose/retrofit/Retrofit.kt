package com.lighttigerxiv.simple.mp.compose.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*

object DiscogsApiBuilder {

    private val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
    private val client = OkHttpClient.Builder().addInterceptor(logging).build()


    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.discogs.com/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun <T> buildService(service: Class<T>): T {
        return retrofit.create(service)
    }
}

fun getDiscogsRetrofit(): RetrofitRequests {
    return DiscogsApiBuilder.buildService(RetrofitRequests::class.java)
}

interface RetrofitRequests {
    @Headers("Content-Type: application/json")
    @GET("database/search")
    fun getArtistCover(
        @Header("Authorization") token: String,
        @Query("q") artist: String,
        @Query("type") type: String = "artist"
    ): Call<String>
}