package com.example.cookhook

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat

class DishActivity : AppCompatActivity() {

    private var mDishType: String? = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dish)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle: Bundle = intent.extras ?: return
        supportActionBar?.title = bundle.getString("name")
        mDishType = bundle.getString("type").toString()

        var photoName = bundle.getString("photo")
        if ((photoName != null) and (photoName?.length!! > 5)) {
            photoName = photoName.dropLast(4)
            val resourceId = resources.getIdentifier(photoName, "drawable", packageName)
            if (resourceId != 0) {
                val dishImageImageView = findViewById<ImageView>(R.id.dishImage)
                dishImageImageView.setImageResource(resourceId)
            }
        }

        val dishIngredientsTextView = findViewById<TextView>(R.id.dishIngredients)
        dishIngredientsTextView.text = bundle.getString("ingredients")

        val dishRecipeTextView = findViewById<TextView>(R.id.dishRecipe)
        dishRecipeTextView.text = HtmlCompat.fromHtml(bundle.getString("recipe").toString(), HtmlCompat.FROM_HTML_MODE_COMPACT)
    }

    override fun onSupportNavigateUp(): Boolean {
        val intent = Intent(this, ListActivity::class.java)
        intent.putExtra("type", mDishType)
        startActivity(intent)
        return true
    }
}