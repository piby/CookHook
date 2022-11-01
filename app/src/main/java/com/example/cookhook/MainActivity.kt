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
        val intent = Intent(this, ListActivity::class.java)
        intent.putExtra("type", "0")
        startActivity(intent)
    }

    fun showSoupList(view: View?) {
        val intent = Intent(this, ListActivity::class.java)
        intent.putExtra("type","1")
        startActivity(intent)
    }

    fun showDinnerList(view: View?) {
        val intent = Intent(this, ListActivity::class.java)
        intent.putExtra("type","2")
        startActivity(intent)
   }

    fun showDesertList(view: View?) {
        val intent = Intent(this, ListActivity::class.java)
        intent.putExtra("type","3")
        startActivity(intent)
   }
}