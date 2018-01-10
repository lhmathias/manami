package io.github.manami.persistence

import io.github.manami.dto.entities.Anime
import io.github.manami.dto.entities.FilterListEntry
import io.github.manami.dto.entities.WatchListEntry

interface InternalPersistenceHandler : ApplicationPersistence {

    /**
     * Clears all the lists: animelist, filterlist, watchlist.
     */
    fun clearAll()

    fun addAnimeList(list: MutableList<Anime>)

    fun addFilterList(list: MutableList<FilterListEntry>)

    fun addWatchList(list: MutableList<WatchListEntry>)
}