package io.github.manami.persistence.exporter

import java.nio.file.Path

/**
 * Interface for an exporter.
 */
interface Exporter {

    /**
     * Writes all three lists animelist, filterlist and watchlist to a specified file.
     *
     * @param file File
     * @return true if the exportToJsonFile was successful and false if an error occurred.
     */
    fun save(file: Path): Boolean
}
