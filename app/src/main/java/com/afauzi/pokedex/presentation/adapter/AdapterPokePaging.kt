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
import com.afauzi.pokedex.domain.entity.Pokemon
import com.afauzi.pokedex.utils.Helpers
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.util.*

/**
 * Adapter khusus untuk menampilkan daftar Pokemon dengan Paging 3.
 *
 * @param context Context dari aplikasi.
 * @param listenerPokeAdapter Listener yang mendengarkan aksi klik pada item adapter.
 */
class AdapterPokePaging(
    private val context: Context,
    private val listenerPokeAdapter: ListenerPokeAdapter
) : PagingDataAdapter<Pokemon, AdapterPokePaging.ViewHolder>(PokeDiffComp) {

    // Objek yang mengimplementasikan DiffUtil.ItemCallback untuk membandingkan item.
    object PokeDiffComp : DiffUtil.ItemCallback<Pokemon>() {
        override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
            return oldItem == newItem
        }
    }

    // ViewHolder yang mengikat tampilan item Pokemon.
    inner class ViewHolder(val binding: ItemPokeLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    // Mengikat data Pokemon ke tampilan ViewHolder.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(getItem(position)) {
                val parts = this?.url?.split("/")
                val name = this?.name
                val pokeId = parts?.get(parts.size - 2).toString()

                binding.characterName.text = Helpers.capitalizeChar(name.toString())
                Glide.with(context)
                    .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/${pokeId}.png")
                    .into(binding.itemImgPoke)

                // Palette Color
                Glide.with(context)
                    .asBitmap()
                    .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/${pokeId}.png")
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            Palette.from(resource).generate { palette ->
                                palette?.let {
                                    // Mendapatkan warna yang Anda inginkan dari objek Palette, misalnya warna dominan
                                    val dominantColor = palette.getDominantColor(ContextCompat.getColor(context, R.color.blue))

                                    // Gunakan warna yang diambil untuk mengatur tampilan UI Anda
                                    if (dominantColor != null) {
                                        binding.llBgCard.setBackgroundColor(dominantColor)
                                    }
                                }
                            }
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                            // Do nothing or handle placeholder
                        }
                    })

                binding.cardItem.setOnClickListener {
                    listenerPokeAdapter.onClickListenerAdapter(name ?: "")
                }
            }
        }
    }

    // Membuat ViewHolder sesuai dengan tampilan item.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPokeLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // Interface untuk mendengarkan aksi klik pada item adapter.
    interface ListenerPokeAdapter {
        fun onClickListenerAdapter(name: String)
    }
}
