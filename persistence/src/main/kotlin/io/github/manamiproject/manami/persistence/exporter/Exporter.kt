package io.github.manamiproject.manami.persistence.exporter

import java.nio.file.Path

/**
 * An exporter which writes data to a file.
 */
internal interface Exporter {

    /**
     * Writes all three lists animelist, filterlist and watchlist to a specified file.
     * @param file File in which the lists are saved.
     * @return True if the export was successful and false if an error occurred.
     */
    fun save(file: Path): Boolean
}
