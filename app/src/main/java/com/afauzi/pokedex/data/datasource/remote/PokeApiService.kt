package com.afauzi.pokedex.data.datasource.remote

import com.afauzi.pokedex.domain.entity.PokeAbility
import com.afauzi.pokedex.domain.entity.PokeDetail
import com.afauzi.pokedex.domain.entity.PokemonList
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface PokeApiService {

    @GET("pokemon/")
    suspend fun getPokemonList(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int = 25
    ): Response<PokemonList>

    @GET("pokemon/{name}")
    suspend fun getPokemonDetail(@Path("name") name: String): Response<PokeDetail>

    @GET("ability/{name}")
    suspend fun getPokeAbility(@Path("name") name: String): Response<PokeAbility>
}