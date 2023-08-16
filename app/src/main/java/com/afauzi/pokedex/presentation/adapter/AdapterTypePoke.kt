package com.afauzi.pokedex.presentation.adapter
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.afauzi.pokedex.databinding.ItemTypePokeBinding
import com.afauzi.pokedex.domain.entity.PokeDetail
import com.afauzi.pokedex.domain.entity.PokemonList
import com.afauzi.pokedex.domain.entity.Type
import com.afauzi.pokedex.domain.entity.TypesItem
import com.afauzi.pokedex.utils.Helpers

class AdapterTypePoke(
    private val items: ArrayList<TypesItem?>
): RecyclerView.Adapter<AdapterTypePoke.ViewHolder>() {
    class ViewHolder(val binding: ItemTypePokeBinding): RecyclerView.ViewHolder(binding.itemTypePoke)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTypePokeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(items[position]) {
                binding.itemTypePoke.text = Helpers.capitalizeChar(this?.type?.name.toString())
            }
        }
    }

    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<TypesItem?>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

}