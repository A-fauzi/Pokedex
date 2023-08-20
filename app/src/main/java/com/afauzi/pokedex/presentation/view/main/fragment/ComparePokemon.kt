package com.afauzi.pokedex.presentation.view.main.fragment

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afauzi.pokedex.R
import com.afauzi.pokedex.data.datasource.remote.PokeApiProvider
import com.afauzi.pokedex.data.datasource.remote.PokeApiService
import com.afauzi.pokedex.data.repository.PokemonRepository
import com.afauzi.pokedex.databinding.FragmentComparePokemonBinding
import com.afauzi.pokedex.databinding.ItemComparisonPokeBinding
import com.afauzi.pokedex.databinding.ItemPokeLayoutBinding
import com.afauzi.pokedex.domain.entity.PokeDetail
import com.afauzi.pokedex.domain.entity.Pokemon
import com.afauzi.pokedex.presentation.adapter.AdapterPokePaging
import com.afauzi.pokedex.presentation.adapter.loadstateadapter.LoadStateAdapterPokemon
import com.afauzi.pokedex.presentation.presenter.viewmodel.PokeViewModel
import com.afauzi.pokedex.presentation.presenter.viewmodelfactory.PokeViewModelFactory
import com.afauzi.pokedex.utils.Helpers
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class ComparePokemon : Fragment() {

    private lateinit var binding: FragmentComparePokemonBinding
    private lateinit var adapterPokePaging: AdapterPokePaging
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
                adapterPokePaging.submitData(pagerData)
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.rvPokemonLis.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = adapterPokePaging.withLoadStateHeaderAndFooter(
                header = LoadStateAdapterPokemon { adapterPokePaging.retry() },
                footer = LoadStateAdapterPokemon { adapterPokePaging.retry() }
            )
        }

        adapterPokePaging.addLoadStateListener { loadState ->
            val isLoading = loadState.refresh is LoadState.Loading

            if (isLoading) {
                binding.rvPokemonLis.visibility = View.GONE
            } else {
                binding.rvPokemonLis.visibility = View.VISIBLE
                binding.progressIndicator.visibility = View.GONE
            }
        }
    }

    private fun getDataCompareOnClick(name: String, image: String) {
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
    private fun setDataToViewAsync(
        pokeDetail: PokeDetail,
        dataCompareView: ItemComparisonPokeBinding
    ) {
        dataCompareView.tvNamePokemon.text = pokeDetail.name?.capitalize()

        Glide.with(requireActivity())
            .load(pokeDetail.sprites?.other?.home?.frontDefault)
            .into(dataCompareView.ivPokemon)

        dataCompareView.tvCodePokemon.text = Helpers.formatIdPoke(pokeDetail.id.toString())

        Helpers.objectColorPaletteImg(
            requireActivity(),
            pokeDetail.sprites?.other?.home?.frontDefault.toString()
        ) {
            dataCompareView.bgDataCompare.setBackgroundColor(it)
        }

        // Menghitung tinggi dan berat dalam unit yang sesuai
        val height = pokeDetail.height?.div(100.0)
        val weight = pokeDetail.weight?.div(10.0)

        dataCompareView.tvDataHeightPoke.text = "$height m"
        dataCompareView.tvDataWeightPoke.text = "$weight kgs"

        bindBaseStats(binding.dataCompareItem1)
        bindBaseStats(binding.dataCompareItem2)

        dataCompareView.hp.tvValueStats.progress = pokeDetail.stats?.get(0)?.baseStat?.toFloat()!!.toInt()
        dataCompareView.hp.tvValueStats.progressTintList = ColorStateList.valueOf(Color.RED)

        dataCompareView.attack.tvValueStats.progress = pokeDetail.stats[1]?.baseStat?.toFloat()!!.toInt()
        dataCompareView.attack.tvValueStats.progressTintList = ColorStateList.valueOf(Color.GREEN)

        dataCompareView.defense.tvValueStats.progress = pokeDetail.stats[2]?.baseStat?.toFloat()!!.toInt()
        dataCompareView.defense.tvValueStats.progressTintList = ColorStateList.valueOf(Color.RED)


        dataCompareView.spAttack.tvValueStats.progress = pokeDetail.stats[3]?.baseStat?.toFloat()!!.toInt()
        dataCompareView.spAttack.tvValueStats.progressTintList = ColorStateList.valueOf(Color.GREEN)

        dataCompareView.spDefense.tvValueStats.progress = pokeDetail.stats[4]?.baseStat?.toFloat()!!.toInt()
        dataCompareView.spDefense.tvValueStats.progressTintList = ColorStateList.valueOf(Color.GREEN)

        dataCompareView.speed.tvValueStats.progress = pokeDetail.stats[5]?.baseStat?.toFloat()!!.toInt()
        dataCompareView.speed.tvValueStats.progressTintList = ColorStateList.valueOf(Color.RED)

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
        adapterPokePaging =
            AdapterPokePaging(requireActivity(), R.layout.item_search_poke) { view, pokemon ->
                bindDataListToView(view, pokemon)
            }
        pokeViewModelFactory = PokeViewModelFactory(pokemonRepository, pokeApiService)
        pokeViewModel = ViewModelProvider(this, pokeViewModelFactory)[PokeViewModel::class.java]
        recyclerView = binding.rvPokemonLis
    }


    private fun bindDataListToView(view: View, pokemon: Pokemon) {
        val parts = pokemon.url?.split("/")
        val name = pokemon.name
        val pokeId = parts?.get(parts.size - 2).toString()

        val characterName = view.findViewById<TextView>(R.id.poke_name)
        val itemImgPoke = view.findViewById<ImageView>(R.id.poke_image)
        val containerView = view.findViewById<LinearLayout>(R.id.container_list_poke_search)
        val cardItem = view.findViewById<CardView>(R.id.card_item_list_poke_search)

        val imgPokemon = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/${pokeId}.png"

        characterName.text = Helpers.capitalizeChar(name.toString())

        Glide.with(requireActivity())
            .load(imgPokemon)
            .into(itemImgPoke)

        Helpers.objectColorPaletteImg(requireActivity(), imgPokemon) { dominantColor: Int ->
            containerView.setBackgroundColor(dominantColor)
        }

        cardItem.setOnClickListener {
            if (name != null) {
                getDataCompareOnClick(name, imgPokemon)
            }
        }
    }


    private fun bindBaseStats(item: ItemComparisonPokeBinding) {
        item.hp.tvTitleStats.text = getString(R.string.hp)
        item.attack.tvTitleStats.text = getString(R.string.attck)
        item.defense.tvTitleStats.text = getString(R.string.defense)
        item.spAttack.tvTitleStats.text = getString(R.string.sp_attack)
        item.spDefense.tvTitleStats.text = getString(R.string.sp_defense)
        item.speed.tvTitleStats.text = getString(R.string.speed)
    }


}