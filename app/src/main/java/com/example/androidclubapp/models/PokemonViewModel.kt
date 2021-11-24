package com.example.androidclubapp.models

import androidx.lifecycle.*
import com.example.androidclubapp.FavoritesAdapter
import com.example.androidclubapp.SearchViewAdapter

class PokemonViewModel: ViewModel() {

    private val mutableSelectedPokemon = MutableLiveData<PokemonInfo>()
    private val mutableListPokemon = MutableLiveData<List<PokemonListItem>>()

    private val mutableFavorites = MutableLiveData<List<FavoritePokemon>>()

    val selectedPokemon: LiveData<PokemonInfo> get() = mutableSelectedPokemon
    val listPokemon: LiveData<List<PokemonListItem>> get() = mutableListPokemon
    val favoritePokemon: LiveData<List<FavoritePokemon>> get() = mutableFavorites

    fun initFavoritesList(): List<FavoritePokemon> = favoritePokemon.value ?: mutableListOf()

    fun selectPokemon(id: Int){
        mutableSelectedPokemon.value = PokemonInfo(id)
    }

    fun doSomethingWithPokemon(owner: LifecycleOwner, viewHolder: SearchViewAdapter.ViewHolder,
                               f:(p: List<PokemonListItem>, v: SearchViewAdapter.ViewHolder) -> Unit){
        listPokemon.observe(owner) {
            f(it, viewHolder)
        }
    }

    fun doSomethingWithFavorites(owner: LifecycleOwner, viewHolder: FavoritesAdapter.ViewHolder, f: (b: List<FavoritePokemon>, v: FavoritesAdapter.ViewHolder) -> Unit){

        favoritePokemon.observe(owner) { favorites ->
            f(favorites, viewHolder)
        }
    }

    fun removeFavoriteIfExistsForIndex(favIndex: Int) {

        val newList = initFavoritesList().filterIndexed { index, fav ->
            index != favIndex
        }

        mutableFavorites.value = newList
    }

    fun updateFavorite(favorite: FavoritePokemon){
        val newList = initFavoritesList().filterNot { it.id == favorite.id }

        val newListOfFavorites: List<FavoritePokemon> = newList.plus(favorite)

        mutableFavorites.value = newListOfFavorites.sortedBy { it.id }
    }

    fun removeFavoriteById(idToRemove: Int){
        val listWithRemovedFavorite = initFavoritesList().filterNot{ it.id == idToRemove}
        mutableFavorites.value = listWithRemovedFavorite
    }

    private fun allFavoritesIds(): List<Int> = initFavoritesList().map{ it.id }

    fun cycleFavorited(id: Int){

        if(initFavoritesList().contains(FavoritePokemon(id))){
            removeFavoriteById(id)
        } else {
            saveFavorite(id)
        }
    }

    fun saveFavorite(favorite: Int){

        val existingFavorite = initFavoritesList().find { it.id == favorite }

        if (existingFavorite != null) {
            removeFavoriteById(existingFavorite.id)

            val newList = initFavoritesList().filterNot { it.id == favorite }

            val newFavorite = FavoritePokemon(favorite)

            val newListOfFavorites: List<FavoritePokemon> = newList.plus(newFavorite)

            mutableFavorites.value = newListOfFavorites.sortedBy { it.id }

        } else {

            val newList = initFavoritesList().filterNot { it.id == favorite }

            val newFavorite = FavoritePokemon(favorite)

            val newListOfFavorites: List<FavoritePokemon> = newList.plus(newFavorite)

            mutableFavorites.value = newListOfFavorites.sortedBy { it.id }
        }

        println(mutableFavorites.value)
    }

    private fun smallestUnusedIndex(ids: List<Int>): Int {
        var smallestIndex = 1

        if(ids.isEmpty()) return smallestIndex

        val sortedList = ids.sorted()

        if(sortedList[0] > 1) return smallestIndex
        if(sortedList[sortedList.size - 1] <= 0) return smallestIndex

        for (element in sortedList) {
            if(element == smallestIndex){
                smallestIndex++
            }
        }

        return smallestIndex
    }

    fun lastFavoriteId(): Int {
        return smallestUnusedIndex(allFavoritesIds())
    }
}

data class PokemonInfo(
    val id: Int
)

data class FavoritePokemon(
    val id: Int
)