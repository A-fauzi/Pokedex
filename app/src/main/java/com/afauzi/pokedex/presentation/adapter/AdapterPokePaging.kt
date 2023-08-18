package com.afauzi.pokedex.presentation.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afauzi.pokedex.R
import com.afauzi.pokedex.databinding.ItemPokeLayoutBinding
import com.afauzi.pokedex.domain.entity.Pokemon
import com.afauzi.pokedex.domain.entity.TypesItem
import com.afauzi.pokedex.utils.Helpers
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.util.*

/**
 * Adapter khusus untuk menampilkan daftar Pokemon dengan Paging 3.
 *
 * @param context Context dari aplikasi.
 */
class AdapterPokePaging(
    private val context: Context,
    private val itemLayout: Int,
    private val bindCallBack: (View, Pokemon) -> Unit
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
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    // Mengikat data Pokemon ke tampilan ViewHolder.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            bindCallBack(holder.itemView, item)
        } else {
            throw Exception("item null")
        }
    }

    // Membuat ViewHolder sesuai dengan tampilan item.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(itemLayout, parent, false)
        return ViewHolder(view)
    }
}
