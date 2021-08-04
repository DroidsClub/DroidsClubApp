package com.example.androidclubapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.example.androidclubapp.connectors.PokeApiConnector
import com.example.androidclubapp.models.PokemonList
import com.example.androidclubapp.models.PokemonListItem


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class PokeDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.details_fragment, container, false)
    }

    fun getIdAndMakeApiCall(view: View, id: Int, connector: PokeApiConnector){
//        val pokemonText = view.findViewById<TextView>(R.id.pokemonIndex).text

        connector.doDetailsApiCall(view, id)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val connector: PokeApiConnector = PokeApiConnector()

        getIdAndMakeApiCall(view, 100, connector)

    }


}