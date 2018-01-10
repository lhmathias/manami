package io.github.manami.persistence.importer.json

import io.github.manami.dto.AnimeType
import io.github.manami.dto.LoggerDelegate
import io.github.manami.dto.entities.Anime
import io.github.manami.dto.entities.FilterListEntry
import io.github.manami.dto.entities.InfoLink
import io.github.manami.dto.entities.WatchListEntry
import io.github.manami.persistence.PersistenceFacade
import io.github.manami.persistence.importer.Importer
import org.json.JSONArray
import org.json.JSONTokener
import org.slf4j.Logger
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.lang.StringBuilder
import java.net.MalformedURLException
import java.net.URL
import java.nio.file.Path
import javax.inject.Inject
import javax.inject.Named


private const val URL_PARSING_EXCEPTION_MESSAGE = "Unable to import [{}]"
private const val UNKNOWN_TYPE_MESSAGE = "Could not import '{}', because the type is unknown."

/**
 * Imports a list from a valid JSON file.
 */
@Named("jsonImporter")
internal class JsonImporter @Inject constructor(private val persistence: PersistenceFacade) : Importer {

    private val log: Logger by LoggerDelegate()
    private val animeListEntries: MutableList<Anime> = mutableListOf()
    private val filterListEntries: MutableList<FilterListEntry> = mutableListOf()
    private val watchListEntries: MutableList<WatchListEntry> = mutableListOf()


    override fun importFile(file: Path) {
        try {
            val fileReader = FileReader(file.toFile())
            val br = BufferedReader(fileReader)
            val strBuilder = StringBuilder()

            br.lines().forEach { line -> strBuilder.append(line).append('\n') }

            val tokener = JSONTokener(strBuilder.toString())
            val jsonArr = JSONArray(tokener)

            extractAnimeList(jsonArr)
            extractWatchList(jsonArr)
            extractFilterList(jsonArr)

            br.close()
        } catch (e: IOException) {
            log.error("An error occurred while trying to import JSON file: ", e)
        }
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
                try {
                    watchListEntries.add(WatchListEntry(title, InfoLink(infoLink), URL(thumbnail)))
                } catch (e: MalformedURLException) {
                    log.error(URL_PARSING_EXCEPTION_MESSAGE, title, e)
                }
            } else {
                log.debug(UNKNOWN_TYPE_MESSAGE, title)
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
                try {
                    filterListEntries.add(FilterListEntry(title, InfoLink(infoLink), URL(thumbnail)))
                } catch (e: MalformedURLException) {
                    log.error(URL_PARSING_EXCEPTION_MESSAGE, title, e)
                }
            } else {
                log.debug(UNKNOWN_TYPE_MESSAGE, title)
            }
        }

        persistence.addFilterList(filterListEntries)
    }
}
