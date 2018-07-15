package io.github.manamiproject.manami.persistence

import io.github.manamiproject.manami.entities.InfoLink
import io.github.manamiproject.manami.entities.MinimalEntry
import io.github.manamiproject.manami.entities.WatchListEntry

/**
 * List of anime which the user is watching or wants to watch.
 */
interface WatchList {

    /**
     * Adds an anime to the watch list.
     * @param anime Anime
     * @return True if the anime has been added successfully.
     */
    fun watchAnime(anime: MinimalEntry): Boolean

    /**
     * Retrieves the persisted watch list.
     * @return List of anime the user is watching or plans to watch.
     */
    fun fetchWatchList(): List<WatchListEntry>

    /**
     * Checks whether an anime is already in the user's watch list.
     * @param infoLink Identifier for an anime.
     * @return True if the InfoLink already exists in the watch list.
     */
    fun watchListEntryExists(infoLink: InfoLink): Boolean

    /**
     * @param anime Anime to be removed from watch List.
     * @return True if the entry has been removed successfully from watch list.
     */
    fun removeFromWatchList(anime: MinimalEntry): Boolean
}
