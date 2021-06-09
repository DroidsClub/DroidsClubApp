package com.example.androidclubapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import com.android.volley.Response
import com.example.androidclubapp.connectors.PokeApiConnector
import com.example.androidclubapp.models.PokemonList
import com.example.androidclubapp.models.PokemonListItem

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class PokeSearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val connector: PokeApiConnector = PokeApiConnector()

        var pokemonList: PokemonList = PokemonList(emptyArray())

        val listener = Response.Listener<PokemonList> { pokemon ->
            pokemonList = pokemon
        }

        connector.search(view, listener)

        val textBox = view.findViewById<EditText>(R.id.pokeSearchInput)

        textBox.doAfterTextChanged {

            val matchedPokemon: List<PokemonListItem> = pokemonList.results.filter {
                it.name.contains(textBox.text)
            }

            view.findViewById<TextView>(R.id.pokeSearchResults).text = matchedPokemon.map{ it.name }.toString()

        }
    }
}