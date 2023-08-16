package com.afauzi.pokedex.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.afauzi.pokedex.databinding.ItemPokeLayoutBinding
import com.afauzi.pokedex.domain.entity.Pokemon
import com.bumptech.glide.Glide

class AdapterPokePaging(
    private val context: Context,
    private val listenerPokeAdapter: ListenerPokeAdapter
): PagingDataAdapter<Pokemon, AdapterPokePaging.ViewHolder>(PokeDiffComp) {
    object PokeDiffComp : DiffUtil.ItemCallback<Pokemon>() {
        override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
            return oldItem == newItem
        }

    }

    inner class ViewHolder(val binding: ItemPokeLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(getItem(position)) {

                val parts = this?.url?.split("/")
                val pokeId = parts?.get(parts.size - 2).toString()

                val name = this?.name

                binding.characterName.text = name
                Glide.with(context)
                    .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$pokeId.png")
                    .into(binding.itemImgPoke)

                binding.cardItem.setOnClickListener {
                    listenerPokeAdapter.onClickListenerAdapter(name ?: "")
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPokeLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    interface ListenerPokeAdapter {
        fun onClickListenerAdapter(name: String)
    }
}