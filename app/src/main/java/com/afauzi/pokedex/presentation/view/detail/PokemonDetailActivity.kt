package com.afauzi.pokedex.presentation.view.detail

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.afauzi.pokedex.R
import com.afauzi.pokedex.data.datasource.remote.PokeApiProvider
import com.afauzi.pokedex.data.datasource.remote.PokeApiService
import com.afauzi.pokedex.data.repository.PokemonRepository
import com.afauzi.pokedex.databinding.ActivityPokemonDetailBinding
import com.afauzi.pokedex.domain.entity.PokeDetail
import com.afauzi.pokedex.domain.entity.TypesItem
import com.afauzi.pokedex.presentation.adapter.AdapterChip
import com.afauzi.pokedex.presentation.adapter.AdapterViewPagerPokeDetail
import com.afauzi.pokedex.presentation.presenter.viewmodel.PokeViewModel
import com.afauzi.pokedex.presentation.presenter.viewmodelfactory.PokeViewModelFactory
import com.afauzi.pokedex.utils.Helpers
import com.bumptech.glide.Glide
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
    private lateinit var adapterChip: AdapterChip<TypesItem>
    private val dataListType: ArrayList<TypesItem> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi antarmuka pengguna dan komponen terkait
        binding = ActivityPokemonDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponentService()

        // Mendapatkan nama pokemon dari intent
        val pokeName = intent.getStringExtra("pokeName").toString()

        // Menjalankan pemrosesan detail pokemon menggunakan viewModel dan LiveData
        lifecycleScope.launch {
            pokeViewModel.pokeDetail.observe(this@PokemonDetailActivity) {
                setDataToViewAsync(it, pokeName)
            }

            // Meminta data detail pokemon melalui viewModel
            pokeViewModel.getPokeDetail(pokeName)
        }

        // Menampilkan tipe-tipe pokemon pada recyclerView
        binding.rvTypePoke.apply {
            layoutManager = LinearLayoutManager(
                this@PokemonDetailActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = adapterChip
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

    private fun setDataToViewAsync(
        it: PokeDetail,
        pokeName: String
    ) {
        val pokeId = it.id.toString()
        val formattedId = Helpers.formatIdPoke(pokeId)

        // Mengisi adapter tipe pokemon dengan data tipe
        adapterChip.setData(it.types as List<TypesItem>)

        // Menampilkan informasi detail pokemon pada antarmuka
        binding.tvIdFormat.text = formattedId
        binding.collapsingToolbar.title = Helpers.capitalizeChar(pokeName)

        Glide.with(this@PokemonDetailActivity)
            .load(it.sprites?.other?.home?.frontDefault)
            .into(binding.imgPokemonCharacter)

        // Mendapatkan warna palet dari gambar dan mengatur tampilan antarmuka berdasarkan warna dari object Helpers
        Helpers.objectColorPaletteImg(
            this,
            it.sprites?.other?.home?.frontDefault.toString()
        ) { dominantColor ->
            binding.appBar.setBackgroundColor(dominantColor)

            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = dominantColor

            binding.collapsingToolbar.setContentScrimColor(dominantColor)
        }
    }

    private fun initComponentService() {
        // Inisialisasi layanan API dan repositori
        adapterChip = AdapterChip(dataListType, R.layout.item_type_poke) { view, typesItem ->
            val textTypeView: TextView = view.findViewById(R.id.item_type_poke)
            textTypeView.text = typesItem.type?.name
        }
        pokeApiService = PokeApiProvider.providePokeApiService()
        pokemonRepository = PokemonRepository(pokeApiService)
        pokeViewModelFactory = PokeViewModelFactory(pokemonRepository, pokeApiService)
        pokeViewModel = ViewModelProvider(this, pokeViewModelFactory)[PokeViewModel::class.java]
    }
}
