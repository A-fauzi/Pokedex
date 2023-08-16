package com.afauzi.pokedex.presentation.adapter
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.afauzi.pokedex.databinding.ItemAbilityPokeBinding
import com.afauzi.pokedex.databinding.ItemTypePokeBinding
import com.afauzi.pokedex.domain.entity.AbilitiesItem
import com.afauzi.pokedex.domain.entity.TypesItem
import com.afauzi.pokedex.utils.Helpers

class AdapterAbilitiesPoke(
    private val listener: AdapterAbilityListener,
    private val items: ArrayList<AbilitiesItem?>
): RecyclerView.Adapter<AdapterAbilitiesPoke.ViewHolder>() {
    class ViewHolder(val binding: ItemAbilityPokeBinding): RecyclerView.ViewHolder(binding.itemAbilityPoke)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAbilityPokeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(items[position]) {
                binding.itemAbilityPoke.text = Helpers.capitalizeChar(this?.ability?.name.toString())
                binding.itemAbilityPoke.setOnClickListener {
                    listener.onclickListenerAbility(this?.ability?.name.toString())
                }
            }
        }
    }

    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<AbilitiesItem?>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    interface AdapterAbilityListener {
        fun onclickListenerAbility(name: String)
    }
}