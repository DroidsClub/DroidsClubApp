package com.example.androidclubapp.connectors

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.androidclubapp.R

class PokeApiConnector {

    fun logger(message: String){
        println(message)
    }

    fun doApiCall(view: View) {
        val url = "https://pokeapi.co/api/v2/pokemon/1"

        val queue = Volley.newRequestQueue(view.context)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                logger("Response: %s".format(response.toString()))
                view.findViewById<TextView>(R.id.jsonResponse).text = "Response: %s".format(response.toString().take(50))
            },
            Response.ErrorListener { error ->
                // TODO: Handle error
            }
        )

        queue.add(jsonObjectRequest)
    }
}