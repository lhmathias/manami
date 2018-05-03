package io.github.manamiproject.manami.cache.remoteretrieval.extractor.recommendations

import io.github.manamiproject.manami.cache.remoteretrieval.extractor.Extractor
import io.github.manamiproject.manami.entities.RecommendationList

internal interface RecommendationsExtractor : Extractor {

    fun extractRecommendations(html: String): RecommendationList
}