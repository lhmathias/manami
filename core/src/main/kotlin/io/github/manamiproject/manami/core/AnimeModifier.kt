package io.github.manamiproject.manami.core

import io.github.manamiproject.manami.entities.*

interface AnimeModifier {

    fun changeTitle(anime: Anime, newTitle: Title)

    fun changeType(anime: Anime, newType: AnimeType)

    fun changeEpisodes(anime: Anime, newNumberOfEpisodes: Episodes)

    fun changeInfoLink(anime: Anime, newInfoLink: InfoLink)

    fun changeLocation(anime: Anime, newLocation: Location)
}