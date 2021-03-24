package com.example.androidclubapp.connectors

import android.view.View
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.androidclubapp.R
import com.example.androidclubapp.models.Pokemon
import com.google.gson.Gson

class PokeApiConnector {

    fun logger(message: String){
        println(message)
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

                val viewId = "00${pokemon.id}".takeLast(3)

                view.findViewById<TextView>(R.id.pokeResponse).text = "$viewId ${pokemon?.name}"
            },
            Response.ErrorListener { error ->
                // TODO: Handle error
            }
        )

        queue.add(request)
    }
}