package com.example.androidclubapp.models

data class PokemonList(
    val results: Array<PokemonListItem>
)

data class PokemonListItem(
    val name: String,
    val url: String
)
