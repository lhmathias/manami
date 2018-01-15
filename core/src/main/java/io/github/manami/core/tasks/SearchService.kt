package io.github.manami.core.tasks

import io.github.manami.common.EventBus
import io.github.manami.core.tasks.events.SearchResultEvent
import io.github.manami.dto.entities.Anime
import io.github.manami.dto.entities.FilterListEntry
import io.github.manami.dto.entities.MinimalEntry
import io.github.manami.dto.entities.WatchListEntry
import io.github.manami.persistence.Persistence
import org.apache.commons.lang3.StringUtils.containsIgnoreCase
import org.apache.commons.text.similarity.LevenshteinDistance


private const val MAX_LEVENSHTEIN_DISTANCE = 2

internal class SearchService(
        private val searchString: String,
        private val persistence: Persistence
) : AbstractTask() {

  private val event = SearchResultEvent(searchString)


  override fun execute() {
    persistence.fetchAnimeList().forEach(this::checkEntry)
    persistence.fetchFilterList().forEach(this::checkEntry)
    persistence.fetchWatchList().forEach(this::checkEntry)

    EventBus.publish(event)
  }


  private fun checkEntry(entry: MinimalEntry) {
    val isTitleNearlyEqual = LevenshteinDistance(MAX_LEVENSHTEIN_DISTANCE + 1).apply(entry.title.toLowerCase(), searchString.toLowerCase()) <= MAX_LEVENSHTEIN_DISTANCE
    val isInTitle = containsIgnoreCase(entry.title, searchString)
    val isInfoLinkEqual = searchString.equals(entry.infoLink.toString(), ignoreCase = true)

    if (isTitleNearlyEqual || isInTitle || isInfoLinkEqual) {
      addToList(entry)
    }
  }


  private fun addToList(entry: MinimalEntry) {
    when(entry) {
      is Anime -> event.addAnimeListSearchResult(entry)
      is FilterListEntry -> event.addFilterListSearchResult(entry)
      is WatchListEntry -> event.addWatchListSearchResult(entry)
    }
  }
}