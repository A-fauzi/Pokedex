package com.afauzi.pokedex.domain.repository

import com.afauzi.pokedex.domain.entity.PokeAbility
import com.afauzi.pokedex.domain.entity.PokeDetail

interface PokemonRepositoryInterface {
    suspend fun getPokemonDetail(pokemonName: String): PokeDetail?
    suspend fun getPokeAbility(abilityName: String): PokeAbility?
}