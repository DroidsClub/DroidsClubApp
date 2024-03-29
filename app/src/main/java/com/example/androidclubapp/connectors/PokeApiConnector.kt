package com.example.androidclubapp.connectors

import android.content.res.Resources
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.androidclubapp.R
import com.example.androidclubapp.models.Descriptions
import com.example.androidclubapp.models.Pokemon
import com.example.androidclubapp.models.PokemonList
import com.example.androidclubapp.models.PokemonViewModel

class PokeApiConnector(val pokemonViewModel: PokemonViewModel, val resources: Resources) {

    fun logger(message: String){
        println(message)
    }

    fun changeColor(button: TextView, color: Int){

        val drawableButton: LayerDrawable = button.background as LayerDrawable
        val layerGradientDrawable: GradientDrawable = drawableButton.findDrawableByLayerId(R.id.roundedButton) as GradientDrawable

        layerGradientDrawable.setTint(color)
    }

    fun typeToColor(view: View, pokemonType: String): Int {
        return when (pokemonType){
            "fire" -> view.resources.getColor(R.color.fire)
            "electric" -> view.resources.getColor(R.color.electric)
            "water" -> view.resources.getColor(R.color.water)
            "poison" -> view.resources.getColor(R.color.poison)
            "grass" -> view.resources.getColor(R.color.grass)
            "flying" -> view.resources.getColor(R.color.flying)
            "normal" -> view.resources.getColor(R.color.normal)
            "ground" -> view.resources.getColor(R.color.ground)
            "fairy" -> view.resources.getColor(R.color.fairy)
            "fighting" -> view.resources.getColor(R.color.fighting)
            "psychic" -> view.resources.getColor(R.color.psychic)
            "rock" -> view.resources.getColor(R.color.rock)
            "steel" -> view.resources.getColor(R.color.steel)
            "ice" -> view.resources.getColor(R.color.ice)
            "ghost" -> view.resources.getColor(R.color.ghost)
            "dragon" -> view.resources.getColor(R.color.dragon)
            "dark" -> view.resources.getColor(R.color.dark)
            else -> view.resources.getColor(R.color.fire)
        }
    }

    fun search(view: View, listener: Response.Listener<PokemonList>) {
        val url = "https://pokeapi.co/api/v2/pokemon?limit=898"

        val queue = Volley.newRequestQueue(view.context)

        val request = GsonRequest(
            url,
            PokemonList::class.java,
            null,
            listener,
            Response.ErrorListener { error ->
                // TODO: Handle error
            }
        )

        queue.add(request)
    }

    fun doApiCallWithUrl(view: View, url: String, f:() -> Unit) {
        val queue = Volley.newRequestQueue(view.context)

        val request = GsonRequest(
            url,
            Pokemon::class.java,
            null,
            Response.Listener { pokemon ->

                logger("Pokemon: $pokemon")

                pokemonViewModel.selectPokemon(pokemon.id.toInt())

                val viewId = "#" + "00${pokemon.id}".takeLast(3)
                DownloadImageFromInternet(view.findViewById(R.id.foundPokemonImage)).execute(pokemon.sprites.other.`official-artwork`.front_default)
                view.findViewById<TextView>(R.id.foundPokemonIndex).text = viewId

                view.findViewById<ImageView>(R.id.foundPokemonImage).setOnClickListener {
                    f()
                }
            },
            Response.ErrorListener { error ->
                // TODO: Handle error
            }
        )

        queue.add(request)
    }

    fun getPokemon(view: View, index: Int) {
        val url = "https://pokeapi.co/api/v2/pokemon/$index"

        val queue = Volley.newRequestQueue(view.context)

        val request = GsonRequest(
            url,
            Pokemon::class.java,
            null,
            Response.Listener { pokemon ->

                logger("Pokemon: $pokemon")


                view.findViewById<TextView>(R.id.favoriteId).text = resources.getString(R.string.favorite_id, pokemon.id)
                view.findViewById<TextView>(R.id.favoriteViewName).text = pokemon.name.capitalize()

            },
            Response.ErrorListener { error ->
                // TODO: Handle error
            }
        )

        queue.add(request)
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
                view.findViewById<TextView>(R.id.pokemonIndex).text = viewId

                when (pokemon.id.toInt()) {
                    1 -> view.findViewById<Button>(R.id.button_previous).visibility = View.GONE
                    898 -> view.findViewById<Button>(R.id.button_next).visibility = View.GONE
                    else -> {
                        view.findViewById<Button>(R.id.button_previous).visibility = View.VISIBLE
                        view.findViewById<Button>(R.id.button_next).visibility = View.VISIBLE
                    }
                }

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

    fun getPokemonDescription(view: View, url: String) {

        val queue = Volley.newRequestQueue(view.context)

        val request = GsonRequest(
            url,
            Descriptions::class.java,
            null,
            Response.Listener { pokemonDescriptions ->

                logger("Pokemon: $pokemonDescriptions")


                view.findViewById<TextView>(R.id.description).text = pokemonDescriptions.flavor_text_entries.find { it.language.name == "en"}?.flavor_text ?: "Not found"

            },
            Response.ErrorListener { error ->
                // TODO: Handle error
            }
        )

        queue.add(request)
    }

    fun doDetailsApiCall(view: View, index: Int) {
        val url = "https://pokeapi.co/api/v2/pokemon/$index"

        val queue = Volley.newRequestQueue(view.context)

        val request = GsonRequest(
            url,
            Pokemon::class.java,
            null,
            Response.Listener { pokemon ->

                logger("Pokemon: $pokemon")

                val viewId = "#" + "00${pokemon.id}".takeLast(3)

                DownloadImageFromInternet(view.findViewById(R.id.detailsPokemonImage)).execute(pokemon.sprites.other.`official-artwork`.front_default)

                view.findViewById<TextView>(R.id.detailsPokemonName).text = pokemon?.name.toString().capitalize()
                view.findViewById<TextView>(R.id.detailsPokemonIndex).text = viewId

                getPokemonDescription(view, pokemon.species.url)


                if(pokemon.types.size > 1){
                    view.findViewById<TextView>(R.id.detailsType).text = pokemon.types[0].type.name
                    view.findViewById<TextView>(R.id.detailsType2).text = pokemon.types[1].type.name
                    view.findViewById<TextView>(R.id.detailsType2).visibility = View.VISIBLE

                    val button = view.findViewById<TextView>(R.id.detailsType)
                    val button2 = view.findViewById<TextView>(R.id.detailsType2)

                    val color: Int = typeToColor(view, pokemon.types[0].type.name)
                    val color2 = typeToColor(view, pokemon.types[1].type.name)

                    changeColor(button, color)
                    changeColor(button2, color2)

                } else {

                    val button = view.findViewById<TextView>(R.id.detailsType)

                    view.findViewById<TextView>(R.id.detailsType).text = pokemon.types[0].type.name
                    view.findViewById<TextView>(R.id.detailsType2).visibility = View.GONE

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