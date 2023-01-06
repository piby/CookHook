package com.example.cookhook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class DishActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dish)

        var photoName: String? = ""
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            photoName = bundle.getString("photo")

            val dishNameTextView = findViewById<TextView>(R.id.dishName)
            dishNameTextView.text = bundle.getString("name")

            // TODO load photo

            val dishIngredientsTextView = findViewById<TextView>(R.id.dishIngredients)
            dishIngredientsTextView.text = bundle.getString("ingredients")

            val dishRecipeTextView = findViewById<TextView>(R.id.dishRecipe)
            dishRecipeTextView.text = bundle.getString("recipe")
        }

        val actionBar = supportActionBar
        actionBar?.title = "Danie"
        actionBar?.setDisplayHomeAsUpEnabled(true)

        // TODO display photo and print all text
    }
}