package io.github.manamiproject.manami.core.tasks.search

import io.github.manamiproject.manami.common.EventBus
import io.github.manamiproject.manami.core.events.SearchResultEvent
import io.github.manamiproject.manami.core.tasks.AbstractTask
import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.entities.FilterListEntry
import io.github.manamiproject.manami.entities.MinimalEntry
import io.github.manamiproject.manami.entities.WatchListEntry
import io.github.manamiproject.manami.persistence.Persistence
import org.apache.commons.text.similarity.LevenshteinDistance


private const val MAX_LEVENSHTEIN_DISTANCE = 2

internal class SearchTask(
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
    val isTitleNearlyEqual = LevenshteinDistance(MAX_LEVENSHTEIN_DISTANCE + 1)
            .apply(entry.title.toLowerCase(), searchString.toLowerCase()) <= MAX_LEVENSHTEIN_DISTANCE
    val isInTitle = entry.title.contains(searchString, ignoreCase = true)
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