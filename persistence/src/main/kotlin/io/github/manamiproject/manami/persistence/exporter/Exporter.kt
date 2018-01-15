package io.github.manamiproject.manami.persistence.exporter

import java.nio.file.Path

/**
 * Interface for an exporter.
 */
internal interface Exporter {

    /**
     * Writes all three lists animelist, filterlist and watchlist to a specified file.
     *
     * @param file File
     * @return true if the exportToJsonFile was successful and false if an error occurred.
     */
    fun save(file: Path): Boolean
}
