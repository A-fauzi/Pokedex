package com.afauzi.pokedex.presentation.presenter.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.afauzi.pokedex.data.datasource.remote.PokeApiService
import com.afauzi.pokedex.data.repository_implement.PokemonPagingSource
import com.afauzi.pokedex.data.repository_implement.PokemonRepository
import com.afauzi.pokedex.domain.entity.PokeAbility
import com.afauzi.pokedex.domain.entity.PokeDetail
import kotlinx.coroutines.launch

class PokeViewModel(
    private val pokemonRepository: PokemonRepository,
    private val pokeApiService: PokeApiService
    ): ViewModel() {
    /**
     * Get List Poke
     */
    val listDataPoke = Pager(PagingConfig(pageSize = 25)) {
        PokemonPagingSource(pokeApiService)
    }.flow.cachedIn(viewModelScope)

    private val _pokeDetail = MutableLiveData<PokeDetail>()
    val pokeDetail: LiveData<PokeDetail>
        get() = _pokeDetail
    fun getPokeDetail(pokemonName: String) {
        viewModelScope.launch {
            try {
                _pokeDetail.value = pokemonRepository.getPokemonDetail(pokemonName)
            }catch (e: Exception) {
                Log.e("PokeViewModel", "Error retrieving poke detail", e)
            }
        }
    }

    private val _pokeAbility = MutableLiveData<PokeAbility>()
    val pokeAbility: LiveData<PokeAbility>
        get() = _pokeAbility
    fun getPokeAbility(abilityName: String) {
        viewModelScope.launch {
            try {
                _pokeAbility.value = pokemonRepository.getPokeAbility(abilityName)
            }catch (e: Exception) {
                Log.e("PokeViewModel", "Error retrieving poke ability", e)
            }
        }
    }

}