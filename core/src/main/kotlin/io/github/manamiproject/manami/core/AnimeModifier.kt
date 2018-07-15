package io.github.manamiproject.manami.core

import io.github.manamiproject.manami.entities.*

/**
 * Functionality to change entries in the anime list.
 */
interface AnimeModifier {

    /**
     * Change the title of an entry.
     * @param anime Entry which should be changed.
     * @param newTitle The new title.
     */
    fun changeTitle(anime: Anime, newTitle: Title)

    /**
     * Change the type of an entry.
     * @param anime Entry which should be changed.
     * @param newType The new type.
     */
    fun changeType(anime: Anime, newType: AnimeType)

    /**
     * Change the amount of episodes of an entry.
     * @param anime Entry which should be changed.
     * @param newNumberOfEpisodes The new amount of episodes. The amount of episodes must be >=1.
     */
    fun changeEpisodes(anime: Anime, newNumberOfEpisodes: Episodes)

    /**
     * Change the InfoLink of an entry.
     * @param anime Entry which should be changed.
     * @param newInfoLink The new URL.
     */
    fun changeInfoLink(anime: Anime, newInfoLink: InfoLink)

    /**
     * Change the location of an entry.
     * @param anime Entry which should be changed.
     * @param newLocation The new location.
     */
    fun changeLocation(anime: Anime, newLocation: Location)
}