package io.github.manamiproject.manami.cache.remoteretrieval.extractor.recommendations.mal

import io.github.manamiproject.manami.cache.remoteretrieval.extractor.recommendations.RecommendationsExtractor
import io.github.manamiproject.manami.dto.entities.DOMAINS
import io.github.manamiproject.manami.dto.entities.InfoLink
import io.github.manamiproject.manami.dto.entities.RecommendationList

internal class MalRecommendationsExtractor : RecommendationsExtractor {
    override fun extractRecommendations(html: String): RecommendationList? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isResponsible(infoLink: InfoLink) = infoLink.toString().contains(DOMAINS.MAL.value)
}