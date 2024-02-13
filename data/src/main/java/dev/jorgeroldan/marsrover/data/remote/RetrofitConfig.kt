package dev.jorgeroldan.marsrover.data.remote

import arrow.retrofit.adapter.either.EitherCallAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit


object RetrofitConfig {

    fun buildRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://lordgio.github.io/MarsRover/mockdata/")
            .addConverterFactory(json.asConverterFactory(
                contentType = MediaType.get("application/json")
            ))
            .addCallAdapterFactory(EitherCallAdapterFactory())
            .build()
    }

    fun buildHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()
    }
}