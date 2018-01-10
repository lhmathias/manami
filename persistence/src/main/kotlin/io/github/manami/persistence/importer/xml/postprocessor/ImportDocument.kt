package io.github.manami.persistence.importer.xml.postprocessor

import io.github.manami.dto.entities.Anime
import io.github.manami.dto.entities.FilterListEntry
import io.github.manami.dto.entities.WatchListEntry

internal data class ImportDocument(
        var documentVersion: String,
        val animeListEntries: MutableList<Anime>,
        val filterListEntries: MutableList<FilterListEntry>,
        val watchListEntries: MutableList<WatchListEntry>
)