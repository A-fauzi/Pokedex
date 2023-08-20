package com.afauzi.pokedex.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.afauzi.pokedex.data.datasource.remote.PokeApiService
import com.afauzi.pokedex.domain.entity.Pokemon

class PokemonPagingSource(private val pokeApiService: PokeApiService) :
    PagingSource<Int, Pokemon>() {

    /**
     * Implementasi fungsi untuk mendapatkan kunci halaman pertama yang digunakan untuk refresh data.
     *
     * @param state Objek PagingState yang menyimpan informasi tentang status halaman saat ini.
     * @return Kunci halaman pertama yang akan digunakan untuk refresh data.
     */
    override fun getRefreshKey(state: PagingState<Int, Pokemon>): Int {
        // Mengembalikan kunci halaman pertama (0) untuk melakukan refresh data.
        return 0
    }


    /**
     * Implementasi fungsi untuk memuat halaman data Pokemon menggunakan Paging 3 library.
     *
     * @param params Parameter LoadParams yang menyediakan informasi tentang halaman yang akan dimuat.
     * @return LoadResult berisi data halaman, key halaman sebelumnya, dan key halaman berikutnya.
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        return try {
            val responseData = mutableListOf<Pokemon>()

            // Mendapatkan nomor halaman yang akan dimuat.
            val currentLoadingPageKey = params.key ?: 0

            // Mengirim permintaan HTTP untuk mengambil daftar Pokemon dengan offset yang sesuai.
            val response = pokeApiService.getPokemonList(offset = currentLoadingPageKey)

            if (response.isSuccessful) {
                val data = response.body()?.results ?: emptyList()
                responseData.addAll(data)

                // Menghitung key halaman sebelumnya dan key halaman berikutnya.
                val prevKey = if (currentLoadingPageKey > 0) currentLoadingPageKey - 25 else null
                val nextKey = if (responseData.isNotEmpty()) currentLoadingPageKey + 25 else null

                // Mengembalikan LoadResult.Page dengan data halaman, key halaman sebelumnya, dan key halaman berikutnya.
                LoadResult.Page(data = responseData, prevKey = prevKey, nextKey = nextKey)
            } else {
                LoadResult.Error(Exception("API Request Failed"))
            }
        } catch (e: Exception) {
            // Jika terjadi kesalahan, mengembalikan LoadResult.Error dengan Exception.
            LoadResult.Error(e)
        }
    }

}