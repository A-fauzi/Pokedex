package com.afauzi.pokedex.data.repository_implement

import com.afauzi.pokedex.data.datasource.remote.PokeApiService
import com.afauzi.pokedex.domain.entity.PokeAbility
import com.afauzi.pokedex.domain.entity.PokeDetail
import com.afauzi.pokedex.domain.repository.PokemonRepositoryInterface

class PokemonRepository(private val pokeApiService: PokeApiService): PokemonRepositoryInterface {

    /**
     * Implementasi fungsi untuk mengambil detail Pokemon dari layanan PokeApi.
     *
     * @param pokemonName Nama Pokemon yang ingin diambil detailnya.
     * @return Objek PokeDetail jika berhasil, atau null jika terjadi kesalahan.
     * @throws Exception Jika terjadi kesalahan dalam mengambil detail Pokemon.
     */
    override suspend fun getPokemonDetail(pokemonName: String): PokeDetail? {
        // Mengirim permintaan HTTP untuk mengambil detail Pokemon dengan nama tertentu.
        val response = pokeApiService.getPokemonDetail(pokemonName)

        // Memeriksa apakah respons HTTP berhasil (kode status 2xx).
        if (response.isSuccessful) {
            // Mengembalikan isi respons sebagai objek PokeDetail.
            return response.body()
        } else {
            // Jika respons tidak berhasil, lemparkan pengecualian dengan pesan error.
            throw Exception("Error Retrieving Pokemon Detail")
        }
    }


    /**
     * Implementasi fungsi untuk mengambil informasi kemampuan (ability) Pokemon dari layanan PokeApi.
     *
     * @param abilityName Nama kemampuan Pokemon yang ingin diambil informasinya.
     * @return Objek PokeAbility jika berhasil, atau null jika terjadi kesalahan.
     * @throws Exception Jika terjadi kesalahan dalam mengambil informasi kemampuan Pokemon.
     */
    override suspend fun getPokeAbility(abilityName: String): PokeAbility? {
        // Mengirim permintaan HTTP untuk mengambil informasi kemampuan Pokemon dengan nama tertentu.
        val response = pokeApiService.getPokeAbility(abilityName)

        // Memeriksa apakah respons HTTP berhasil (kode status 2xx).
        if (response.isSuccessful) {
            // Mengembalikan isi respons sebagai objek PokeAbility.
            return response.body()
        } else {
            // Jika respons tidak berhasil, lemparkan pengecualian dengan pesan error.
            throw Exception("Error Retrieving Pokemon Ability")
        }
    }

}