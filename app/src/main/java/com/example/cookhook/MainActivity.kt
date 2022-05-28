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
        //val intent = Intent(this, ListActivity::class.java)
        //intent.putExtra("type", "Breakfast")
        //startActivity(intent)

        Log.i("info", "Breakfast")
        Toast.makeText(this,"Breakfast", Toast.LENGTH_SHORT).show()
    }

    fun showSoupList(view: View?) {
        //val intent = Intent(this, ListActivity::class.java)
        //intent.putExtra("type","Soup")
        //startActivity(intent)

        Log.i("info", "Soup")
        Toast.makeText(this,"Soup", Toast.LENGTH_SHORT).show()
    }

    fun showDinnerList(view: View?) {
        //val intent = Intent(this, ListActivity::class.java)
        //intent.putExtra("type","Dinner")
        //startActivity(intent)

        Log.i("info", "Dinner")
        Toast.makeText(this,"Dinner", Toast.LENGTH_SHORT).show()
    }

    fun showDesertList(view: View?) {
        //val intent = Intent(this, ListActivity::class.java)
        //intent.putExtra("type","Desert")
        //startActivity(intent)

        Log.i("info", "Desert")
        Toast.makeText(this,"Desert", Toast.LENGTH_SHORT).show()
    }
}