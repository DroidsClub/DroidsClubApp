package com.example.androidclubapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidclubapp.connectors.PokeApiConnector
import com.example.androidclubapp.models.PokemonViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ManageFavorites : Fragment() {

    private val pokemonViewModel: PokemonViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.manage_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set up the RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.yourFavoritesList)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        val adapter: FavoritesAdapter = FavoritesAdapter(pokemonViewModel, getResources(), this, PokeApiConnector(pokemonViewModel,resources))
        recyclerView.adapter = adapter

        view.findViewById<ImageView>(R.id.addNewFavorite).setOnClickListener {
            adapter.addNewFavorite(1)
        }
    }
}