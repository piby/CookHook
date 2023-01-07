package com.example.cookhook

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import java.util.logging.Level


object StringValueSerializer : KSerializer<GenericComponentStringValue> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("GenericComponentStringValue")

    override fun deserialize(decoder: Decoder): GenericComponentStringValue {
        require(decoder is JsonDecoder)
        val element = decoder.decodeJsonElement()
        return GenericComponentStringValue(element.jsonPrimitive.content)
    }

    override fun serialize(encoder: Encoder, value: GenericComponentStringValue) {
        encoder.encodeString(value.value)
    }
}

object IntValueSerializer : KSerializer<GenericComponentIntValue> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("GenericComponentIntValue")

    override fun deserialize(decoder: Decoder): GenericComponentIntValue {
        require(decoder is JsonDecoder)
        val element = decoder.decodeJsonElement()
        return GenericComponentIntValue(element.jsonPrimitive.int)
    }

    override fun serialize(encoder: Encoder, value: GenericComponentIntValue) {
        encoder.encodeInt(value.value)
    }
}

object FloatValueSerializer : KSerializer<GenericComponentFloatValue> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("GenericComponentFloatValue")

    override fun deserialize(decoder: Decoder): GenericComponentFloatValue {
        require(decoder is JsonDecoder)
        val element = decoder.decodeJsonElement()
        return GenericComponentFloatValue(element.jsonPrimitive.float)
    }

    override fun serialize(encoder: Encoder, value: GenericComponentFloatValue) {
        encoder.encodeFloat(value.value)
    }
}

object GenericComponentSerializer : JsonContentPolymorphicSerializer<GenericComponent>(GenericComponent::class) {
    override fun selectDeserializer(element: JsonElement) = when {
        element.jsonPrimitive.isString -> GenericComponentStringValue.serializer()
        "." in element.toString() -> GenericComponentFloatValue.serializer()
        else -> GenericComponentIntValue.serializer()
    }
}

@Serializable(with = GenericComponentSerializer::class)
sealed class GenericComponent

// https://stackoverflow.com/questions/64529032/deserialize-json-array-with-different-values-type-with-kotlinx-serialization-lib
@Serializable(with = StringValueSerializer::class)
class GenericComponentStringValue(val value: String) : GenericComponent()

@Serializable(with = IntValueSerializer::class)
class GenericComponentIntValue(val value: Int) : GenericComponent()

@Serializable(with = FloatValueSerializer::class)
class GenericComponentFloatValue(val value: Float) : GenericComponent()

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
    val ingredients: List<List<GenericComponent>>,
    val recipe: List<RecipeSection>,
    val categories: List<Int>
)

// UnitData
//     val id: Int,
//     val baseForm: String,
//     val fractionForm: String,
//     val fewForm: String,
//     val manyForm: String
//
// IngredientTypesData
//     val id: Int,
//     val name: String
//
// IngredientData
//     val id: Int,
//     val name: String,
//     val category: Int,
//     val defaultQuantity: Int,
//     val defaultUnit: Int,
//     val unused: String

@Serializable
data class Dishes(
    val units: List<List<GenericComponent>>,
    val categories: List<String>,
    val ingredient_types: List<List<GenericComponent>>,
    val ingredients: List<List<GenericComponent>>,
    val dishes: List<DishData>
)

class ListActivity : AppCompatActivity(), DishAdapter.OnItemClickListener {

    private lateinit var mDishesData: Dishes

    private var mDishNamesList: ArrayList<String> = ArrayList<String>()

    private var mDishType: String? = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            mDishType = bundle.getString("type")
        }

        val dishesMap = mapOf(
            "0" to R.string.breakfast,
            "1" to R.string.soup,
            "2" to R.string.dinner,
            "3" to R.string.dessert
        )
        dishesMap[mDishType]?.let { supportActionBar!!.setTitle(it) }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // https://github.com/Kotlin/kotlinx.serialization#android
        // https://kotlinlang.org/docs/serialization.html#example-json-serialization
        val dishesBufferReader = application.assets.open("dishes.json").bufferedReader()
        val dishesJsonData = dishesBufferReader.use {
            it.readText()
        }

        mDishesData = Json.decodeFromString<Dishes>(dishesJsonData)
        for (dish in mDishesData.dishes) {
            if (dish.meal == mDishType) {
                mDishNamesList.add(dish.name)
            }
        }

        // https://gist.github.com/codinginflow/d1f55000c62a82d998234730267b3e0a
        val dishAdapter = DishAdapter(mDishNamesList!!, this)
        val recyclerView = findViewById<RecyclerView>(R.id.dishRecyclerView)
        recyclerView.adapter = dishAdapter
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.setHasFixedSize(true)
    }

    override fun onItemClick(position: Int) {
        val clickedDishName = mDishNamesList?.get(position)
        val intent = Intent(this, DishActivity::class.java)

        // find clicked dish
        for (dish in mDishesData.dishes) {
            if (dish.name == clickedDishName) {

                // prepare ingredients string from dish.ingredients
                var ingredients: String? = ""
                for (dishIngredient in dish.ingredients) {
                    val unitID = dishIngredient[0] as GenericComponentIntValue
                    val ingredientID = dishIngredient[1] as GenericComponentIntValue
                    val quantity = dishIngredient[2] as GenericComponentStringValue

                    val globalUnit = mDishesData.units[unitID.value - 1]
                    var unitName = globalUnit[1]

                    // select proper form of unit based on quantity
                    var quantityValue = quantity.value
                    val quantityAsDouble = quantity.value.toDouble()
                    val quantityAsInt = quantityAsDouble.toInt()
                    if ((quantityAsDouble - quantityAsInt.toDouble()) > 0.0) {
                        unitName = globalUnit[2]        // fraction
                    }
                    else {
                        quantityValue = quantityAsInt.toString()
                        if (quantityAsInt > 5) {
                            unitName = globalUnit[4]    // more than 5
                        }
                        else if (quantityAsInt > 1) {
                            unitName = globalUnit[3]    // 2, 3 or 4
                        }
                    }
                    unitName = unitName as GenericComponentStringValue

                    val globalIngredient = mDishesData.ingredients[ingredientID.value - 1]
                    val ingredientName = globalIngredient[1] as GenericComponentStringValue

                    ingredients += ingredientName.value
                    ingredients += ": $quantityValue "
                    ingredients += unitName.value
                    ingredients += "\n"
                }

                // prepare recipe string; (name, points) pairs
                var recipe: String? = ""
                for (dishRecipe in dish.recipe) {

                    if (dishRecipe.name != "opis") {
                        recipe += dishRecipe.name + "\n"
                    }

                    var pointCounter = 1
                    for (pointText in dishRecipe.points) {
                        recipe += "$pointCounter. $pointText\n"
                        pointCounter++
                    }
                }
                intent.putExtra("name", clickedDishName)
                intent.putExtra("type", mDishType)
                intent.putExtra("photo", dish.photo)
                intent.putExtra("ingredients", ingredients)
                intent.putExtra("recipe", recipe)

                startActivity(intent)
                break
            }
        }
    }
}
