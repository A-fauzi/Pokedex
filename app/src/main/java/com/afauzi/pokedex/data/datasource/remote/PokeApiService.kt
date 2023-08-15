package com.afauzi.pokedex.data.datasource.remote

import com.afauzi.pokedex.domain.entity.PokemonList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PokeApiService {
    /*
        Endpoint:

        Pokemon
        Pokemon(id or name)
        ability(id or name)
     */


    @GET("pokemon/")
    suspend fun getPokemonList(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int = 20
    ): Response<PokemonList>
}