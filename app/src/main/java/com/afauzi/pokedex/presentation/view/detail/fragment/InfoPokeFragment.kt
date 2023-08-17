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
import com.afauzi.pokedex.domain.entity.PokeDetail
import com.afauzi.pokedex.presentation.adapter.AdapterTypePoke
import com.afauzi.pokedex.presentation.presenter.viewmodel.PokeViewModel
import com.afauzi.pokedex.presentation.presenter.viewmodelfactory.PokeViewModelFactory
import kotlinx.coroutines.launch

/**
 * Fragment untuk menampilkan informasi detail Pokemon.
 */
class InfoPokeFragment : Fragment() {
    private lateinit var binding: FragmentInforPokeBinding

    // Deklarasi ViewModel, Repository, dan ViewModelFactory
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

        initComponentService()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pokeName = requireActivity().intent.getStringExtra("pokeName").toString()

        // Mengamati data dari ViewModel menggunakan lifecycleScope
        lifecycleScope.launch {
            pokeViewModel.pokeDetail.observe(requireActivity()) { pokeDetail ->
                setDataToViewAsync(pokeDetail)
            }

            // Memanggil fungsi untuk mendapatkan detail Pokemon
            pokeViewModel.getPokeDetail(pokeName)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDataToViewAsync(pokeDetail: PokeDetail) {
        // Mengatur teks untuk TextView
        binding.includedViewName.tvText.text = "Name"
        binding.includedViewHeight.tvText.text = "Height"
        binding.includedViewWeight.tvText.text = "Weight"

        // Menghitung tinggi dan berat dalam unit yang sesuai
        val height = pokeDetail.height?.div(100.0)
        val weight = pokeDetail.weight?.div(10.0)

        // Mengisi TextViews dengan nilai dari Pokemon detail
        binding.includedViewName.tvNamePoke.text = pokeDetail.name
        binding.includedViewHeight.tvNamePoke.text = "$height m"
        binding.includedViewWeight.tvNamePoke.text = "$weight kgs"
    }

    private fun initComponentService() {
        // Menginisialisasi layanan dan repository
        pokeApiService = PokeApiProvider.providePokeApiService()
        pokemonRepository = PokemonRepository(pokeApiService)

        // Membuat ViewModelFactory dengan repository dan layanan
        pokeViewModelFactory = PokeViewModelFactory(pokemonRepository, pokeApiService)

        // Membuat instance ViewModel menggunakan ViewModelProvider
        pokeViewModel = ViewModelProvider(this, pokeViewModelFactory)[PokeViewModel::class.java]
    }
}
