package io.github.manami.persistence.importer.xml.parser

import io.github.manami.dto.AnimeType
import io.github.manami.dto.entities.Anime
import io.github.manami.dto.entities.FilterEntry
import io.github.manami.dto.entities.InfoLink
import io.github.manami.dto.entities.WatchListEntry
import io.github.manami.persistence.PersistenceFacade
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler


class MalSaxParser(private val persistence: PersistenceFacade) : DefaultHandler() {

  /**
   * Actual anime object.
   */
  private var actAnime: Anime? = null
  private var infoLink: InfoLink? = null
  private var title: String? = null
  private var animeType: AnimeType? = null
  private var episodes: Int? = null
  private var statusCurrentAnime: String? = null

  /**
   * This is the builder for the text within the elements.
   */
  private var strBuilder: StringBuilder = StringBuilder()


  private val animeListEntries: MutableList<Anime> = mutableListOf()
  private val filterListEntries: MutableList<FilterEntry> = mutableListOf()
  private val watchListEntries: MutableList<WatchListEntry> = mutableListOf()


  override fun startElement(namespaceURI: String, localName: String, qName: String, attributes: Attributes) {
    strBuilder = StringBuilder()

    if (qName == "anime") {
      actAnime = null
      infoLink = null
      title = null
      animeType = null
      episodes = 1
    }
  }


  override fun endElement(namespaceUri: String, localName: String, qName: String) {

    when(qName) {
        "series_animedb_id" -> infoLink = InfoLink("https://myanimelist.net/anime/$strBuilder.toString().trim()")
        "series_title" -> title = strBuilder.toString().trim()
        "series_type" -> animeType = AnimeType.findByName(strBuilder.toString().trim())
        "series_episodes" -> {
            val episodeStr: String = strBuilder.toString().trim()
            episodes = episodeStr.toIntOrNull()
        }
        "my_status" -> statusCurrentAnime = strBuilder.toString().trim().toLowerCase()
        "anime" -> addAnime()
    }
  }


  private fun addAnime() {
      val title = this.title.takeIf { it != null } ?: return
      val infoLink = this.infoLink.takeIf { it != null } ?: return

      actAnime = Anime(title, infoLink).also { anime ->

          episodes?.let {
              anime.episodes = it
          }

          animeType?.let {
              anime.type = it
          }

          anime.location = "/"

          when (statusCurrentAnime) {
              "completed" -> animeListEntries.add(anime)
              "watching", "plan to watch" -> watchListEntries.add(WatchListEntry.valueOf(anime))
              "dropped" -> filterListEntries.add(FilterEntry.valueOf(anime))

          }
      }

    statusCurrentAnime = null
  }


  override fun characters(ch: CharArray, start: Int, length: Int) {
    strBuilder.append(String(ch, start, length))
  }


  override fun endDocument() {
    persistence.addAnimeList(animeListEntries)
    persistence.addFilterList(filterListEntries)
    persistence.addWatchList(watchListEntries)
  }
}
