package io.github.manami.cache.extractor.relatedanime;

import java.util.List;

/**
 * Can extract links to related animes.
 *
 * @author manami-project
 * @since 2.3.0
 */
public interface RelatedAnimeExtractor {

    /**
     * Extracts links to related animes.
     *
     * @since 2.3.0
     * @return A list of URLs for related animes.
     */
    List<String> extractRelatedAnimes();
}
