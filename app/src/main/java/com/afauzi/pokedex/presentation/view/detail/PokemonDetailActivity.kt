package com.afauzi.pokedex.presentation.view.detail

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import com.afauzi.pokedex.R
import com.afauzi.pokedex.data.datasource.remote.PokeApiProvider
import com.afauzi.pokedex.data.datasource.remote.PokeApiService
import com.afauzi.pokedex.data.repository_implement.PokemonRepository
import com.afauzi.pokedex.databinding.ActivityPokemonDetailBinding
import com.afauzi.pokedex.presentation.adapter.AdapterTypePoke
import com.afauzi.pokedex.presentation.adapter.AdapterViewPagerPokeDetail
import com.afauzi.pokedex.presentation.presenter.viewmodel.PokeViewModel
import com.afauzi.pokedex.presentation.presenter.viewmodelfactory.PokeViewModelFactory
import com.afauzi.pokedex.utils.Helpers
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch


/**
 * Activity yang menampilkan detail informasi tentang suatu pokemon.
 */
class PokemonDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPokemonDetailBinding
    private lateinit var pokeViewModel: PokeViewModel
    private lateinit var pokeViewModelFactory: PokeViewModelFactory
    private lateinit var pokeApiService: PokeApiService
    private lateinit var pokemonRepository: PokemonRepository
    private lateinit var adapterTypePoke: AdapterTypePoke

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi antarmuka pengguna dan komponen terkait
        binding = ActivityPokemonDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi adapter untuk tipe pokemon
        adapterTypePoke = AdapterTypePoke(arrayListOf())

        // Inisialisasi layanan API dan repositori
        pokeApiService = PokeApiProvider.providePokeApiService()
        pokemonRepository = PokemonRepository(pokeApiService)
        pokeViewModelFactory = PokeViewModelFactory(pokemonRepository, pokeApiService)
        pokeViewModel = ViewModelProvider(this, pokeViewModelFactory)[PokeViewModel::class.java]

        // Mendapatkan nama pokemon dari intent
        val pokeName = intent.getStringExtra("pokeName").toString()

        // Menjalankan pemrosesan detail pokemon menggunakan viewModel dan LiveData
        lifecycleScope.launch {
            pokeViewModel.pokeDetail.observe(this@PokemonDetailActivity) {
                val pokeId = it.id.toString()
                val formattedId = Helpers.formatIdPoke(pokeId)

                // Mengisi adapter tipe pokemon dengan data tipe
                it.types?.let { type -> adapterTypePoke.setData(type) }

                // Menampilkan informasi detail pokemon pada antarmuka
                binding.tvIdFormat.text = formattedId
                binding.collapsingToolbar.title = Helpers.capitalizeChar(pokeName)

                Glide.with(this@PokemonDetailActivity)
                    .load(it.sprites?.other?.home?.frontDefault)
                    .into(binding.imgPokemonCharacter)

                // Mendapatkan warna palet dari gambar dan mengatur tampilan antarmuka berdasarkan warna
                Glide.with(this@PokemonDetailActivity)
                    .asBitmap()
                    .load(it.sprites?.other?.home?.frontDefault)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            Palette.from(resource).generate { palette ->
                                palette.let {
                                    val dominantColor = palette?.getDominantColor(ContextCompat.getColor(this@PokemonDetailActivity, R.color.blue))

                                    if (dominantColor != null) {
                                        binding.appBar.setBackgroundColor(dominantColor)

                                        val window: Window = window
                                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                                        window.statusBarColor = dominantColor

                                        binding.collapsingToolbar.setContentScrimColor(dominantColor)
                                    }
                                }
                            }
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                            // Do nothing or handle placeholder
                        }
                    })
            }

            // Meminta data detail pokemon melalui viewModel
            pokeViewModel.getPokeDetail(pokeName)
        }

        // Menampilkan tipe-tipe pokemon pada recyclerView
        binding.rvTypePoke.apply {
            layoutManager = LinearLayoutManager(this@PokemonDetailActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = adapterTypePoke
        }

        // Konfigurasi ViewPager dan TabLayout untuk menampilkan tampilan fragment yang berbeda
        val itemTabs = arrayOf("About", "Statistic", "Ability")
        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        val adapterViewPager = AdapterViewPagerPokeDetail(supportFragmentManager, lifecycle)
        viewPager.adapter = adapterViewPager

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = itemTabs[position]
        }.attach()
    }
}
