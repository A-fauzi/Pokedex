package com.afauzi.pokedex.presentation.view.main.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.afauzi.pokedex.data.datasource.remote.PokeApiProvider
import com.afauzi.pokedex.data.datasource.remote.PokeApiService
import com.afauzi.pokedex.data.repository_implement.PokemonRepository
import com.afauzi.pokedex.databinding.ActivityPokemonDetailBinding
import com.afauzi.pokedex.domain.entity.Type
import com.afauzi.pokedex.presentation.adapter.AdapterTypePoke
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
    private lateinit var adapterTypePoke: AdapterTypePoke

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPokemonDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapterTypePoke = AdapterTypePoke(arrayListOf())
        pokeApiService = PokeApiProvider.providePokeApiService()
        pokemonRepository = PokemonRepository(pokeApiService)
        pokeViewModelFactory = PokeViewModelFactory(pokemonRepository, pokeApiService)
        pokeViewModel = ViewModelProvider(this, pokeViewModelFactory)[PokeViewModel::class.java]



        val pokeName = intent.getStringExtra("pokeName").toString()

        lifecycleScope.launch {
            pokeViewModel.pokeDetail.observe(this@PokemonDetailActivity) {
                val pokeId = it.id.toString()
                val formattedId = Helpers.formatIdPoke(pokeId)

                it.types?.let { type -> adapterTypePoke.setData(type) }

                binding.tvIdFormat.text = formattedId
                binding.collapsingToolbar.title = Helpers.capitalizeChar(pokeName)
                Glide.with(this@PokemonDetailActivity)
                    .load(it.sprites?.other?.home?.frontDefault)
                    .into(binding.imgPokemonCharacter)
            }
            pokeViewModel.getPokeDetail(pokeName)
        }

        binding.rvTypePoke.apply {
            layoutManager = LinearLayoutManager(this@PokemonDetailActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = adapterTypePoke
        }
    }
}