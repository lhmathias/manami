package io.github.manamiproject.manami.persistence.importer.xml.postprocessor

import io.github.manamiproject.manami.dto.entities.Anime
import io.github.manamiproject.manami.dto.entities.FilterListEntry
import io.github.manamiproject.manami.dto.entities.WatchListEntry

internal data class ImportDocument(
        var documentVersion: String,
        val animeListEntries: MutableList<Anime>,
        val filterListEntries: MutableList<FilterListEntry>,
        val watchListEntries: MutableList<WatchListEntry>
)