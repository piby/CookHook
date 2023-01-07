package com.example.cookhook

import android.app.TaskStackBuilder
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DishActivity : AppCompatActivity() {

    private var mDishType: String? = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dish)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle: Bundle = intent.extras ?: return
        supportActionBar?.title = bundle.getString("name")
        mDishType = bundle.getString("type").toString()

        //var photoName = bundle.getString("photo")
        // TODO load photo

        val dishIngredientsTextView = findViewById<TextView>(R.id.dishIngredients)
        dishIngredientsTextView.text = bundle.getString("ingredients")

        val dishRecipeTextView = findViewById<TextView>(R.id.dishRecipe)
        dishRecipeTextView.text = bundle.getString("recipe")
    }

    override fun onSupportNavigateUp(): Boolean {
        val intent = Intent(this, ListActivity::class.java)
        intent.putExtra("type", mDishType)
        startActivity(intent)
        return true
    }
}