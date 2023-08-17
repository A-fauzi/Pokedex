package com.afauzi.pokedex.presentation.view.detail.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.afauzi.pokedex.R
import com.afauzi.pokedex.data.datasource.remote.PokeApiProvider
import com.afauzi.pokedex.data.datasource.remote.PokeApiService
import com.afauzi.pokedex.data.repository_implement.PokemonRepository
import com.afauzi.pokedex.databinding.FragmentStatisticsPokeBinding
import com.afauzi.pokedex.presentation.presenter.viewmodel.PokeViewModel
import com.afauzi.pokedex.presentation.presenter.viewmodelfactory.PokeViewModelFactory
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import kotlinx.coroutines.launch

/**
 * Fragment untuk menampilkan grafik statistik Pokemon.
 */
class StatisticsPokeFragment : Fragment() {

    private lateinit var binding: FragmentStatisticsPokeBinding

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
        binding = FragmentStatisticsPokeBinding.inflate(layoutInflater, container, false)

        initComponentService()

        // Memanggil fungsi untuk mendapatkan data statistik dan mengisi grafik
        getBarChartData()

        return binding.root
    }

    // Fungsi untuk mendapatkan data statistik dan mengisi grafik
    private fun getBarChartData() {
        val pokeName = requireActivity().intent.getStringExtra("pokeName").toString()

        // Mengamati data dari ViewModel menggunakan lifecycleScope
        lifecycleScope.launch {
            pokeViewModel.pokeDetail.observe(requireActivity()) { pokeDetail ->
                // Mendapatkan referensi ke grafik horizontal bar dari layout
                val horizontalBarChart: HorizontalBarChart = binding.horizontalBarChart

                // Membuat daftar entry untuk grafik
                val entries: ArrayList<BarEntry> = ArrayList()
                entries.add(BarEntry(0f, pokeDetail.stats?.get(5)?.baseStat!!.toFloat())) // speed
                entries.add(BarEntry(1f, pokeDetail.stats[4]?.baseStat!!.toFloat())) // spes def
                entries.add(BarEntry(2f, pokeDetail.stats[3]?.baseStat!!.toFloat())) // spes att
                entries.add(BarEntry(3f, pokeDetail.stats[2]?.baseStat!!.toFloat())) // def
                entries.add(BarEntry(4f, pokeDetail.stats[1]?.baseStat!!.toFloat())) // attack
                entries.add(BarEntry(5f, pokeDetail.stats[0]?.baseStat!!.toFloat())) // hp

                // Membuat dataset untuk grafik
                val barDataSet = BarDataSet(entries, "Pokemon Statistic")
                barDataSet.color = resources.getColor(R.color.blue)

                val dataSets: ArrayList<IBarDataSet> = ArrayList()
                dataSets.add(barDataSet)

                val data = BarData(dataSets)
                horizontalBarChart.data = data

                // Mengatur label sumbu X sesuai dengan nama statistik
                val months = arrayOf("HP", "Attack", "Defense", "Special Attack", "Special Defense", "Speed").reversed()
                horizontalBarChart.xAxis.valueFormatter = IndexAxisValueFormatter(months)
                horizontalBarChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
                horizontalBarChart.xAxis.granularity = 1f

                // Menambahkan animasi
                horizontalBarChart.animateY(1000, Easing.EaseInOutQuad)

                // Memperbarui grafik
                horizontalBarChart.invalidate()
            }

            // Memanggil fungsi untuk mendapatkan detail Pokemon
            pokeViewModel.getPokeDetail(pokeName)
        }
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
