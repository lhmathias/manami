package io.github.manami.persistence.importer.xml.parser;

import io.github.manami.dto.AnimeType
import io.github.manami.dto.LoggerDelegate
import io.github.manami.dto.entities.Anime
import io.github.manami.dto.entities.FilterEntry
import io.github.manami.dto.entities.InfoLink
import io.github.manami.dto.entities.WatchListEntry
import io.github.manami.persistence.PersistenceFacade
import io.github.manami.persistence.importer.xml.postprocessor.ImportMigrationPostProcessor
import org.slf4j.Logger
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler
import java.net.MalformedURLException
import java.net.URL

class ManamiSaxParser(private val persistence: PersistenceFacade) : DefaultHandler() {

    private val log: Logger by LoggerDelegate()
    private val THUMBNAIL_PARSING_EXCEPTION: String = "Unable to parse thumbnail URL from [{}]"

    /**
     * This is the builder for the text within the elements.
     */
    private var strBuilder: StringBuilder = StringBuilder()
    private val animeListEntries: MutableList<Anime> = mutableListOf()
    private val filterListEntries: MutableList<FilterEntry> = mutableListOf()
    private val watchListEntries: MutableList<WatchListEntry> = mutableListOf()

    private var importMigrationPostProcessor: ImportMigrationPostProcessor? = null


    override fun startElement(namespaceUri: String, localName: String, qName: String, attributes: Attributes) {
        strBuilder = StringBuilder()

        when (qName) {
            "manami" -> importMigrationPostProcessor = ImportMigrationPostProcessor(
                    attributes.getValue("version"),
                    animeListEntries,
                    filterListEntries,
                    watchListEntries
            )
            "anime" -> createAnimeEntry(attributes)
            "filterEntry" -> createFilterEntry(attributes)
            "watchListEntry" -> createWatchListEntry(attributes)
        }
    }


    override fun endDocument() {
        persistence.addAnimeList(animeListEntries)
        persistence.addFilterList(filterListEntries)
        persistence.addWatchList(watchListEntries)
        importMigrationPostProcessor?.execute()
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

        animeListEntries.add(actAnime)
    }


    private fun createFilterEntry(attributes: Attributes) {
        try {
            val entry = FilterEntry(
                    attributes.getValue("title").trim(),
                    InfoLink(attributes.getValue("infoLink").trim()),
                    URL(attributes.getValue("thumbnail").trim())
            )

            filterListEntries.add(entry)
        } catch (e: MalformedURLException) {
            log.error(THUMBNAIL_PARSING_EXCEPTION, attributes.getValue("title"))
        }
    }


    private fun createWatchListEntry(attributes: Attributes) {
        try {
            val entry = WatchListEntry(
                    attributes.getValue("title").trim(),
                    InfoLink(attributes.getValue("infoLink").trim()),
                    URL(attributes.getValue("thumbnail").trim())
            )

            watchListEntries.add(entry);
        } catch (e: MalformedURLException) {
            log.error(THUMBNAIL_PARSING_EXCEPTION, attributes.getValue("title"))
        }
    }


    override fun characters(ch: CharArray, start: Int, length: Int) {
        strBuilder.append(String(ch, start, length))
    }
}
