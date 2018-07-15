package io.github.manamiproject.manami.cache.remoteretrieval.extractor.relatedanime

import io.github.manamiproject.manami.cache.remoteretrieval.extractor.Extractor
import io.github.manamiproject.manami.entities.InfoLink

/**
 * Extracts a list of related anime from a source. A raw HTML page for example.
 */
internal interface RelatedAnimeExtractor : Extractor {

    /**
     * Extracts the list of related anime from a source String.
     * @param source Any given source. A raw HTML page for example.
     * @return A Set of directly related anime represented with their InfoLinks.
     */
    fun extractRelatedAnime(source: String): Set<InfoLink>
}