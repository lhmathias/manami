package io.github.manamiproject.manami.persistence.importer

import java.nio.file.Path

/**
 * Imports data from a file.
 */
internal interface Importer {

    /**
     * Imports data from a file.
     * @param file File
     */
    fun importFile(file: Path)
}
