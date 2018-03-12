package io.github.manamiproject.manami.cache.populator

import io.github.manamiproject.manami.cache.caches.AnimeCache
import io.github.manamiproject.manami.cache.caches.RelatedAnimeCache

internal class CachePopulator(
        private val animeEntryCache: AnimeCache,
        private val relatedAnimeCache: RelatedAnimeCache,
        private val cacheEntrySource: CacheEntrySource
) {
    fun populate() {
        cacheEntrySource.getAnimeCacheEntries().forEach { infoLink, anime ->
            animeEntryCache.populate(infoLink, anime)
        }

        cacheEntrySource.getRelatedAnimeCacheEntries().forEach { infoLink, relatedAnime ->
            relatedAnimeCache.populate(infoLink, relatedAnime)
        }
    }
}