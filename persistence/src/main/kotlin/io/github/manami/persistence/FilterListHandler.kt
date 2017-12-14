package io.github.manami.persistence;

import io.github.manami.dto.entities.FilterEntry
import io.github.manami.dto.entities.InfoLink
import io.github.manami.dto.entities.MinimalEntry


interface FilterListHandler {

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
    fun fetchFilterList(): MutableList<FilterEntry>


    /**
     * Checks whether an anime is already in your filter list.
     *
     * @param infoLink URL
     * @return true if the URL is in the filter list.
     */
    fun filterEntryExists(infoLink: InfoLink): Boolean


    /**
     * Removes an entry from the filter list.
     *
     * @param infoLink URL
     * @return
     */
    fun removeFromFilterList(infoLink: InfoLink): Boolean
}
