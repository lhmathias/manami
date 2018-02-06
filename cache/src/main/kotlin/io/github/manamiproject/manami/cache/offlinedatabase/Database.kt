package io.github.manamiproject.manami.cache.offlinedatabase

import io.github.manamiproject.manami.dto.entities.InfoLink

internal class Database(
        val animeMetaData: MutableSet<AnimeEntry> = mutableSetOf(),
        val deadEntries: MutableSet<InfoLink>  = mutableSetOf()
) {

    fun addAnimeEntry(anime: AnimeEntry) {
        animeMetaData.add(anime)
    }

    fun addDeadEntry(infoLink: InfoLink) {
        deadEntries.add(infoLink)
    }
}