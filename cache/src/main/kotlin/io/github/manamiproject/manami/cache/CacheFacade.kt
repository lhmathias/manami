package io.github.manamiproject.manami.cache

import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.entities.InfoLink
import io.github.manamiproject.manami.entities.RecommendationList
import io.github.manamiproject.manami.cache.offlinedatabase.OfflineDatabaseGitRepository
import io.github.manamiproject.manami.cache.caches.RecommendationsCache
import io.github.manamiproject.manami.cache.caches.RelatedAnimeCache
import io.github.manamiproject.manami.cache.caches.AnimeCache
import io.github.manamiproject.manami.cache.offlinedatabase.OfflineDatabaseUpdatedSuccessfullyEvent
import io.github.manamiproject.manami.cache.populator.CachePopulator
import io.github.manamiproject.manami.cache.remoteretrieval.RemoteRetrieval
import io.github.manamiproject.manami.cache.remoteretrieval.extractor.Extractors
import io.github.manamiproject.manami.cache.remoteretrieval.extractor.anime.mal.MalAnimeExtractor
import io.github.manamiproject.manami.cache.remoteretrieval.extractor.recommendations.mal.MalRecommendationsExtractor
import io.github.manamiproject.manami.cache.remoteretrieval.extractor.relatedanime.mal.MalRelatedAnimeExtractor
import io.github.manamiproject.manami.common.EventBus


object CacheFacade : Cache {

    private val remoteRetrieval = RemoteRetrieval(
        Extractors(
            MalAnimeExtractor(),
            MalRelatedAnimeExtractor(),
            MalRecommendationsExtractor()
        )
    )
    private val animeEntryCache = AnimeCache(remoteRetrieval)
    private val relatedAnimeCache = RelatedAnimeCache(remoteRetrieval)
    private val recommendationsCache = RecommendationsCache(remoteRetrieval)
    private val cachePopulator = CachePopulator(animeEntryCache, relatedAnimeCache, OfflineDatabaseGitRepository())

    init {
        cachePopulator.populate()
        EventBus.publish(OfflineDatabaseUpdatedSuccessfullyEvent)
    }

    override fun fetchAnime(infoLink: InfoLink): Anime? {
        if (!infoLink.isValid()) {
            return null
        }

        return animeEntryCache.get(infoLink)
    }

    override fun fetchRelatedAnime(infoLink: InfoLink): Set<InfoLink> {
        if (!infoLink.isValid()) {
            return mutableSetOf()
        }

        relatedAnimeCache.get(infoLink)?.let {
            return it
        }

        return hashSetOf()
    }

    override fun fetchRecommendations(infoLink: InfoLink): RecommendationList {
        if (!infoLink.isValid()) {
            return RecommendationList()
        }

        recommendationsCache.get(infoLink)?.let {
            return it
        }

        return RecommendationList()
    }

    override fun invalidate() {
        animeEntryCache.invalidate()
        relatedAnimeCache.invalidate()
        recommendationsCache.invalidate()
    }
}
