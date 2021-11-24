package com.example.androidclubapp

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import com.example.androidclubapp.connectors.PokeApiConnector
import com.example.androidclubapp.models.FavoritePokemon
import com.example.androidclubapp.models.PokemonViewModel
import com.example.androidclubapp.utils.CommonMethods.makeTextViewHorizontalScroll
import com.example.androidclubapp.utils.CommonMethods.toEditable

class FavoritesAdapter(private val pokemonViewModel: PokemonViewModel, val resources: Resources, val lifecycleOwner: LifecycleOwner, val connector: PokeApiConnector) :

    RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {
        val favName: TextView
        val favId: TextView
        val favSave: AppCompatImageView
        val favEdit: AppCompatImageView
        val favEditText: AppCompatEditText
        val favDelete: AppCompatImageView
        val fullView: View

        init {
            // Define click listener for the ViewHolder's View.
            favName = view.findViewById(R.id.favoriteViewName)
            favId = view.findViewById(R.id.favoriteId)
            favSave = view.findViewById(R.id.favoriteEditSave)
            favEdit = view.findViewById(R.id.favoriteEdit)
            favEditText = view.findViewById(R.id.favoriteIdEditText)
            favDelete = view.findViewById(R.id.favoriteDelete)
            fullView = view
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.favorite_view, viewGroup, false)

        return ViewHolder(view)
    }

    private var position = -1

    fun getPosition(): Int {
        return position
    }

    fun setPosition(position: Int) {
        this.position = position
    }

    fun addNewFavorite(id: Int){
        pokemonViewModel.saveFavorite(id)
        this.notifyDataSetChanged()
      //  this.notifyItemRangeChanged(0,this.itemCount)
    }

    fun saveFavorite(viewHolder: ViewHolder){

        val id: Int = viewHolder.favEditText.text.toString().filter { it.isDigit() }.toInt()

        pokemonViewModel.removeFavoriteById(viewHolder.favId.text.toString().filter { it.isDigit() }.toInt())
        pokemonViewModel.updateFavorite(FavoritePokemon(id))

        connector.getPokemon(viewHolder.fullView, id)

        viewHolder.favEditText.text = id.toString().toEditable()
        viewHolder.favEditText.visibility = INVISIBLE
        viewHolder.favName.visibility = VISIBLE

        this.notifyItemRemoved(position)
        this.notifyItemRangeChanged(position, this.itemCount)
    }

    fun editFavorite(viewHolder: ViewHolder){
        val visibility = viewHolder.favEditText.visibility

        if(visibility == INVISIBLE){
            viewHolder.favEditText.visibility = VISIBLE
            viewHolder.favId.visibility = INVISIBLE
            viewHolder.favEditText.text = viewHolder.favId.text.toString().toEditable()
        } else {
            viewHolder.favEditText.text = "".toEditable()
            viewHolder.favEditText.visibility = INVISIBLE
            viewHolder.favId.visibility = VISIBLE
        }
    }

    fun deleteFavorite(){
        if (position == this.itemCount - 1) {
            pokemonViewModel.removeFavoriteIfExistsForIndex(position)
            this.notifyItemRemoved(position)
        } else {
            pokemonViewModel.removeFavoriteIfExistsForIndex(position)
            this.notifyItemRemoved(position)
            this.notifyItemRangeChanged(position,this.itemCount)
        }
    }


    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val drawable = resources.getDrawable(R.drawable.rounded_button_blue)
        drawable.setTint(resources.getColor(R.color.water))

        viewHolder.fullView.findViewById<GridLayout>(R.id.favoriteViewLayout).background = drawable

        fun populateFavoritesAdapter(favorites: List<FavoritePokemon>, viewHolder: ViewHolder) {

            println("amount of favorites " + favorites.size.toString() + ", last index = " + favorites.lastIndex + ", position - " + position)

            if(favorites.lastIndex != -1){
                try {
                    connector.getPokemon(viewHolder.fullView, favorites[position].id)

                    makeTextViewHorizontalScroll(viewHolder.favName)

                    viewHolder.favDelete.setOnClickListener{
                        setPosition(position)
                        deleteFavorite()
                    }

                    viewHolder.favEditText.doAfterTextChanged {

                        val currentText = viewHolder.favEditText.text.toString()

                        if(currentText != favorites[position].id.toString() && !currentText.isNullOrBlank()){
                            viewHolder.favSave.visibility = VISIBLE
                            viewHolder.favEdit.visibility = INVISIBLE
                        } else {
                            viewHolder.favEdit.visibility = VISIBLE
                            viewHolder.favSave.visibility = INVISIBLE
                        }
                    }

                    viewHolder.favEdit.setOnClickListener{

                        println("clicked edit")
                        setPosition(position)
                        editFavorite(viewHolder)
                    }

                    viewHolder.favSave.setOnClickListener {

                        println("clicked save")
                        setPosition(position)
                        saveFavorite(viewHolder)
                        viewHolder.favEdit.visibility = VISIBLE
                        viewHolder.favSave.visibility = INVISIBLE
                    }

                } catch (exception: IndexOutOfBoundsException) {
                    println("Got an exception from: " + position)
                }
            }
        }

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        pokemonViewModel.doSomethingWithFavorites(lifecycleOwner, viewHolder, ::populateFavoritesAdapter)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = run {

        var count = 0

        pokemonViewModel.favoritePokemon.observe(lifecycleOwner) { favorites ->
            count = favorites.size
        }

        count
    }
}