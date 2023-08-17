package com.afauzi.pokedex.presentation.view.detail.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.afauzi.pokedex.R
import com.afauzi.pokedex.data.datasource.remote.PokeApiProvider
import com.afauzi.pokedex.data.datasource.remote.PokeApiService
import com.afauzi.pokedex.data.repository_implement.PokemonRepository
import com.afauzi.pokedex.databinding.FragmentAbilityPokeBinding
import com.afauzi.pokedex.domain.entity.PokeAbility
import com.afauzi.pokedex.presentation.adapter.AdapterAbilitiesPoke
import com.afauzi.pokedex.presentation.adapter.AdapterTypePoke
import com.afauzi.pokedex.presentation.presenter.viewmodel.PokeViewModel
import com.afauzi.pokedex.presentation.presenter.viewmodelfactory.PokeViewModelFactory
import com.afauzi.pokedex.utils.Helpers
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch


/**
 * Fragment untuk menampilkan informasi tentang kemampuan Pokemon.
 */
class AbilityPokeFragment : Fragment(), AdapterAbilitiesPoke.AdapterAbilityListener {

    private lateinit var binding: FragmentAbilityPokeBinding

    // Deklarasi ViewModel, Repository, ViewModelFactory, dan Adapter
    private lateinit var pokeViewModel: PokeViewModel
    private lateinit var pokeViewModelFactory: PokeViewModelFactory
    private lateinit var pokeApiService: PokeApiService
    private lateinit var pokemonRepository: PokemonRepository
    private lateinit var adapterAbilitiesPoke: AdapterAbilitiesPoke

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAbilityPokeBinding.inflate(layoutInflater, container, false)

        initComponentService()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pokeName = requireActivity().intent.getStringExtra("pokeName").toString()

        // Mengamati data dari ViewModel menggunakan lifecycleScope
        lifecycleScope.launch {
            pokeViewModel.pokeDetail.observe(requireActivity()) { pokeDetail ->
                // Menggunakan adapter untuk mengisi daftar kemampuan Pokemon
                pokeDetail.abilities?.let { abilities -> adapterAbilitiesPoke.setData(abilities) }
            }

            // Memanggil fungsi untuk mendapatkan detail Pokemon
            pokeViewModel.getPokeDetail(pokeName)
        }

        // Mengatur RecyclerView dan adapter untuk daftar kemampuan Pokemon
        binding.rvAbilitiesPoke.apply {
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            adapter = adapterAbilitiesPoke
        }
    }

    // Implementasi dari AdapterAbilitiesPoke.AdapterAbilityListener
    override fun onclickListenerAbility(name: String) {
        lifecycleScope.launch {
            pokeViewModel.pokeAbility.observe(requireActivity()) { pokeAbility ->
                setDataToViewAsync(pokeAbility)
            }

            // Memanggil fungsi untuk mendapatkan informasi kemampuan Pokemon
            pokeViewModel.getPokeAbility(name)
        }
    }

    private fun setDataToViewAsync(pokeAbility: PokeAbility) {
        // Mengambil dan menampilkan deskripsi kemampuan
        val sortedEntries = pokeAbility.effectEntries?.filter { effect -> effect?.language?.name == "en" }

        sortedEntries?.forEach { effectEntries ->
            binding.tvAbilityDesc.text = effectEntries?.effect.toString()

            binding.tvAbilityDesc.setBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.blue
                )
            )
            binding.tvAbilityDesc.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.white
                )
            )
        }
    }

    private fun initComponentService() {
        // Membuat instance adapter untuk kemampuan Pokemon
        adapterAbilitiesPoke = AdapterAbilitiesPoke(this, arrayListOf())

        // Menginisialisasi layanan dan repository
        pokeApiService = PokeApiProvider.providePokeApiService()
        pokemonRepository = PokemonRepository(pokeApiService)

        // Membuat ViewModelFactory dengan repository dan layanan
        pokeViewModelFactory = PokeViewModelFactory(pokemonRepository, pokeApiService)

        // Membuat instance ViewModel menggunakan ViewModelProvider
        pokeViewModel = ViewModelProvider(this, pokeViewModelFactory)[PokeViewModel::class.java]
    }
}
