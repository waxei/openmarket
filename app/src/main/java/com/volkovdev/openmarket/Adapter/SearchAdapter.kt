package com.volkovdev.openmarket.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.volkovdev.openmarket.R

class SearchAdapter(private var items: List<Pair<String, Boolean>>,
                    private val onItemChecked: (Int, Boolean) -> Unit) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)
        return SearchViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val currentItem = items[position]

        holder.textView.text = currentItem.first
        holder.checkBox.isChecked = currentItem.second

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            onItemChecked(position, isChecked)
        }
    }

    fun updateItems(updatedItems: List<Pair<String, Boolean>>) {
        items = updatedItems
        notifyDataSetChanged()
    }

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.itemText)
        val checkBox: CheckBox = itemView.findViewById(R.id.itemCheckbox)
    }
}