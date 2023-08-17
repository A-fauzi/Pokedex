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

/**
 * Adapter khusus untuk menampilkan daftar kemampuan Pokemon.
 *
 * @param listener Listener untuk aksi klik pada item adapter.
 * @param items Daftar kemampuan Pokemon yang akan ditampilkan dalam RecyclerView.
 */
class AdapterAbilitiesPoke(
    private val listener: AdapterAbilityListener,
    private val items: ArrayList<AbilitiesItem?>
) : RecyclerView.Adapter<AdapterAbilitiesPoke.ViewHolder>() {

    // ViewHolder yang mengikat tampilan item kemampuan Pokemon.
    class ViewHolder(val binding: ItemAbilityPokeBinding) : RecyclerView.ViewHolder(binding.itemAbilityPoke)

    // Membuat ViewHolder sesuai dengan tampilan item.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAbilityPokeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // Mengikat data kemampuan Pokemon ke tampilan ViewHolder.
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

    // Mengembalikan jumlah item dalam daftar kemampuan Pokemon.
    override fun getItemCount() = items.size

    // Fungsi untuk mengubah data yang ditampilkan dalam adapter.
    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<AbilitiesItem?>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    // Interface untuk mendengarkan aksi klik pada item adapter.
    interface AdapterAbilityListener {
        fun onclickListenerAbility(name: String)
    }
}
