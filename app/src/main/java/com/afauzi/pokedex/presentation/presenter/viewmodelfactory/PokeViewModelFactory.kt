package com.afauzi.pokedex.presentation.presenter.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.afauzi.pokedex.data.datasource.remote.PokeApiService
import com.afauzi.pokedex.data.repository.PokemonRepository
import com.afauzi.pokedex.presentation.presenter.viewmodel.PokeViewModel

/**
 * Factory untuk membuat instance ViewModel dari kelas PokeViewModel.
 *
 * @param pokemonRepository Objek repository yang menyediakan akses ke data Pokemon.
 * @param pokeApiService Layanan untuk berinteraksi dengan API Pokemon.
 */
class PokeViewModelFactory(
    private val pokemonRepository: PokemonRepository,
    private val pokeApiService: PokeApiService
) : ViewModelProvider.Factory {

    /**
     * Fungsi untuk membuat instance ViewModel dari kelas PokeViewModel.
     *
     * @param modelClass Tipe class dari ViewModel yang ingin dibuat.
     * @return Instance ViewModel yang telah dibuat.
     * @throws IllegalArgumentException Jika tipe class yang diminta bukanlah PokeViewModel.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PokeViewModel::class.java)) {
            // Mengembalikan instance PokeViewModel dengan menyediakan parameter-dependency.
            return PokeViewModel(pokemonRepository, pokeApiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
