package io.github.manamiproject.manami.persistence

import io.github.manamiproject.manami.dto.entities.Anime
import io.github.manamiproject.manami.dto.entities.FilterListEntry
import io.github.manamiproject.manami.dto.entities.WatchListEntry

interface InternalPersistence : ApplicationPersistence {

    /**
     * Clears all the lists: animelist, filterlist, watchlist.
     */
    fun clearAll()

    fun addAnimeList(list: MutableList<Anime>)

    fun addFilterList(list: MutableList<FilterListEntry>)

    fun addWatchList(list: MutableList<WatchListEntry>)
}