package com.afauzi.pokedex.data.repository_implement.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.afauzi.pokedex.data.datasource.remote.PokeApiService
import com.afauzi.pokedex.domain.entity.Pokemon

class PokemonPagingSource(private val pokeApiService: PokeApiService): PagingSource<Int, Pokemon>(){
    override fun getRefreshKey(state: PagingState<Int, Pokemon>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        try {
            val currentLoadingPageKey = params.key ?: 0
            val response = pokeApiService.getPokemonList(offset = currentLoadingPageKey)
            val responseData = mutableListOf<Pokemon>()
            val data = response.body()?.results
            data.let {
                if (it != null) {
                    responseData.addAll(it)
                }
            }

            val prevKey = if (currentLoadingPageKey == 0) null else currentLoadingPageKey - 20
            return if (responseData.isEmpty()) {
                LoadResult.Page(
                    data = responseData,
                    prevKey = prevKey,
                    nextKey =  null
                )
            } else {
                LoadResult.Page(
                    data = responseData,
                    prevKey = prevKey,
                    nextKey =  currentLoadingPageKey.plus(20)
                )
            }
        }catch (e: Exception){
            return LoadResult.Error(e)
        }
    }
}