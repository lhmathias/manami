package io.github.manamiproject.manami.cache.caches

import io.github.manamiproject.manami.cache.remoteretrieval.RemoteRetrieval
import io.github.manamiproject.manami.entities.InfoLink
import io.github.manamiproject.manami.entities.RecommendationList


internal class RecommendationsCache(
        private val remoteRetrieval: RemoteRetrieval
) : AbstractAnimeDataCache<InfoLink, RecommendationList>({ key ->
    remoteRetrieval.fetchRecommendations(key)
})