package io.github.manamiproject.manami.core

import java.nio.file.Path

interface Application {

    fun newList()

    fun open(file: Path)

    fun export(file: Path)

    fun importFile(file: Path)

    fun saveAs(file: Path)

    fun save()

    fun exit()

    fun undo()

    fun redo()

    fun isFileSaved(): Boolean
}