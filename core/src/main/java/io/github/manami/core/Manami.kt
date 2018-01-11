package io.github.manami.core

import io.github.manami.dto.entities.Anime
import io.github.manami.persistence.ApplicationPersistence
import java.nio.file.Path

interface Manami : ApplicationPersistence {

    fun newList()

    fun open(file: Path)

    fun export(file: Path)

    fun importFile(file: Path)

    fun save()

    fun exit()

    fun search(searchString: String)

    fun exportList(list: MutableList<Anime>, file: Path)
}