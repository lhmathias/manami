package io.github.manami.persistence.exporter.csv

import com.google.common.collect.Lists.newArrayList
import io.github.manami.dto.LoggerDelegate
import io.github.manami.dto.entities.Anime
import io.github.manami.dto.entities.FilterEntry
import io.github.manami.dto.entities.WatchListEntry
import io.github.manami.persistence.ApplicationPersistence
import io.github.manami.persistence.exporter.Exporter
import org.slf4j.Logger
import org.supercsv.io.CsvListWriter
import org.supercsv.io.ICsvListWriter
import org.supercsv.prefs.CsvPreference
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Path

/**
 * Exports a list to a csv file.
 */
class CsvExporter(private val persistence: ApplicationPersistence) : Exporter {

    private val log: Logger by LoggerDelegate()

    /**
     * Contains configurations for reading and writing csv files.
     */
    private val config: CsvConfig = CsvConfig()

    private var listWriter: ICsvListWriter? = null


    override fun exportAll(file: Path): Boolean {

        try {
            listWriter = CsvListWriter(FileWriter(file.toFile()), CsvPreference.STANDARD_PREFERENCE)

            // write the header
            listWriter?.writeHeader(*config.getHeaders())

            writeAnimeList()
            writeWatchList()
            writeFilterList()

            listWriter?.close()
        } catch (e: IOException) {
            log.error("An error occurred while trying to export the list to CSV: ", e)
            return false
        }

        return true
    }


    private fun writeAnimeList() {
        val mappedEntryList: MutableList<MutableList<String>> = mutableListOf()
        var curEntry: MutableList<String>

        // Map Anime Objects to a list
        for (entry: Anime in persistence.fetchAnimeList()) {
            curEntry = mutableListOf()
            curEntry.add(CsvConfig.CsvConfigType.ANIMELIST.value)
            curEntry.add(entry.title)
            curEntry.add(entry.toString())
            curEntry.add(entry.episodes.toString())
            curEntry.add(entry.infoLink.url.toString())
            curEntry.add(entry.location)
            mappedEntryList.add(curEntry)
        }

        // write the animeList
        for (entry: MutableList<String> in mappedEntryList) {
            listWriter?.write(entry, config.getProcessors())
        }
    }

    private fun writeWatchList() {
        val mappedEntryList: MutableList<MutableList<String>> = mutableListOf(mutableListOf())
        var curEntry: MutableList<String>

        // Map Anime Objects to a list
        for (entry: WatchListEntry in persistence.fetchWatchList()) {
            curEntry = newArrayList()
            curEntry.add(CsvConfig.CsvConfigType.WATCHLIST.value)
            curEntry.add(entry.title)
            curEntry.add("")
            curEntry.add("")
            curEntry.add(entry.infoLink.url.toString())
            curEntry.add("")
            mappedEntryList.add(curEntry)
        }

        // write the watchList
        for (entry: MutableList<String> in mappedEntryList) {
            listWriter?.write(entry, config.getProcessors())
        }
    }


    private fun writeFilterList() {
        val mappedEntryList: MutableList<MutableList<String>> = mutableListOf(mutableListOf())
        var curEntry: MutableList<String>

        // Map Anime Objects to a list
        for (entry: FilterEntry in persistence.fetchFilterList()) {
            curEntry = newArrayList()
            curEntry.add(CsvConfig.CsvConfigType.FILTERLIST.value)
            curEntry.add(entry.title)
            curEntry.add("")
            curEntry.add("")
            curEntry.add(entry.infoLink.url.toString())
            curEntry.add("")
            mappedEntryList.add(curEntry)
        }

        // write the filterList
        for (entry: MutableList<String> in mappedEntryList) {
            listWriter?.write(entry, config.getProcessors())
        }
    }
}