package io.github.manamiproject.manami.core.tasks.thumbnails

import io.github.manamiproject.manami.cache.Cache
import io.github.manamiproject.manami.cache.CacheFacade
import io.github.manamiproject.manami.common.LoggerDelegate
import io.github.manamiproject.manami.core.tasks.AbstractTask
import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.entities.FilterListEntry
import io.github.manamiproject.manami.entities.MinimalEntry
import io.github.manamiproject.manami.entities.WatchListEntry
import io.github.manamiproject.manami.persistence.Persistence
import org.slf4j.Logger
import org.slf4j.MDC
import java.net.HttpURLConnection
import java.net.URL


private const val HTTP_STATUS_OK = 200


internal class ThumbnailBackloadTask(
        private val persistence: Persistence
) : AbstractTask() {

  private val log: Logger by LoggerDelegate()
  private val cache: Cache = CacheFacade


  override fun execute() {
      persistence.fetchFilterList().parallelStream().forEach(this::loadThumbnailIfNotExists)
      persistence.fetchWatchList().parallelStream().forEach(this::loadThumbnailIfNotExists)

      persistence.fetchFilterList().parallelStream().forEach(this::updateThumbnailIfNotAvailable)
      persistence.fetchWatchList().parallelStream().forEach(this::updateThumbnailIfNotAvailable)
  }


  private fun loadThumbnailIfNotExists(entry: MinimalEntry) {
    MDC.put("infoLink", entry.infoLink.toString())

    if (MinimalEntry.NO_IMG_THUMB == entry.thumbnail) {
      cache.fetchAnime(entry.infoLink)?.let {
        log.debug("Loading thumbnail")
        updateThumbnail(entry, it)
      }
    }
  }


  private fun updateThumbnail(entry: MinimalEntry, cachedAnime: Anime) {
    when(entry) {
      is FilterListEntry -> persistence.updateOrCreate(entry.copy(thumbnail = cachedAnime.thumbnail))
      is WatchListEntry -> persistence.updateOrCreate(entry.copy(thumbnail = cachedAnime.thumbnail))
    }
  }


  private fun updateThumbnailIfNotAvailable(entry: MinimalEntry) {
    MDC.put("infoLink", entry.infoLink.toString())

    log.debug("Checking thumbnail.")

    val connection = (URL("").openConnection() as HttpURLConnection).apply {
      requestMethod = "GET"
    }

    connection.connect()

    if (connection.responseCode != HTTP_STATUS_OK) {
      cache.fetchAnime(entry.infoLink)?.let {
        log.debug("Updating thumbnail.")
        updateThumbnail(entry, it)
      }
    }
  }
}