package com.afauzi.pokedex.data.datasource.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PokeApiProvider {
    private const val BASE_URL = "https://pokeapi.co/api/v2/"

    private val httpClient = OkHttpClient.Builder()

        // Menambahkan sebuah interceptor untuk melakukan logging HTTP.
        .addInterceptor(HttpLoggingInterceptor().apply {

            // Mengatur tingkat logging untuk interceptor.
            level = HttpLoggingInterceptor.Level.BODY
        })

        // Membangun instance OkHttpClient dengan konfigurasi yang telah ditentukan.
        .build()


    private val retrofit = Retrofit.Builder()

        // Mengatur URL dasar (base URL) dari API yang akan diakses.
        .baseUrl(BASE_URL)

        // Menggunakan klien HTTP yang telah dikonfigurasi sebelumnya.
        .client(httpClient)

        // Menambahkan konverter untuk mengonversi data JSON ke objek Kotlin.
        .addConverterFactory(GsonConverterFactory.create())

        // Membangun instance Retrofit dengan konfigurasi yang telah diatur sebelumnya.
        .build()

    fun providePokeApiService(): PokeApiService {

        // Menggunakan instance Retrofit yang telah dikonfigurasi sebelumnya.
        // Memanggil metode create untuk membuat implementasi dari antarmuka PokeApiService.
        return retrofit.create(PokeApiService::class.java)
    }

}