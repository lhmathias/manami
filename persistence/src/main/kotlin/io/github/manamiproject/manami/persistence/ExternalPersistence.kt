package io.github.manamiproject.manami.persistence


/**
 * Persistence functionality.
 */
interface ExternalPersistence : AnimeList, WatchList, FilterList {

    /**
     * Clears all the lists: animelist, filterlist, watchlist.
     */
    fun clearAll()
}
