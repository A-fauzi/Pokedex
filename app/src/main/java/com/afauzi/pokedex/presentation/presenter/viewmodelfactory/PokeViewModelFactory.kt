package com.afauzi.pokedex.presentation.presenter.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.afauzi.pokedex.data.datasource.remote.PokeApiService
import com.afauzi.pokedex.data.repository_implement.PokemonRepository
import com.afauzi.pokedex.presentation.presenter.viewmodel.PokeViewModel

class PokeViewModelFactory(
    private val pokemonRepository: PokemonRepository,
    private val pokeApiService: PokeApiService
    ): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PokeViewModel::class.java)) {
            return PokeViewModel(pokemonRepository, pokeApiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}