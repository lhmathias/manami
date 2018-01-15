package io.github.manami.cache.strategies.headlessbrowser.extractor.relatedanime;

import io.github.manami.cache.strategies.headlessbrowser.extractor.AnimeExtractor;
import io.github.manamiproject.manami.dto.entities.InfoLink;
import java.util.Set;

/**
 * Can extract links to related anime.
 */
public interface RelatedAnimeExtractor extends AnimeExtractor {

  /**
   * Extracts links to related anime.
   *
   * @return A list of URLs for related anime.
   */
  Set<InfoLink> extractRelatedAnime(String siteContent);
}
