package dev.jorgeroldan.marsrover.data.remote

import arrow.retrofit.adapter.either.EitherCallAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit


object RetrofitConfig {

    fun buildRetrofit(json: Json): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://lordgio.github.io/MarsRover/mockdata/")
            .addConverterFactory(json.asConverterFactory(
                contentType = MediaType.get("application/json")
            ))
            .addCallAdapterFactory(EitherCallAdapterFactory())
            .build()
    }
}