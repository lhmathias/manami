package io.github.manamiproject.manami.persistence

import io.github.manamiproject.manami.entities.MinimalEntry

interface ExternalPersistence : AnimeList, WatchList, FilterList {

    /**
     * Clears all the lists: animelist, filterlist, watchlist.
     */
    fun clearAll()

    fun updateOrCreate(entry: MinimalEntry)
}
