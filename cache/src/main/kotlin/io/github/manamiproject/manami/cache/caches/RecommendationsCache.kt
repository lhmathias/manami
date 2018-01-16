package io.github.manamiproject.manami.cache.caches

import io.github.manami.cache.strategies.headlessbrowser.HeadlessBrowserRetrievalStrategy
import io.github.manamiproject.manami.dto.entities.InfoLink
import io.github.manamiproject.manami.dto.entities.RecommendationList


class RecommendationsCache(
        private val headlessBrowserRetrievalStrategy: HeadlessBrowserRetrievalStrategy
) : AbstractAnimeDataCache<InfoLink, RecommendationList>({ key ->
    headlessBrowserRetrievalStrategy.fetchRecommendations(key)
})