package com.example.cookhook

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*


object StringValueSerializer : KSerializer<IngredientComponentStringValue> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("IngredientComponentStringValue")

    override fun deserialize(decoder: Decoder): IngredientComponentStringValue {
        require(decoder is JsonDecoder)
        val element = decoder.decodeJsonElement()
        return IngredientComponentStringValue(element.jsonPrimitive.content)
    }

    override fun serialize(encoder: Encoder, value: IngredientComponentStringValue) {
        encoder.encodeString(value.value)
    }
}

object IntValueSerializer : KSerializer<IngredientComponentIntValue> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("IngredientComponentIntValue")

    override fun deserialize(decoder: Decoder): IngredientComponentIntValue {
        require(decoder is JsonDecoder)
        val element = decoder.decodeJsonElement()
        return IngredientComponentIntValue(element.jsonPrimitive.int)
    }

    override fun serialize(encoder: Encoder, value: IngredientComponentIntValue) {
        encoder.encodeInt(value.value)
    }
}

object IngredientComponentSerializer : JsonContentPolymorphicSerializer<IngredientComponent>(IngredientComponent::class) {
    override fun selectDeserializer(element: JsonElement) = when {
        element.jsonPrimitive.isString -> IngredientComponentStringValue.serializer()
        else -> IngredientComponentIntValue.serializer()
    }
}

@Serializable(with = IngredientComponentSerializer::class)
sealed class IngredientComponent

// https://stackoverflow.com/questions/64529032/deserialize-json-array-with-different-values-type-with-kotlinx-serialization-lib
@Serializable(with = StringValueSerializer::class)
class IngredientComponentStringValue(val value: String) : IngredientComponent()

@Serializable(with = IntValueSerializer::class)
class IngredientComponentIntValue(val value: Int) : IngredientComponent()

@Serializable
data class RecipeSection(
    val points: List<String>,
    val name: String
)

@Serializable
data class DishData(
    val name: String,
    val meal: String,
    val photo: String,
    val ingredients: List<List<IngredientComponent>>,
    val recipe: List<RecipeSection>,
    val categories: List<Int>
)

@Serializable
data class Dishes(
    val dishes: List<DishData>
)

class ListActivity : AppCompatActivity(), DishAdapter.OnItemClickListener {

    private var mItems: ArrayList<String> = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        var dishType: String? = ""
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            dishType = bundle.getString("type")
        }

        val actionBar = supportActionBar
        actionBar!!.title = dishType
        actionBar.setDisplayHomeAsUpEnabled(true)

        // https://github.com/Kotlin/kotlinx.serialization#android
        // https://kotlinlang.org/docs/serialization.html#example-json-serialization
        val bufferReader = application.assets.open("dishes.json").bufferedReader()
        val dishesJsonData = bufferReader.use {
            it.readText()
        }

        Log.i("info", "1 test")
        val dishesArray = Json.decodeFromString<Dishes>(dishesJsonData)
        Log.i("info", "2 test")
        for (dish in dishesArray.dishes) {
            if (dish.meal == "0") {                 // TODO we need to pass number not string from MainActivity
                mItems.add(dish.name)
            }
        }

        // https://gist.github.com/codinginflow/d1f55000c62a82d998234730267b3e0a
        val dishAdapter = DishAdapter(mItems!!, this)
        val recyclerView = findViewById<RecyclerView>(R.id.dishRecyclerView)
        recyclerView.adapter = dishAdapter
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.setHasFixedSize(true)
    }

    override fun onItemClick(position: Int) {
        val clickedItem = mItems?.get(position)
        Toast.makeText(this, "-> $clickedItem <-", Toast.LENGTH_SHORT).show()
        //clickedItem.text1 = "Clicked"
        //adapter.notifyItemChanged(position)
    }
}
