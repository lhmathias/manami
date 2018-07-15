package io.github.manamiproject.manami.persistence

import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.entities.FilterListEntry
import io.github.manamiproject.manami.entities.WatchListEntry

/**
 * Persistence with basic and internal functionality.
 */
internal interface InternalPersistence : ExternalPersistence {

    /**
     * Adds a list of anime to the anime list.
     * @param list of anime
     */
    fun addAnimeList(list: List<Anime>)

    /**
     * Adds a list of anime to the filter list.
     * @param list List of anime.
     */
    fun addFilterList(list: List<FilterListEntry>)

    /**
     * Adds a list of anime to the watch list.
     * @param list List of anime.
     */
    fun addWatchList(list: List<WatchListEntry>)
}