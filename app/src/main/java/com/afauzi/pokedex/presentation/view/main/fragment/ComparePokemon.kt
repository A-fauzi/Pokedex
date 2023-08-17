package com.afauzi.pokedex.presentation.view.main.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
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
import com.afauzi.pokedex.databinding.ItemPokeLayoutBinding
import com.afauzi.pokedex.domain.entity.PokeDetail
import com.afauzi.pokedex.presentation.adapter.AdapterSearchPokePaging
import com.afauzi.pokedex.presentation.presenter.viewmodel.PokeViewModel
import com.afauzi.pokedex.presentation.presenter.viewmodelfactory.PokeViewModelFactory
import com.afauzi.pokedex.utils.Helpers
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class ComparePokemon : Fragment(), AdapterSearchPokePaging.ListenerPokeAdapter {

    private lateinit var binding: FragmentComparePokemonBinding
    private lateinit var adapterSearchPokePaging: AdapterSearchPokePaging
    private lateinit var pokeViewModel: PokeViewModel
    private lateinit var pokeViewModelFactory: PokeViewModelFactory
    private lateinit var pokeApiService: PokeApiService
    private lateinit var pokemonRepository: PokemonRepository
    private lateinit var recyclerView: RecyclerView

    private val listName = arrayListOf<String>()
    private val listImage = arrayListOf<String>()
    private val maxListSize = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentComparePokemonBinding.inflate(layoutInflater, container, false)

        initComponentService()

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

    override fun onClickListenerAdapter(name: String, image: String) {
        if (listName.size < maxListSize) {
            listName.add(name)
            listImage.add(image)
        }

        if (listName.size == 1) {
            binding.layoutCompareView.visibility = View.VISIBLE
            binding.item1Poke.root.visibility = View.VISIBLE
            binding.tvVs.visibility = View.VISIBLE

            setDataToView(listName[0], listImage[0], binding.item1Poke)


        }

        if (listName.size == 2) {
            binding.item2Poke.root.visibility = View.VISIBLE
            binding.btnCompare.visibility = View.VISIBLE
            binding.rvPokemonLis.visibility = View.GONE

            setDataToView(listName[1], listImage[1], binding.item2Poke)

        }


        binding.btnCompare.setOnClickListener {
            binding.layoutDataCompare.visibility = View.VISIBLE

            pokeViewModel.pokeDetail.observe(viewLifecycleOwner) { pokeDetail ->
                if (pokeDetail.name == listName[0]) {
                    setDataToViewAsync(pokeDetail, binding.dataCompareItem1)
                } else if (pokeDetail.name == listName[1]) {
                    setDataToViewAsync(pokeDetail, binding.dataCompareItem2)
                }
            }

            fetchPokemonData()
        }

    }

    private fun setDataToView(
        dataNameOnListByIndex: String,
        dataImageOnListByIndex: String,
        setView: ItemPokeLayoutBinding
    ) {
        setView.characterName.text = dataNameOnListByIndex.capitalize()
        Glide.with(requireActivity())
            .load(dataImageOnListByIndex)
            .into(setView.itemImgPoke)

        Helpers.objectColorPaletteImg(requireActivity(), dataImageOnListByIndex) {
            setView.llBgCard.setBackgroundColor(it)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDataToViewAsync(pokeDetail: PokeDetail, dataCompareView: ItemComparisonPokeBinding) {
        dataCompareView.tvNamePokemon.text = pokeDetail.name
        Glide.with(requireActivity())
            .load(pokeDetail.sprites?.other?.home?.frontDefault)
            .into(dataCompareView.ivPokemon)
        dataCompareView.tvCodePokemon.text = Helpers.formatIdPoke(pokeDetail.id.toString())
        Helpers.objectColorPaletteImg(requireActivity(), pokeDetail.sprites?.other?.home?.frontDefault.toString()) {
            dataCompareView.bgDataCompare.setBackgroundColor(it)
        }

        // Menghitung tinggi dan berat dalam unit yang sesuai
        val height = pokeDetail.height?.div(100.0)
        val weight = pokeDetail.weight?.div(10.0)

        dataCompareView.tvDataHeightPoke.text = "$height m"
        dataCompareView.tvDataWeightPoke.text = "$weight kgs"
    }

    private fun fetchPokemonData() {
        lifecycleScope.launch {
            listName.forEach { name ->
                pokeViewModel.getPokeDetail(name)
            }
        }
    }

    private fun initComponentService() {
        pokeApiService = PokeApiProvider.providePokeApiService()
        pokemonRepository = PokemonRepository(pokeApiService)
        adapterSearchPokePaging = AdapterSearchPokePaging(requireActivity(), this)
        pokeViewModelFactory = PokeViewModelFactory(pokemonRepository, pokeApiService)
        pokeViewModel = ViewModelProvider(this, pokeViewModelFactory)[PokeViewModel::class.java]
        recyclerView = binding.rvPokemonLis
    }
}