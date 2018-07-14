package io.github.manamiproject.manami.cache.caches

import io.github.manamiproject.manami.cache.remoteretrieval.RemoteFetcher
import io.github.manamiproject.manami.entities.InfoLink
import io.github.manamiproject.manami.entities.RecommendationList


internal class RecommendationsCache(
        private val remoteRetrieval: RemoteFetcher
) : AbstractAnimeDataCache<InfoLink, RecommendationList>({ key ->
    remoteRetrieval.fetchRecommendations(key)
})