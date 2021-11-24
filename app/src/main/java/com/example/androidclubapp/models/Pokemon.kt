package com.example.androidclubapp.models

data class Pokemon(
    val id: String,
    val name: String,
    val sprites: Sprites,
    val species: Species,
    val types: Array<`Types`>
)

data class Sprites(
    val other: Other
)

data class Species(
    val url: String
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

data class Descriptions(
    val flavor_text_entries: Array<Description>
)

data class Description(
    val flavor_text: String,
    val language: Language
)

data class Language(
    val name: String
)