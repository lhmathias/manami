package io.github.manamiproject.manami.persistence

import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.entities.InfoLink


/**
 * List of anime which the user already watched.
 */
interface AnimeList {

    /**
     * Adds an Anime if it is not already in the list.
     * @param anime Anime to add to the list of watched anime.
     * @return True if the anime has been added successfully.
     */
    fun addAnime(anime: Anime): Boolean


    /**
     * @return A List of Anime which the user already watched.
     */
    fun fetchAnimeList(): List<Anime>


    /**
     * Checks whether an anime is already in the user's anime list.
     * @param infoLink Identifier for an anime.
     * @return True if an entry with this InfoLink already exists in anime list.
     */
    fun animeEntryExists(infoLink: InfoLink): Boolean


    /**
     * @param anime Anime to be removed from anime list.
     * @return True if the entry has been removed successfully from anime list.
     */
    fun removeAnime(anime: Anime): Boolean
}
