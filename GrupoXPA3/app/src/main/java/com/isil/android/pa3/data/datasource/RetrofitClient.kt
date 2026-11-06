package com.isil.android.pa3.data.datasource

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // IMPORTANTE: cambia la IP por la de tu PC (la muestra la consola al ejecutar "npm start").
    // Celular real: la PC y el celular deben estar en la misma Wi-Fi.
    // Emulador de Android Studio: http://10.0.2.2:3000/
    //const val BASE_URL = "http://192.168.18.12:3000/"
    const val BASE_URL = "http://localhost:3000/"
    fun provideApiService(tokenManager: TokenManager): ApiService {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Agrega el token guardado en DataStore a cada petición
        val authInterceptor = Interceptor { chain ->
            val token = runBlocking { tokenManager.getToken.first() }
            val request = chain.request().newBuilder().apply {
                if (!token.isNullOrBlank()) {
                    header("Authorization", "Bearer $token")
                }
            }.build()
            chain.proceed(request)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
