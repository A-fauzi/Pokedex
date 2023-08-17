package com.afauzi.pokedex.presentation.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.afauzi.pokedex.R
import com.afauzi.pokedex.databinding.ItemPokeLayoutBinding
import com.afauzi.pokedex.databinding.ItemSearchPokeBinding
import com.afauzi.pokedex.domain.entity.Pokemon
import com.afauzi.pokedex.utils.Helpers
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.util.*

class AdapterSearchPokePaging(
    private val context: Context,
    private val listenerPokeAdapter: ListenerPokeAdapter
): PagingDataAdapter<Pokemon, AdapterSearchPokePaging.ViewHolder>(PokeDiffComp) {
    object PokeDiffComp : DiffUtil.ItemCallback<Pokemon>() {
        override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
            return oldItem == newItem
        }

    }

    inner class ViewHolder(val binding: ItemSearchPokeBinding): RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(getItem(position)) {
                val parts = this?.url?.split("/")

                val name = this?.name
                val pokeId = parts?.get(parts.size - 2).toString()

                binding.pokeName.text = Helpers.capitalizeChar(name.toString())

                val img = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/${pokeId}.png"
                Glide.with(context)
                    .load(img)
                    .into(binding.pokeImage)

                binding.cardItem.setOnClickListener {
                    listenerPokeAdapter.onClickListenerAdapter(name ?: "", img)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchPokeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    interface ListenerPokeAdapter {
        fun onClickListenerAdapter(name: String, image: String)
    }
}