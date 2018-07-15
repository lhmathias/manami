package io.github.manamiproject.manami.core

import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.entities.InfoLink
import java.nio.file.Path

/**
 * All functionality on anime data.
 */
interface AnimeDataAccess {

    /**
     * Searches for an anime and fires an event with the search result.
     * @param animeTitle Title or part of the title to search for.
     */
    fun search(animeTitle: String)

    /**
     * Export a given list to a file.
     * @param list List of Anime to export.
     * @param file The file in which the list will be saved. If the file does not exist, it will be created.
     */
    fun exportList(list: List<Anime>, file: Path)

    /**
     * Fetch an anime based on it's infoLink.
     * @param infoLink Website which contains the anime meta data.
     * @return Either the requested Anime or null if an anime couldn't be extracted from the infoLink.
     */
    fun fetchAnime(infoLink: InfoLink): Anime?
}