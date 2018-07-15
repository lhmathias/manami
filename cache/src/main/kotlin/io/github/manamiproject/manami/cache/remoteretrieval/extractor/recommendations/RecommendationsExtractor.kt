package io.github.manamiproject.manami.cache.remoteretrieval.extractor.recommendations

import io.github.manamiproject.manami.cache.remoteretrieval.extractor.Extractor
import io.github.manamiproject.manami.entities.RecommendationList

/**
 * Extracts recommendations from a source. A raw HTML page for example.
 */
internal interface RecommendationsExtractor : Extractor {

    /**
     * Extracts a list of recommendations from a source String.
     * @param source Any given source. A raw HTML page for example.
     * @return A RecommendationList.
     */
    fun extractRecommendations(source: String): RecommendationList
}