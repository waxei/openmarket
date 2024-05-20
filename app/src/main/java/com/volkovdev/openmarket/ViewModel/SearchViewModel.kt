package com.volkovdev.openmarket.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.volkovdev.openmarket.Activity.MainActivity

class SearchViewModel : ViewModel() {
    val items = MutableLiveData<List<Pair<String, Boolean>>>()

    init {
        items.value = listOf(
            Pair("Вайлберис", false),
            Pair("Озон", false),
            Pair("Яндекс Маркет", false)
        )
    }

    fun updateItemChecked(position: Int, checked: Boolean) {
        val currentItems = items.value.orEmpty().toMutableList()
        currentItems[position] = Pair(currentItems[position].first, checked)
        items.value = currentItems
    }
}