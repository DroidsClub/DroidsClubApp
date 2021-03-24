package com.example.androidclubapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.androidclubapp.connectors.PokeApiConnector

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val connector = PokeApiConnector()

        view.findViewById<Button>(R.id.button_first).setOnClickListener {

            val pokemonText = view.findViewById<TextView>(R.id.pokeResponse).text

            val id = pokemonText.take(3)

            val idAsInt = id.toString().toIntOrNull()

            if(idAsInt != null){
                connector.doApiCall(view, idAsInt + 1)
            } else {
                connector.doApiCall(view, 1)
            }
        }
    }
}