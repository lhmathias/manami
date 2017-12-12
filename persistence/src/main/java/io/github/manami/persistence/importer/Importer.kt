package io.github.manami.persistence.importer

import java.nio.file.Path

/**
 * Interface for an importer.
 */
interface Importer {

    /**
     * Imports a list from a file and enriches the given list.
     *
     * @param file File
     * @throws SAXException, ParserConfigurationException, IOException
     */
    fun importFile(file: Path)
}
