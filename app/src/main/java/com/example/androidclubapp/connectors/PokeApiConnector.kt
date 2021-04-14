package com.example.androidclubapp.connectors

import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.androidclubapp.R
import com.example.androidclubapp.models.Pokemon

class PokeApiConnector {

    fun logger(message: String){
        println(message)
    }

    fun changeColor(button: Button, color: Int){

        val drawableButton: LayerDrawable = button.background as LayerDrawable
        val layerGradientDrawable: GradientDrawable = drawableButton.findDrawableByLayerId(R.id.roundedButton) as GradientDrawable

        layerGradientDrawable.setColor(color)
    }

    fun typeToColor(view: View, pokemonType: String): Int {
        return when (pokemonType){
            "fire" -> view.resources.getColor(R.color.fire)
            "electric" -> view.resources.getColor(R.color.electric)
            "water" -> view.resources.getColor(R.color.water)
            else -> view.resources.getColor(R.color.fire)
        }
    }

    fun doApiCall(view: View, index: Int) {
        val url = "https://pokeapi.co/api/v2/pokemon/$index"

        val queue = Volley.newRequestQueue(view.context)

        val request = GsonRequest(
            url,
            Pokemon::class.java,
            null,
            Response.Listener { pokemon ->

                logger("Pokemon: $pokemon")

                val viewId = "#" + "00${pokemon.id}".takeLast(3)

                DownloadImageFromInternet(view.findViewById(R.id.pokemonImage)).execute(pokemon.sprites.other.`official-artwork`.front_default)

                view.findViewById<TextView>(R.id.pokemonName).text = pokemon?.name.toString().capitalize()
                view.findViewById<TextView>(R.id.pokeResponse).text = viewId

                if(pokemon.types.size > 1){
                    view.findViewById<Button>(R.id.type).text = pokemon.types[0].type.name
                    view.findViewById<Button>(R.id.type2).text = pokemon.types[1].type.name
                    view.findViewById<Button>(R.id.type2).visibility = View.VISIBLE

                    val button = view.findViewById<Button>(R.id.type)
                    val button2 = view.findViewById<Button>(R.id.type2)

                    val color: Int = typeToColor(view, pokemon.types[0].type.name)
                    val color2 = typeToColor(view, pokemon.types[1].type.name)

                    changeColor(button, color)
                    changeColor(button2, color2)

                } else {

                    val button = view.findViewById<Button>(R.id.type)

                    view.findViewById<Button>(R.id.type).text = pokemon.types[0].type.name
                    view.findViewById<Button>(R.id.type2).visibility = View.GONE

                    val color: Int = typeToColor(view, pokemon.types[0].type.name)
                    changeColor(button, color)

                }

            },
            Response.ErrorListener { error ->
                // TODO: Handle error
            }
        )

        queue.add(request)
    }
}