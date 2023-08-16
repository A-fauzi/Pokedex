package com.afauzi.pokedex.presentation.view.detail.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.afauzi.pokedex.R
import com.afauzi.pokedex.data.datasource.remote.PokeApiProvider
import com.afauzi.pokedex.data.datasource.remote.PokeApiService
import com.afauzi.pokedex.data.repository_implement.PokemonRepository
import com.afauzi.pokedex.databinding.FragmentInforPokeBinding
import com.afauzi.pokedex.presentation.adapter.AdapterTypePoke
import com.afauzi.pokedex.presentation.presenter.viewmodel.PokeViewModel
import com.afauzi.pokedex.presentation.presenter.viewmodelfactory.PokeViewModelFactory
import kotlinx.coroutines.launch

class InfoPokeFragment : Fragment() {
    private lateinit var binding: FragmentInforPokeBinding

    private lateinit var pokeViewModel: PokeViewModel
    private lateinit var pokeViewModelFactory: PokeViewModelFactory
    private lateinit var pokeApiService: PokeApiService
    private lateinit var pokemonRepository: PokemonRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentInforPokeBinding.inflate(layoutInflater, container, false)

        pokeApiService = PokeApiProvider.providePokeApiService()
        pokemonRepository = PokemonRepository(pokeApiService)
        pokeViewModelFactory = PokeViewModelFactory(pokemonRepository, pokeApiService)
        pokeViewModel = ViewModelProvider(this, pokeViewModelFactory)[PokeViewModel::class.java]

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pokeName = requireActivity().intent.getStringExtra("pokeName").toString()

        lifecycleScope.launch {
            pokeViewModel.pokeDetail.observe(requireActivity()) {

                // TextView Keys
                binding.includedViewName.tvText.text = "Name"
                binding.includedViewHeight.tvText.text = "Height"
                binding.includedViewWeight.tvText.text = "Weight"


                val height = it.height?.div(100.0)
                val weight = it.weight?.div(10.0)
                // TextViews Value
                binding.includedViewName.tvNamePoke.text = it.name
                binding.includedViewHeight.tvNamePoke.text = "$height m"
                binding.includedViewWeight.tvNamePoke.text = "$weight kgs"
            }

            pokeViewModel.getPokeDetail(pokeName)
        }
    }
}