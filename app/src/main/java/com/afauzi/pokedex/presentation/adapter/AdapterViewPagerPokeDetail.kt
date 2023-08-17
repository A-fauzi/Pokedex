package com.afauzi.pokedex.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.afauzi.pokedex.presentation.view.detail.fragment.AbilityPokeFragment
import com.afauzi.pokedex.presentation.view.detail.fragment.InfoPokeFragment
import com.afauzi.pokedex.presentation.view.detail.fragment.StatisticsPokeFragment

/**
 * Adapter khusus untuk ViewPager dalam tampilan detail Pokemon.
 *
 * @param fragmentManager Objek FragmentManager untuk mengelola fragment dalam ViewPager.
 * @param lifecycle Objek Lifecycle untuk mengatur siklus hidup fragment dalam ViewPager.
 */
class AdapterViewPagerPokeDetail(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    private val numTabs = 3 // Jumlah tab atau fragment dalam ViewPager.

    /**
     * Mengembalikan jumlah fragment dalam ViewPager.
     */
    override fun getItemCount() = numTabs

    /**
     * Membuat fragment sesuai dengan posisi yang diberikan.
     *
     * @param position Posisi fragment dalam ViewPager.
     * @return Fragment yang sesuai dengan posisi.
     */
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> InfoPokeFragment() // Fragment Info Pokemon.
            1 -> StatisticsPokeFragment() // Fragment Statistik Pokemon.
            else -> AbilityPokeFragment() // Fragment Kemampuan Pokemon.
        }
    }
}
