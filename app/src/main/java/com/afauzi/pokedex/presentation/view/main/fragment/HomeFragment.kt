package com.afauzi.pokedex.presentation.view.main.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.afauzi.pokedex.data.datasource.remote.PokeApiProvider
import com.afauzi.pokedex.data.datasource.remote.PokeApiService
import com.afauzi.pokedex.data.repository_implement.PokemonRepository
import com.afauzi.pokedex.databinding.FragmentHomeBinding
import com.afauzi.pokedex.presentation.adapter.AdapterPokePaging
import com.afauzi.pokedex.presentation.presenter.viewmodel.PokeViewModel
import com.afauzi.pokedex.presentation.presenter.viewmodelfactory.PokeViewModelFactory
import com.afauzi.pokedex.presentation.view.detail.PokemonDetailActivity
import kotlinx.coroutines.launch

/**
 * Fragment untuk menampilkan daftar Pokemon di tampilan beranda.
 */
class HomeFragment : Fragment(), AdapterPokePaging.ListenerPokeAdapter {

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

        // Menginisialisasi layanan, repository, adapter, dan ViewModel
        pokeApiService = PokeApiProvider.providePokeApiService()
        pokemonRepository = PokemonRepository(pokeApiService)
        adapterPokePaging = AdapterPokePaging(requireActivity(), this)
        pokeViewModelFactory = PokeViewModelFactory(pokemonRepository, pokeApiService)
        pokeViewModel = ViewModelProvider(this, pokeViewModelFactory)[PokeViewModel::class.java]

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

    // Implementasi dari ListenerPokeAdapter untuk menghandle klik pada item Pokemon
    override fun onClickListenerAdapter(name: String) {
        val intent = Intent(requireActivity(), PokemonDetailActivity::class.java)
        intent.putExtra("pokeName", name)
        startActivity(intent)
    }
}

