package io.github.manami.core.tasks

import io.github.manami.cache.Cache
import io.github.manami.dto.LoggerDelegate
import io.github.manami.dto.entities.Anime
import io.github.manami.dto.entities.FilterListEntry
import io.github.manami.dto.entities.MinimalEntry
import io.github.manami.dto.entities.WatchListEntry
import io.github.manami.persistence.PersistenceHandler
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.RequestBuilder
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.slf4j.Logger
import org.slf4j.MDC
import java.util.*


private const val HTTP_STATUS_OK = 200


internal class ThumbnailBackloadTask(
        private val cache: Cache,
        private val persistence: PersistenceHandler
) : AbstractTask() {

  private val log: Logger by LoggerDelegate()
  private val httpClient: CloseableHttpClient = HttpClients.createDefault()


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
      val cachedAnime: Optional<Anime> = cache.fetchAnime(entry.infoLink)

      log.debug("Loading thumbnail")

      if (cachedAnime.isPresent) {
        updateThumbnail(entry, cachedAnime.get())
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
      val updatedAnime: Optional<Anime> = cache.fetchAnime(entry.infoLink)

      if (updatedAnime.isPresent) {
        log.debug("Updating thumbnail.")
        updateThumbnail(entry, updatedAnime.get())
      }
    }
  }
}