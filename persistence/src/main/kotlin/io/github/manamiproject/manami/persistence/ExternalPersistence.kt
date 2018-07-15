package io.github.manamiproject.manami.persistence

import io.github.manamiproject.manami.entities.MinimalEntry

/**
 * Persistence functionality.
 */
interface ExternalPersistence : AnimeList, WatchList, FilterList {

    /**
     * Clears all the lists: animelist, filterlist, watchlist.
     */
    fun clearAll()

    /**
     * Update a specific entry or add it if does not exist.
     * @param entry Anime.
     */
    fun updateOrCreate(entry: MinimalEntry)
}
