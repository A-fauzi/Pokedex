package com.afauzi.pokedex.presentation.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.afauzi.pokedex.data.datasource.remote.PokeApiService
import com.afauzi.pokedex.data.repository_implement.pagingsource.PokemonPagingSource

class PokeViewModel(private val pokeApiService: PokeApiService): ViewModel() {
    val listDataPoke = Pager(PagingConfig(pageSize = 20)) {
        PokemonPagingSource(pokeApiService)
    }.flow.cachedIn(viewModelScope)
}