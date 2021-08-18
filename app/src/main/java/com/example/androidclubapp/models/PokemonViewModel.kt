package com.example.androidclubapp.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PokemonViewModel: ViewModel() {

    private val mutableSelectedPokemon = MutableLiveData<PokemonInfo>()

    val selectedPokemon: LiveData<PokemonInfo> get() = mutableSelectedPokemon

    fun selectPokemon(id: Int){
        mutableSelectedPokemon.value = PokemonInfo(id)
    }
}

data class PokemonInfo(
    val id: Int
)