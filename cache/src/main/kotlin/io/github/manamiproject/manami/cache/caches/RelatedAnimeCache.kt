package io.github.manamiproject.manami.cache.caches

import io.github.manamiproject.manami.cache.AnimeFetcher
import io.github.manamiproject.manami.entities.InfoLink

internal class RelatedAnimeCache(
        private val remoteFetcher: AnimeFetcher
) : AbstractAnimeDataCache<InfoLink, Set<InfoLink>>({ key ->
    remoteFetcher.fetchRelatedAnime(key)
})