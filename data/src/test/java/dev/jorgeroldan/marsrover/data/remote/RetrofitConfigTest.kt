package dev.jorgeroldan.marsrover.data.remote

import arrow.retrofit.adapter.either.EitherCallAdapterFactory
import io.mockk.mockk
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import org.junit.Assert
import org.junit.Test
import retrofit2.Retrofit

class RetrofitConfigTest {

    @Test
    fun `buildRetrofit returns a properly configured instance`() {
        val jsonMock: Json = mockk(relaxed = true)
        val okHttpClientMock: OkHttpClient = mockk(relaxed = true)
        val result = RetrofitConfig.buildRetrofit(okHttpClientMock, jsonMock)

        Assert.assertTrue(result is Retrofit)
        Assert.assertTrue(result.callAdapterFactories().first()::class.java.canonicalName == EitherCallAdapterFactory::class.java.canonicalName)
        Assert.assertEquals("https://lordgio.github.io/MarsRover/mockdata/", result.baseUrl().url().toString())
    }

    @Test
    fun `buildHttpClient returns a properly configured instance`() {
        val result = RetrofitConfig.buildHttpClient()

        Assert.assertTrue(result is OkHttpClient)
        Assert.assertEquals(10000, result.connectTimeoutMillis())
        Assert.assertEquals(20000, result.readTimeoutMillis())
    }
}