package io.github.manamiproject.manami.core.tasks

import io.github.manamiproject.manami.cache.Cache
import io.github.manamiproject.manami.cache.CacheFacade
import io.github.manamiproject.manami.common.LoggerDelegate
import io.github.manamiproject.manami.dto.entities.Anime
import io.github.manamiproject.manami.dto.entities.FilterListEntry
import io.github.manamiproject.manami.dto.entities.MinimalEntry
import io.github.manamiproject.manami.dto.entities.WatchListEntry
import io.github.manamiproject.manami.persistence.Persistence
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.RequestBuilder
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.slf4j.Logger
import org.slf4j.MDC


private const val HTTP_STATUS_OK = 200


internal class ThumbnailBackloadTask(
        private val persistence: Persistence
) : AbstractTask() {

  private val log: Logger by LoggerDelegate()
  private val httpClient: CloseableHttpClient = HttpClients.createDefault()
  private val cache: Cache = CacheFacade


  override fun execute() {
    persistence.fetchFilterList().parallelStream().forEach(this::loadThumbnailIfNotExists)
    persistence.fetchWatchList().parallelStream().forEach(this::loadThumbnailIfNotExists)

    persistence.fetchFilterList().parallelStream().forEach(this::updateThumbnailIfNotAvailable)
    persistence.fetchWatchList().parallelStream().forEach(this::updateThumbnailIfNotAvailable)

    httpClient.close()
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

    val response: CloseableHttpResponse = httpClient.execute(RequestBuilder.head(entry.thumbnail.toString()).build())
    val responseCodeThumbnail = response.statusLine.statusCode

    if (responseCodeThumbnail != HTTP_STATUS_OK) {
      cache.fetchAnime(entry.infoLink)?.let {
        log.debug("Updating thumbnail.")
        updateThumbnail(entry, it)
      }
    }
  }
}