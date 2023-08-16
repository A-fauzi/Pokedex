package com.afauzi.pokedex.data.repository_implement

import com.afauzi.pokedex.data.datasource.remote.PokeApiService
import com.afauzi.pokedex.domain.entity.PokeDetail

class PokemonRepository(private val pokeApiService: PokeApiService) {

    interface OnFinishedRequestPoke {
        fun onSuccess()
        fun onError()
    }

    suspend fun getPokemonDetail(pokemonName: String): PokeDetail? {
        val response = pokeApiService.getPokemonDetail(pokemonName)
        if (response.isSuccessful) {
            return response.body()
        } else {
            throw Exception("Error Retrieving Pokemon Detail")
        }
    }
}