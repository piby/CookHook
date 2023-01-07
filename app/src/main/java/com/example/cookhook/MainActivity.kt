package com.example.cookhook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun showBreakfastList(view: View?) {
        showDishList("0")
    }

    fun showSoupList(view: View?) {
        showDishList("1")
    }

    fun showDinnerList(view: View?) {
        showDishList("2")
    }

    fun showDessertList(view: View?) {
       showDishList("3")
    }

    private fun showDishList(type: String) {
        val intent = Intent(this, ListActivity::class.java)
        intent.putExtra("type", type)
        startActivity(intent)
    }
}