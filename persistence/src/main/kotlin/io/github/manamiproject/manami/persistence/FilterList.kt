package io.github.manamiproject.manami.persistence

import io.github.manamiproject.manami.entities.FilterListEntry
import io.github.manamiproject.manami.entities.InfoLink
import io.github.manamiproject.manami.entities.MinimalEntry


interface FilterList {

    /**
     * Adds a URL to the filter list.
     *
     * @param anime Anime
     */
    fun filterAnime(anime: MinimalEntry): Boolean


    /**
     * Retrieves the persisted filter list.
     *
     * @return List of anime which have been filtered.
     */
    fun fetchFilterList(): MutableList<FilterListEntry>


    /**
     * Checks whether an anime is already in your filter list.
     *
     * @param infoLink URL
     * @return true if the URL is in the filter list.
     */
    fun filterListEntryExists(infoLink: InfoLink): Boolean


    /**
     * Removes an entry from the filter list.
     *
     * @param anime URL
     * @return
     */
    fun removeFromFilterList(anime: MinimalEntry): Boolean
}
