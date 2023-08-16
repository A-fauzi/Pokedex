package com.afauzi.pokedex.presentation.view.main.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.afauzi.pokedex.data.datasource.remote.PokeApiProvider
import com.afauzi.pokedex.data.datasource.remote.PokeApiService
import com.afauzi.pokedex.data.repository_implement.PokemonRepository
import com.afauzi.pokedex.databinding.ActivityPokemonDetailBinding
import com.afauzi.pokedex.presentation.adapter.AdapterPokePaging
import com.afauzi.pokedex.presentation.presenter.viewmodel.PokeViewModel
import com.afauzi.pokedex.presentation.presenter.viewmodelfactory.PokeViewModelFactory
import com.afauzi.pokedex.utils.Helpers
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class PokemonDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPokemonDetailBinding
    private lateinit var pokeViewModel: PokeViewModel
    private lateinit var pokeViewModelFactory: PokeViewModelFactory
    private lateinit var pokeApiService: PokeApiService
    private lateinit var pokemonRepository: PokemonRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPokemonDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pokeApiService = PokeApiProvider.providePokeApiService()
        pokemonRepository = PokemonRepository(pokeApiService)
        pokeViewModelFactory = PokeViewModelFactory(pokemonRepository, pokeApiService)
        pokeViewModel = ViewModelProvider(this, pokeViewModelFactory)[PokeViewModel::class.java]



        val pokeName = intent.getStringExtra("pokeName").toString()

        lifecycleScope.launch {
            pokeViewModel.pokeDetail.observe(this@PokemonDetailActivity) {
                Log.d("PokeDetail", it.toString())
                binding.collapsingToolbar.title = Helpers.capitalizeChar(pokeName)
                Glide.with(this@PokemonDetailActivity)
                    .load(it.sprites?.other?.home?.frontDefault)
                    .into(binding.imgPokemonCharacter)
            }
            pokeViewModel.getPokeDetail(pokeName)
        }
    }
}