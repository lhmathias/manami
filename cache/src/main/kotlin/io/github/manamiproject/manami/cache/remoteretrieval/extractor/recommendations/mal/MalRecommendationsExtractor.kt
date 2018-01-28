package io.github.manamiproject.manami.cache.remoteretrieval.extractor.recommendations.mal

import io.github.manamiproject.manami.cache.remoteretrieval.extractor.recommendations.RecommendationsExtractor
import io.github.manamiproject.manami.dto.entities.DOMAINS
import io.github.manamiproject.manami.dto.entities.InfoLink
import io.github.manamiproject.manami.dto.entities.RecommendationList

internal class MalRecommendationsExtractor : RecommendationsExtractor {
    override fun extractRecommendations(html: String): RecommendationList {
        return RecommendationList()
    }

    override fun isResponsible(infoLink: InfoLink) = infoLink.toString().contains(DOMAINS.MAL.value)
}