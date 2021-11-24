package com.example.androidclubapp

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.androidclubapp.connectors.PokeApiConnector
import com.example.androidclubapp.models.PokemonListItem
import com.example.androidclubapp.models.PokemonViewModel

class SearchViewAdapter(private val dataSet: List<PokemonListItem>, val pokemonViewModel: PokemonViewModel, val resources: Resources, val fragment: Fragment) :

    RecyclerView.Adapter<SearchViewAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val imageView: ImageView
        val fullView: View

        init {
            // Define click listener for the ViewHolder's View.
            textView = view.findViewById(R.id.foundPokemonName)
            imageView = view.findViewById(R.id.foundPokemonImage)
            fullView = view
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.pokemon_found_view, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView.text = dataSet[position].name.capitalize()

        val connector: PokeApiConnector = PokeApiConnector(pokemonViewModel,resources)
        connector.doApiCallWithUrl(viewHolder.fullView,dataSet[position].url,
            { fragment.findNavController().navigate(R.id.action_to_Details) })
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}