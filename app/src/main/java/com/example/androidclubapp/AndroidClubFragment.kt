package com.example.androidclubapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.androidclubapp.connectors.PokeApiConnector
import com.example.androidclubapp.models.Pokemon
import com.example.androidclubapp.models.PokemonInfo
import com.example.androidclubapp.models.PokemonViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class AndroidClubFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.android_club_fragment, container, false)
    }

    private val pokemonViewModel: PokemonViewModel by activityViewModels()

    fun getIdAndMakeApiCall(view: View, next: Boolean, connector: PokeApiConnector) {
        val pokemonText = view.findViewById<TextView>(R.id.pokemonIndex).text

        val id = pokemonText.drop(1).take(3)

        val idAsInt = id.toString().toIntOrNull()

        if (idAsInt != null) {

            val idForAPI = if (next) idAsInt + 1 else idAsInt - 1
            pokemonViewModel.selectPokemon(idForAPI)
            connector.doApiCall(view, idForAPI)
        } else {
            pokemonViewModel.selectPokemon(1)
            connector.doApiCall(view, 1)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val connector: PokeApiConnector = PokeApiConnector()

        view.findViewById<Button>(R.id.button_next).setOnClickListener {
            getIdAndMakeApiCall(view,true, connector)
        }

        view.findViewById<Button>(R.id.button_previous).setOnClickListener {
            getIdAndMakeApiCall(view,false, connector)
        }

        view.findViewById<Button>(R.id.buttonSearch).setOnClickListener {
            findNavController().navigate(R.id.action_Home_to_Search)
        }

        view.findViewById<ImageView>(R.id.pokemonImage).setOnClickListener {
            findNavController().navigate(R.id.action_Home_to_Details)
        }

//        view.findViewById<Button>(R.id.action_Search).setOnClickListener {
//            findNavController().navigate(R.id.action_Home_to_Search)
//        }
    }
}