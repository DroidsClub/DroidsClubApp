package com.example.androidclubapp.models

import androidx.lifecycle.*
import com.example.androidclubapp.SearchViewAdapter

class PokemonViewModel: ViewModel() {

    private val mutableSelectedPokemon = MutableLiveData<PokemonInfo>()
    private val mutableListPokemon = MutableLiveData<List<PokemonListItem>>()

    val selectedPokemon: LiveData<PokemonInfo> get() = mutableSelectedPokemon
    val listPokemon: LiveData<List<PokemonListItem>> get() = mutableListPokemon

    fun selectPokemon(id: Int){
        mutableSelectedPokemon.value = PokemonInfo(id)
    }

    fun doSomethingWithPokemon(owner: LifecycleOwner, viewHolder: SearchViewAdapter.ViewHolder,
                               f:(p: List<PokemonListItem>, v: SearchViewAdapter.ViewHolder) -> Unit){
        listPokemon.observe(owner) {
            f(it, viewHolder)
        }
    }
}

data class PokemonInfo(
    val id: Int
)