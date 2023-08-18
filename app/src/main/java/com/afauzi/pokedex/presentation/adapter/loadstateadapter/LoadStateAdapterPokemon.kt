package com.afauzi.pokedex.presentation.adapter.loadstateadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.afauzi.pokedex.databinding.ItemLoadStateBinding

class LoadStateAdapterPokemon(private val retry: () -> Unit): LoadStateAdapter<LoadStateAdapterPokemon.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemLoadStateBinding, private val retry: () -> Unit): RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.progressIndicator.visibility = View.GONE
                binding.tvError.visibility = View.VISIBLE
                binding.tvError.text = loadState.error.localizedMessage
            } else {
                binding.progressIndicator.visibility = View.VISIBLE
            }

            binding.progressIndicator.isVisible = (loadState is LoadState.Loading)
            binding.btnRetry.isVisible = (loadState is LoadState.Error)
            binding.tvError.isVisible = (loadState is LoadState.Error)

            binding.btnRetry.setOnClickListener {
                binding.tvError.visibility = View.GONE
                retry
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder = ViewHolder(
        ItemLoadStateBinding.inflate(LayoutInflater.from(parent.context), parent, false), retry
    )
}