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
import com.afauzi.pokedex.data.repository.PokemonPagingSource
import com.afauzi.pokedex.data.repository.PokemonRepository
import com.afauzi.pokedex.domain.entity.PokeAbility
import com.afauzi.pokedex.domain.entity.PokeDetail
import kotlinx.coroutines.launch

class PokeViewModel(
    private val pokemonRepository: PokemonRepository,
    private val pokeApiService: PokeApiService
    ): ViewModel() {


    /**
     * Membuat aliran data Pokemon dalam mode berhalaman.
     *
     * Langkah 1:
     * Menggunakan Pager untuk mengatur pengambilan data dalam halaman.
     * Parameter PagingConfig(pageSize = 25) mengatur jumlah item per halaman.
     *
     * Langkah 2:
     * Menginisialisasi PokemonPagingSource di dalam blok lambda {} Pager.
     * PokemonPagingSource adalah sumber data berhalaman dari PokeApiService.
     *
     * Langkah 3:
     * Mengubah hasil Pager menjadi aliran data berkelanjutan (Flow) menggunakan .flow.
     * cachedIn(viewModelScope) menyimpan aliran data dalam viewModelScope.
     * viewModelScope adalah cakupan ViewModel yang menggunakan aliran data ini.
     *
     * Hasil Akhir:
     * Variabel listDataPoke berisi aliran data Pokemon diatur dalam mode berhalaman
     * dengan ukuran halaman 25, diambil dari PokemonPagingSource via PokeApiService.
     * Aliran data tetap aktif dalam viewModelScope.
     */
    val listDataPoke = Pager(PagingConfig(pageSize = 25)) {
        PokemonPagingSource(pokeApiService)
    }.flow.cachedIn(viewModelScope)


    /**
     * ViewModel yang bertanggung jawab untuk mengelola detail Pokemon.
     *
     * Properti private _pokeDetail berfungsi sebagai penyimpanan internal data detail Pokemon.
     * Properti publik pokeDetail memberikan akses hanya baca ke data detail Pokemon.
     *
     * Fungsi getPokeDetail digunakan untuk mengambil detail Pokemon dari repository.
     * Fungsi ini akan dilakukan dalam viewModelScope.
     *
     * Langkah-langkah:
     * 1. Menggunakan viewModelScope.launch untuk menjalankan operasi asinkron dalam cakupan ViewModel.
     * 2. Di dalam blok try, mengambil detail Pokemon dari repository menggunakan pokemonRepository.
     * 3. Jika berhasil, memperbarui nilai _pokeDetail melalui _pokeDetail.value.
     * 4. Jika terjadi kesalahan, menangkap Exception dan mencatat pesan error menggunakan Log.
     *
     * @param pokemonName Nama Pokemon yang ingin diambil detailnya.
     */
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


    /**
     * ViewModel yang mengelola informasi tentang kemampuan (ability) Pokemon.
     *
     * Properti private _pokeAbility digunakan sebagai penyimpanan internal informasi kemampuan.
     * Properti publik pokeAbility memberikan akses baca ke informasi kemampuan Pokemon.
     *
     * Fungsi getPokeAbility digunakan untuk mengambil informasi kemampuan dari repository.
     * Fungsi ini akan dijalankan dalam viewModelScope.
     *
     * Langkah-langkah:
     * 1. Menggunakan viewModelScope.launch untuk menjalankan operasi asinkron dalam cakupan ViewModel.
     * 2. Di dalam blok try, mengambil informasi kemampuan Pokemon dari repository menggunakan pokemonRepository.
     * 3. Jika berhasil, memperbarui nilai _pokeAbility melalui _pokeAbility.value.
     * 4. Jika terjadi kesalahan, menangkap Exception dan mencatat pesan error menggunakan Log.
     *
     * @param abilityName Nama kemampuan Pokemon yang ingin diambil informasinya.
     */
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