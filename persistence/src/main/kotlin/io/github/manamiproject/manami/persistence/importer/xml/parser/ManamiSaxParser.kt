package io.github.manamiproject.manami.persistence.importer.xml.parser

import io.github.manamiproject.manami.entities.AnimeType
import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.entities.FilterListEntry
import io.github.manamiproject.manami.entities.InfoLink
import io.github.manamiproject.manami.entities.WatchListEntry
import io.github.manamiproject.manami.persistence.InternalPersistence
import io.github.manamiproject.manami.persistence.importer.xml.postprocessor.ImportDocument
import io.github.manamiproject.manami.persistence.importer.xml.postprocessor.ImportMigrationPostProcessor
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler
import java.lang.StringBuilder
import java.net.URL


internal class ManamiSaxParser(
        private val persistence: InternalPersistence
) : DefaultHandler() {

    /**
     * This is the builder for the text within the elements.
     */
    private var strBuilder = StringBuilder()
    private val importDocument = ImportDocument(
            "",
            mutableListOf(),
            mutableListOf(),
            mutableListOf()
    )

    override fun startElement(namespaceUri: String, localName: String, qName: String, attributes: Attributes) {
        strBuilder = StringBuilder()

        when (qName) {
            "manami" -> importDocument.documentVersion = attributes.getValue("version")
            "anime" -> createAnimeEntry(attributes)
            "filterEntry" -> createFilterEntry(attributes)
            "watchListEntry" -> createWatchListEntry(attributes)
        }
    }


    override fun endDocument() {
        persistence.addAnimeList(importDocument.animeListEntries)
        persistence.addFilterList(importDocument.filterListEntries)
        persistence.addWatchList(importDocument.watchListEntries)
        ImportMigrationPostProcessor.migrate(importDocument)
    }


    private fun createAnimeEntry(attributes: Attributes) {
        val title: String = attributes.getValue("title").trim()
        val infoLink = InfoLink(attributes.getValue("infoLink").trim())

        val actAnime: Anime = Anime(title, infoLink).apply {
            episodes = Integer.valueOf(attributes.getValue("episodes").trim())
            location = attributes.getValue("location").trim()
        }

        AnimeType.findByName(attributes.getValue("type").trim())?.let {
            actAnime.type = it
        }

        importDocument.animeListEntries.add(actAnime)
    }


    private fun createFilterEntry(attributes: Attributes) {
        val entry = FilterListEntry(
                attributes.getValue("title").trim(),
                InfoLink(attributes.getValue("infoLink").trim()),
                URL(attributes.getValue("thumbnail").trim())
        )

        importDocument.filterListEntries.add(entry)
    }


    private fun createWatchListEntry(attributes: Attributes) {
        val entry = WatchListEntry(
                attributes.getValue("title").trim(),
                InfoLink(attributes.getValue("infoLink").trim()),
                URL(attributes.getValue("thumbnail").trim())
        )

        importDocument.watchListEntries.add(entry)
    }


    override fun characters(ch: CharArray, start: Int, length: Int) {
        strBuilder.append(String(ch, start, length))
    }
}