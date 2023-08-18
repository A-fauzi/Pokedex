package com.afauzi.pokedex.presentation.view.main.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.GridLayoutManager
import com.afauzi.pokedex.R
import com.afauzi.pokedex.data.datasource.remote.PokeApiProvider
import com.afauzi.pokedex.data.datasource.remote.PokeApiService
import com.afauzi.pokedex.data.repository_implement.PokemonRepository
import com.afauzi.pokedex.databinding.FragmentHomeBinding
import com.afauzi.pokedex.domain.entity.Pokemon
import com.afauzi.pokedex.presentation.adapter.AdapterPokePaging
import com.afauzi.pokedex.presentation.presenter.viewmodel.PokeViewModel
import com.afauzi.pokedex.presentation.presenter.viewmodelfactory.PokeViewModelFactory
import com.afauzi.pokedex.presentation.view.detail.PokemonDetailActivity
import com.afauzi.pokedex.utils.Helpers
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.launch

/**
 * Fragment untuk menampilkan daftar Pokemon di tampilan beranda.
 */
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapterPokePaging: AdapterPokePaging
    private lateinit var pokeViewModel: PokeViewModel
    private lateinit var pokeViewModelFactory: PokeViewModelFactory
    private lateinit var pokeApiService: PokeApiService
    private lateinit var pokemonRepository: PokemonRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        initComponentService()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mengatur ViewModel dan RecyclerView saat tampilan sudah dibuat
        setUpViewModel()
        setUpRecyclerView()
    }

    // Mengatur ViewModel dengan mengamati data dan mengirimkannya ke adapter
    private fun setUpViewModel() {
        lifecycleScope.launch {
            pokeViewModel.listDataPoke.collect { pagingData ->
                adapterPokePaging.submitData(pagingData)
            }
        }
    }

    // Mengatur RecyclerView dengan mengatur layoutManager dan adapter
    private fun setUpRecyclerView() {
        binding.rvPokemon.apply {
            layoutManager = GridLayoutManager(requireActivity(), 2)
            adapter = adapterPokePaging
        }
    }

    private fun initComponentService() {
        // Menginisialisasi layanan, repository, adapter, dan ViewModel
        pokeApiService = PokeApiProvider.providePokeApiService()
        pokemonRepository = PokemonRepository(pokeApiService)
        pokeViewModelFactory = PokeViewModelFactory(pokemonRepository, pokeApiService)
        pokeViewModel = ViewModelProvider(this, pokeViewModelFactory)[PokeViewModel::class.java]
        adapterPokePaging =
            AdapterPokePaging(requireActivity(), R.layout.item_poke_layout) { view, pokemon ->
                bindDataToView(view, pokemon)
            }
    }


    private fun bindDataToView(view: View, pokemon: Pokemon) {
        val parts = pokemon.url?.split("/")
        val name = pokemon.name
        val pokeId = parts?.get(parts.size - 2).toString()

        val characterName = view.findViewById<TextView>(R.id.character_name)
        val itemImgPoke = view.findViewById<ImageView>(R.id.item_img_poke)
        val containerView = view.findViewById<LinearLayout>(R.id.ll_bg_card)
        val cardItem = view.findViewById<CardView>(R.id.card_item)

        val imgPokemon = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/${pokeId}.png"

        characterName.text = Helpers.capitalizeChar(name.toString())

        Glide.with(requireActivity())
            .load(imgPokemon)
            .into(itemImgPoke)

        Helpers.objectColorPaletteImg(requireActivity(), imgPokemon) { dominantColor: Int ->
            containerView.setBackgroundColor(dominantColor)
        }

        cardItem.setOnClickListener {
            val intent = Intent(requireActivity(), PokemonDetailActivity::class.java)
            intent.putExtra("pokeName", pokemon.name)
            startActivity(intent)
        }
    }

}
