package io.github.manamiproject.manami.persistence

import io.github.manamiproject.manami.entities.FilterListEntry
import io.github.manamiproject.manami.entities.InfoLink
import io.github.manamiproject.manami.entities.MinimalEntry

/**
 * List of anime which the user does'n want to see.
 */
interface FilterList {

    /**
     * Adds an anime to the filter list.
     * @param anime Anime
     * @return True if the anime has been added successfully.
     */
    fun filterAnime(anime: MinimalEntry): Boolean


    /**
     * Retrieves the persisted filter list.
     * @return List of anime which have been filtered.
     */
    fun fetchFilterList(): List<FilterListEntry>


    /**
     * Checks whether an anime is already in the user's filter list.
     * @param infoLink Identifier for an anime.
     * @return True if the InfoLink already exists in the filter list.
     */
    fun filterListEntryExists(infoLink: InfoLink): Boolean


    /**
     * @param anime Anime to be removed from filter List.
     * @return True if the entry has been removed successfully from filter list.
     */
    fun removeFromFilterList(anime: MinimalEntry): Boolean
}
