package io.github.manamiproject.manami.persistence

import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.entities.FilterListEntry
import io.github.manamiproject.manami.entities.WatchListEntry

interface InternalPersistence : ApplicationPersistence {

    /**
     * Clears all the lists: animelist, filterlist, watchlist.
     */
    fun clearAll()

    fun addAnimeList(list: List<Anime>)

    fun addFilterList(list: List<FilterListEntry>)

    fun addWatchList(list: List<WatchListEntry>)
}