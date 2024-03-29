package com.example.androidclubapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.example.androidclubapp.connectors.PokeApiConnector
import com.example.androidclubapp.models.PokemonList
import com.example.androidclubapp.models.PokemonListItem
import com.example.androidclubapp.models.PokemonViewModel


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

    private val pokemonViewModel: PokemonViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val connector: PokeApiConnector = PokeApiConnector(pokemonViewModel,resources)

        var pokemonList: PokemonList = PokemonList(emptyArray())

        val listener = Response.Listener<PokemonList> { pokemon ->
            pokemonList = pokemon
        }

        connector.search(view, listener)

        val textBox = view.findViewById<EditText>(R.id.pokeSearchInput)

        textBox.doAfterTextChanged {

            val matchedPokemon: List<PokemonListItem> = pokemonList.results.filter {
                it.name.contains(textBox.text.toString().toLowerCase())
            }

            // set up the RecyclerView
            val recyclerView: RecyclerView = view.findViewById(R.id.pokeSearchResults)
            recyclerView.layoutManager = LinearLayoutManager(view.context)
            val adapter: SearchViewAdapter = SearchViewAdapter(matchedPokemon,pokemonViewModel,resources, this)
            recyclerView.adapter = adapter
        }

        view.findViewById<Button>(R.id.backButton).setOnClickListener {
            findNavController().navigate(R.id.action_Search_to_Home)
        }
    }

    fun doingSomething(pokemon: List<PokemonListItem>, view: SearchViewAdapter.ViewHolder){
        view.textView.text = "Done the function"
    }


}