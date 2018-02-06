package io.github.manamiproject.manami.cache.offlinedatabase

import io.github.manamiproject.manami.dto.AnimeType
import java.net.URL
import java.util.*

internal data class AnimeEntry(
    var id: UUID,
    var title: String,
    var synonyms: Set<String>,
    var type: AnimeType,
    var episodes: Int,
    var picture: URL,
    var thumbnail: URL,
    var sources: Set<URL>,
    var relations: Set<URL>
)