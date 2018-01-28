package io.github.manamiproject.manami.persistence.importer.json

import io.github.manamiproject.manami.common.LoggerDelegate
import io.github.manamiproject.manami.dto.AnimeType
import io.github.manamiproject.manami.dto.entities.Anime
import io.github.manamiproject.manami.dto.entities.FilterListEntry
import io.github.manamiproject.manami.dto.entities.InfoLink
import io.github.manamiproject.manami.dto.entities.WatchListEntry
import io.github.manamiproject.manami.persistence.InternalPersistence
import io.github.manamiproject.manami.persistence.importer.Importer
import org.json.JSONArray
import org.json.JSONTokener
import org.slf4j.Logger
import java.io.BufferedReader
import java.io.FileReader
import java.lang.StringBuilder
import java.net.URL
import java.nio.file.Path


private const val UNKNOWN_TYPE_MESSAGE = "Could not import '{}', because the type is unknown."

/**
 * Imports a list from a valid JSON file.
 */
internal class JsonImporter(private val persistence: InternalPersistence) : Importer {

    private val log: Logger by LoggerDelegate()
    private val animeListEntries: MutableList<Anime> = mutableListOf()
    private val filterListEntries: MutableList<FilterListEntry> = mutableListOf()
    private val watchListEntries: MutableList<WatchListEntry> = mutableListOf()


    override fun importFile(file: Path) {
        val fileReader = FileReader(file.toFile())
        val strBuilder = StringBuilder()

        BufferedReader(fileReader).use {
            it.lines().forEach { line -> strBuilder.append(line).append('\n') }
        }

        val tokener = JSONTokener(strBuilder.toString())
        val jsonArr = JSONArray(tokener)

        extractAnimeList(jsonArr)
        extractWatchList(jsonArr)
        extractFilterList(jsonArr)
    }


    private fun extractAnimeList(jsonArr: JSONArray) {
        val animeListArr: JSONArray = jsonArr.getJSONArray(0)

        (0 until animeListArr.length()).forEach { i ->
            val title: String = animeListArr.getJSONObject(i).getString("title").trim()
            val type: AnimeType? = AnimeType.findByName(animeListArr.getJSONObject(i).getString("type").trim())
            val episodes: Int = animeListArr.getJSONObject(i).getInt("episodes")
            val location: String = animeListArr.getJSONObject(i).getString("location").trim()
            val infoLink: String = animeListArr.getJSONObject(i).getString("infoLink").trim()

            val curAnime = Anime(title, InfoLink(infoLink)).apply {
                this.episodes = episodes
                this.location = location
            }

            type?.let {
                curAnime.type = it
            }

            animeListEntries.add(curAnime)
        }

        persistence.addAnimeList(animeListEntries)
    }


    private fun extractWatchList(jsonArr: JSONArray) {
        val animeListArr: JSONArray = jsonArr.getJSONArray(1)

        for (i in 0 until animeListArr.length()) {
            val thumbnail: String = animeListArr.getJSONObject(i).getString("thumbnail").trim()
            val title: String = animeListArr.getJSONObject(i).getString("title").trim()
            var infoLink: String = animeListArr.getJSONObject(i).getString("infoLink")

            infoLink = infoLink.trim()

            if (title.isNotBlank() && infoLink.isNotBlank()) {
                watchListEntries.add(WatchListEntry(title, InfoLink(infoLink), URL(thumbnail)))
            } else {
                log.warn(UNKNOWN_TYPE_MESSAGE, title)
            }
        }

        persistence.addWatchList(watchListEntries)
    }


    private fun extractFilterList(jsonArr: JSONArray) {
        val animeListArr: JSONArray = jsonArr.getJSONArray(2)

        (0 until animeListArr.length()).forEach { i ->
            val thumbnail: String = animeListArr.getJSONObject(i).getString("thumbnail").trim()
            val title: String = animeListArr.getJSONObject(i).getString("title").trim()
            var infoLink: String = animeListArr.getJSONObject(i).getString("infoLink")
            infoLink = infoLink.trim()

            if (title.isNotBlank()) {
                filterListEntries.add(FilterListEntry(title, InfoLink(infoLink), URL(thumbnail)))
            } else {
                log.warn(UNKNOWN_TYPE_MESSAGE, title)
            }
        }

        persistence.addFilterList(filterListEntries)
    }
}