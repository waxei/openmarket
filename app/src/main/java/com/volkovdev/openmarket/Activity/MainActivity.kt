package com.volkovdev.openmarket.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.volkovdev.openmarket.Adapter.SearchAdapter
import com.volkovdev.openmarket.R
import com.volkovdev.openmarket.ViewModel.SearchViewModel
import com.volkovdev.openmarket.services.ProxyService

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val intent = Intent(this, ProxyService::class.java)

        val button:Button = findViewById(R.id.searchButton)
        button.setOnClickListener {
            Log.d("Main","Trying to start...")
            startService(intent)
            val vibratorService = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibratorService.vibrate(200)

        }

        val stop:Button = findViewById(R.id.stopProxy)
        stop.setOnClickListener{
            stopService(intent)
            Log.d("Main","Service stopped")
        }


//---------------------------------------------------------------------------------------


        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        // RecyclerView и Adapter
        val recyclerView = findViewById<RecyclerView>(R.id.searchRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = SearchAdapter(viewModel.items.value.orEmpty(), viewModel::updateItemChecked)
        recyclerView.adapter = adapter

        // Настройка кнопки
        val imageButton = findViewById<ImageButton>(R.id.search_button)
        imageButton.setOnClickListener {
            recyclerView.visibility = if (recyclerView.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }

        // Наблюдение за изменениями данных
        viewModel.items.observe(this, Observer {
            adapter.updateItems(it)
        })

//-------------------------**************************------------------------------------

            // Настройка кнопки test
            val testButton = findViewById<Button>(R.id.test)
        testButton.setOnClickListener {
            handleCheckboxes()
        }
    }

    // Метод для проверки состояния чекбоксов
    private fun handleCheckboxes() {
        val checkedItems = mutableListOf<String>()
        viewModel.items.value?.forEach {
            if (it.second) {
                checkedItems.add(it.first)
            }
        }

        val message = if (checkedItems.isEmpty()) {
            "Нет выбранных элементов"
        } else {
            "Выбранные элементы: ${checkedItems.joinToString(", ")}"
        }

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()


//---------------------------------------------------------------------------------------
        }
    data class SearchItem(
        val name: String,
        var active: Boolean,
        var visible: Boolean
    )}