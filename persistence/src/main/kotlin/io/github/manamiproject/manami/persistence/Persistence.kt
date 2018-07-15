package io.github.manamiproject.manami.persistence


import io.github.manamiproject.manami.entities.Anime
import java.nio.file.Path

interface Persistence : ExternalPersistence, InternalPersistence {

    /**
     * Opens a Manami file containing all three lists anime list, filter list and watch list
     * @param file Valid Manami file to be opened.
     */
    fun open(file: Path)

    /**
     * Imports a myanimelist.net file.
     * @param file A myanimelist.net export file.
     */
    fun importMalFile(file: Path)

    /**
     * Import of a JSON file which has been previously exported with using.
     * @param file Manami JSON file.
     */
    fun importJsonFile(file: Path)

    /**
     * Exports a list of Anime to a JSON file.
     * @param list List of Anime
     * @param file File in which the list will be saved..
     */
    fun exportListToJsonFile(list: List<Anime>, file: Path)

    /**
     * Exports all three lists anime list, filter list and watch list toa specified json file.
     * @param file File in which the lists will be saved.
     */
    fun exportToJsonFile(file: Path)

    /**
     * Writes all three lists anime list, filter list and watch list to a specified file.
     * @param file File in which alle lists will be saved.
     * @return True if saving was successful and false if an error occurred.
     */
    fun save(file: Path): Boolean
}
