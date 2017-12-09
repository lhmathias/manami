package io.github.manami.core.services;

import static org.springframework.util.Assert.notNull;

import io.github.manami.cache.Cache;
import io.github.manami.dto.entities.Anime;
import io.github.manami.dto.entities.FilterEntry;
import io.github.manami.dto.entities.MinimalEntry;
import io.github.manami.dto.entities.WatchListEntry;
import io.github.manami.persistence.PersistenceFacade;
import java.io.IOException;
import java.util.Optional;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * @author manami-project
 * @since 2.8.2
 */
public class ThumbnailBackloadService extends AbstractService<Void> {

  private static final Logger log = LoggerFactory.getLogger(ThumbnailBackloadService.class);
  private static final int OK = 200;
  private static final int NOT_FOUND = 404;
  private final PersistenceFacade persistence;
  private final Cache cache;
  private CloseableHttpClient httpclient;


  /**
   * Constructor awaiting a cache.
   *
   * @param cache Cache
   * @since 2.8.2
   */
  public ThumbnailBackloadService(final Cache cache, final PersistenceFacade persistence) {
    this.cache = cache;
    this.persistence = persistence;
  }


  /**
   * @since 2.8.2
   */
  @Override
  protected Void execute() {
    notNull(cache, "Cache cannot be null");
    notNull(persistence, "List of anime cannot be null");

    httpclient = HttpClients.createDefault();

    persistence.fetchFilterList().parallelStream().forEach(this::loadThumbnailIfNotExists);
    persistence.fetchWatchList().parallelStream().forEach(this::loadThumbnailIfNotExists);

    persistence.fetchFilterList().parallelStream().forEach(this::checkPictures);
    persistence.fetchWatchList().parallelStream().forEach(this::checkPictures);

    try {
      httpclient.close();
    } catch (final IOException e) {
      log.error("An error occured while trying to close http client: ", e);
    }

    return null;
  }


  @Override
  public void reset() {
    cancel();
    super.reset();
  }


  /**
   * @since 2.9.0
   */
  private void loadThumbnailIfNotExists(final MinimalEntry entry) {
    if (isInterrupt() || entry == null) {
      return;
    }

    MDC.put("infoLink", entry.getInfoLink().getUrl());

    if (entry != null && (entry.getThumbnail() == null || MinimalEntry.Companion.getNO_IMG_THUMB()
        .equals(entry.getThumbnail()))) {
      final Optional<Anime> cachedAnime = cache.fetchAnime(entry.getInfoLink());
      log.debug("Loading thumbnail");
      if (cachedAnime.isPresent()) {
        updateThumbnail(entry, cachedAnime.get());
      }
    }
  }


  /**
   * @since 2.9.0
   */
  private void updateThumbnail(final MinimalEntry entry, final Anime cachedAnime) {
    MinimalEntry updatedEntry = null;

    if (entry instanceof FilterEntry) {
      updatedEntry = new FilterEntry(
          entry.getTitle(),
          entry.getInfoLink(),
          cachedAnime.getThumbnail());
    } else if (entry instanceof WatchListEntry) {
      updatedEntry = new WatchListEntry(
          entry.getTitle(),
          entry.getInfoLink(),
          cachedAnime.getThumbnail());
    }

    persistence.updateOrCreate(updatedEntry);
  }


  /**
   * @since 2.9.0
   */
  private void checkPictures(final MinimalEntry entry) {
    if (isInterrupt() || entry == null) {
      return;
    }

    MDC.put("infoLink", entry.getInfoLink().getUrl());

    if (entry != null && entry.getThumbnail() != null) {
      log.debug("Checking thumbnail.");

      int responseCodeThumbnail = NOT_FOUND;

      try {
        final CloseableHttpResponse response = httpclient
            .execute(RequestBuilder.head(entry.getThumbnail().toString()).build());
        responseCodeThumbnail = response.getStatusLine().getStatusCode();
      } catch (final IOException e) {
        log.error("Could not retrieve picture link status: ", e);
      }

      if (responseCodeThumbnail != OK) {
        final Optional<Anime> updatedAnime = cache.fetchAnime(entry.getInfoLink());
        if (updatedAnime.isPresent()) {
          log.debug("Updating thumbnail.");
          updateThumbnail(entry, updatedAnime.get());
        }
      }
    }
  }
}
