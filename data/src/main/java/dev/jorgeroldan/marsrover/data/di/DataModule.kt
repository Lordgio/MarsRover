package dev.jorgeroldan.marsrover.data.di

import dev.jorgeroldan.marsrover.data.remote.MarsRoverApi
import dev.jorgeroldan.marsrover.data.remote.RetrofitConfig
import dev.jorgeroldan.marsrover.data.repository.MarsRoverRepositoryImpl
import dev.jorgeroldan.marsrover.domain.repository.MarsRoverRepository
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit

object DataModule {
    val module = module {
        single<Json> { Json }
        single<OkHttpClient> { RetrofitConfig.buildHttpClient() }
        single<Retrofit> { RetrofitConfig.buildRetrofit(get(), get()) }
        single<MarsRoverApi> { get<Retrofit>().create(MarsRoverApi::class.java) }
        factory<MarsRoverRepository> { MarsRoverRepositoryImpl(get()) }
    }
}