package com.example.androidclubapp.models

data class Pokemon(
    val id: String,
    val name: String,
    val sprites: Sprites,
    val types: Array<`Types`>
)

data class Sprites(
    val other: Other
)

data class Other(
    val `official-artwork`: OfficialArtwork
)

data class OfficialArtwork(
    val front_default: String
)

data class `Types`(
    val `type`: `Type`
)

data class `Type`(
    val name: String
)