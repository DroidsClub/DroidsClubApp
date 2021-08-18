package com.example.androidclubapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.androidclubapp.connectors.PokeApiConnector
import com.example.androidclubapp.models.PokemonViewModel


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

    private val pokemonViewModel: PokemonViewModel by activityViewModels()

    fun getIdAndMakeApiCall(view: View, connector: PokeApiConnector, savedInstanceState: Bundle?){

        val id: Int? = savedInstanceState?.getInt("pokemonIndex")

        println("BUNDLE " + savedInstanceState)
        println("ID FROM BUNDLE " + id)

        println(pokemonViewModel.selectedPokemon.value)

        val idFromModel = pokemonViewModel.selectedPokemon.value?.id ?: 1

        connector.doDetailsApiCall(view, idFromModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val connector: PokeApiConnector = PokeApiConnector()
        getIdAndMakeApiCall(view, connector,savedInstanceState)

        view.findViewById<Button>(R.id.detailsBackButton).setOnClickListener {
            findNavController().navigate(R.id.action_Details_to_Home)
        }

    }


}