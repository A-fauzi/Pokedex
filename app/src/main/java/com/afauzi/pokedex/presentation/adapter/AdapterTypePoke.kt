package com.afauzi.pokedex.presentation.adapter
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.afauzi.pokedex.databinding.ItemTypePokeBinding
import com.afauzi.pokedex.domain.entity.TypesItem
import com.afauzi.pokedex.utils.Helpers

/**
 * Adapter khusus untuk menampilkan daftar tipe Pokemon.
 *
 * @param items Daftar tipe Pokemon yang akan ditampilkan dalam RecyclerView.
 */
class AdapterTypePoke(
    private val items: ArrayList<TypesItem?>
) : RecyclerView.Adapter<AdapterTypePoke.ViewHolder>() {

    // ViewHolder yang mengikat item tampilan untuk daftar tipe Pokemon.
    class ViewHolder(val binding: ItemTypePokeBinding) : RecyclerView.ViewHolder(binding.itemTypePoke)

    // Membuat ViewHolder sesuai dengan tampilan item.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTypePokeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // Mengikat data tipe Pokemon ke tampilan ViewHolder.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(items[position]) {
                binding.itemTypePoke.text = Helpers.capitalizeChar(this?.type?.name.toString())
            }
        }
    }

    // Mengembalikan jumlah item dalam daftar.
    override fun getItemCount() = items.size

    // Fungsi untuk mengubah data yang ditampilkan dalam adapter.
    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<TypesItem?>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }
}
