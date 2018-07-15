package io.github.manamiproject.manami.core

import java.nio.file.Path

/**
 * Functionality of a stateful application.
 */
interface StatefulApplication {

    /**
     * Creates a completely new list. Comparable with "new file" elsewhere.
     */
    fun newList()

    /**
     * Opens an existing manami file.
     * @param file Valid manami file.
     */
    fun open(file: Path)

    /**
     * Exports all lists (anime list, watch list, filter list) to a different file format.
     * @param file The file to which the lists are being exported.
     */
    fun export(file: Path)

    /**
     * Imports a different file format.
     * @param file Existing file in a different format.
     */
    fun importFile(file: Path)

    /**
     * Save file with a specific name and location.
     * @param file Name and location to save the file to.
     */
    fun saveAs(file: Path)

    /**
     * Save an existing and currently opened file.
     */
    fun save()

    /**
     * Terminate the application.
     */
    fun exit()

    /**
     * Undo the last change.
     */
    fun undo()

    /**
     * Redo the last change that had been undone.
     */
    fun redo()

    /**
     * @return True if the currently opened file has unsaved changes.
     */
    fun isFileUnsaved(): Boolean

    /**
     * @return Currently opened file or a reference to the folder of the application if no file has been opened.
     */
    fun getCurrentlyOpenedFile(): Path

    /**
     * @return True if successfully executed changes exist.
     */
    fun doneCommandsExist(): Boolean

    /**
     * @return True if changes exist which had been made undone.
     */
    fun undoneCommandsExist(): Boolean
}