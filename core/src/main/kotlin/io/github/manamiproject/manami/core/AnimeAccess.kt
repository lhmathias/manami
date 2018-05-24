package io.github.manamiproject.manami.core

import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.entities.InfoLink
import java.nio.file.Path

interface AnimeAccess {

    fun search(searchString: String)

    fun exportList(list: List<Anime>, file: Path)

    fun fetchAnime(infoLink: InfoLink): Anime?
}