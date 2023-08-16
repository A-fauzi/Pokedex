package com.afauzi.pokedex.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.afauzi.pokedex.presentation.view.detail.fragment.AbilityPokeFragment
import com.afauzi.pokedex.presentation.view.detail.fragment.InfoPokeFragment
import com.afauzi.pokedex.presentation.view.detail.fragment.StatisticsPokeFragment

private const val NUM_TABS = 3

class AdapterViewPagerPokeDetail(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount() = NUM_TABS

    override fun createFragment(position: Int): Fragment {
        when(position) {
            0 -> return InfoPokeFragment()
            1 -> return StatisticsPokeFragment()
        }
        return AbilityPokeFragment()
    }

}