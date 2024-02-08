package dev.jorgeroldan.marsrover.data.di

import dev.jorgeroldan.marsrover.data.remote.MarsRoverApi
import dev.jorgeroldan.marsrover.data.remote.RetrofitConfig
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import retrofit2.Retrofit

object DataModule {
    val module = module {
        single<Json> { Json }
        single<Retrofit> { RetrofitConfig.buildRetrofit(get()) }
        single<MarsRoverApi> { get<Retrofit>().create(MarsRoverApi::class.java) }
    }
}