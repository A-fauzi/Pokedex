package com.afauzi.pokedex.presentation.view.main.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afauzi.pokedex.data.datasource.remote.PokeApiProvider
import com.afauzi.pokedex.data.datasource.remote.PokeApiService
import com.afauzi.pokedex.data.repository_implement.PokemonRepository
import com.afauzi.pokedex.databinding.FragmentComparePokemonBinding
import com.afauzi.pokedex.databinding.ItemComparisonPokeBinding
import com.afauzi.pokedex.domain.entity.PokeDetail
import com.afauzi.pokedex.presentation.adapter.AdapterSearchPokePaging
import com.afauzi.pokedex.presentation.presenter.viewmodel.PokeViewModel
import com.afauzi.pokedex.presentation.presenter.viewmodelfactory.PokeViewModelFactory
import com.afauzi.pokedex.utils.Helpers
import com.bumptech.glide.Glide
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class ComparePokemon : Fragment(), AdapterSearchPokePaging.ListenerPokeAdapter {

    private lateinit var binding: FragmentComparePokemonBinding
    private lateinit var adapterSearchPokePaging: AdapterSearchPokePaging
    private lateinit var pokeViewModel: PokeViewModel
    private lateinit var pokeViewModelFactory: PokeViewModelFactory
    private lateinit var pokeApiService: PokeApiService
    private lateinit var pokemonRepository: PokemonRepository
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentComparePokemonBinding.inflate(layoutInflater, container, false)
        pokeApiService = PokeApiProvider.providePokeApiService()
        pokemonRepository = PokemonRepository(pokeApiService)
        adapterSearchPokePaging = AdapterSearchPokePaging(requireActivity(), this)
        pokeViewModelFactory = PokeViewModelFactory(pokemonRepository, pokeApiService)
        pokeViewModel = ViewModelProvider(this, pokeViewModelFactory)[PokeViewModel::class.java]
        recyclerView = binding.rvPokemonLis
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        setUpViewModel()
    }

    private fun setUpViewModel() {
        lifecycleScope.launch {
            pokeViewModel.listDataPoke.collect { pagerData ->
                adapterSearchPokePaging.submitData(pagerData)
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.rvPokemonLis.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = adapterSearchPokePaging
        }
    }

    private val listName = arrayListOf<String>()
    private val listImage = arrayListOf<String>()

    private val maxListSize = 2

    override fun onClickListenerAdapter(name: String, image: String) {
        if (listName.size < maxListSize) {
            listName.add(name)
            listImage.add(image)
        }

        if (listName.size == 1) {
            binding.layoutCompareView.visibility = View.VISIBLE
            binding.item1Poke.root.visibility = View.VISIBLE
            binding.item1Poke.characterName.text = listName[0].capitalize()
            binding.tvVs.visibility = View.VISIBLE
            Glide.with(requireActivity())
                .load(listImage[0])
                .into(binding.item1Poke.itemImgPoke)
        }

        if (listName.size == 2) {
            binding.item2Poke.root.visibility = View.VISIBLE
            binding.item2Poke.characterName.text = listName[1].capitalize()
            Glide.with(requireActivity())
                .load(listImage[1])
                .into(binding.item2Poke.itemImgPoke)

            binding.btnCompare.visibility = View.VISIBLE
            binding.rvPokemonLis.visibility = View.GONE
        }


        binding.btnCompare.setOnClickListener {
            binding.layoutDataCompare.visibility = View.VISIBLE

            Log.d("ComparePokemon", "lis: $listName")
            Log.d("ComparePokemon", "data 1: ${listName[0]}")
            Log.d("ComparePokemon", "data 2: ${listName[1]}")

            pokeViewModel.pokeDetail.observe(viewLifecycleOwner) { pokeDetail ->
                if (pokeDetail.name == listName[0]) {
                    setDataToView(pokeDetail, binding.dataCompareItem1)
                } else if (pokeDetail.name == listName[1]) {
                    setDataToView(pokeDetail, binding.dataCompareItem2)
                }
            }

            fetchPokemonData()
        }

    }

    private fun setDataToView(pokeDetail: PokeDetail, dataCompareView: ItemComparisonPokeBinding) {
        dataCompareView.tvNamePokemon.text = pokeDetail.name
        Glide.with(requireActivity())
            .load(pokeDetail.sprites?.other?.home?.frontDefault)
            .into(dataCompareView.ivPokemon)
        dataCompareView.tvCodePokemon.text = Helpers.formatIdPoke(pokeDetail.id.toString())
    }

    private fun fetchPokemonData() {
        lifecycleScope.launch {
            listName.forEach { name ->
                pokeViewModel.getPokeDetail(name)
            }
        }
    }
}