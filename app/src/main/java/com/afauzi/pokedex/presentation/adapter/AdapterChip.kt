package com.afauzi.pokedex.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AdapterChip<T>(
    private val dataList: ArrayList<T>,
    private val itemLayout: Int,
    private val bindCallBack: (View, T) -> Unit
): RecyclerView.Adapter<AdapterChip.ViewHolder<T>>(){
    class ViewHolder<U>(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> {
        val view = LayoutInflater.from(parent.context).inflate(itemLayout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
        val item = dataList[position]
        bindCallBack(holder.itemView, item)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<T>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }
}