package io.github.manamiproject.manami.cache.caches

import io.github.manamiproject.manami.cache.AnimeRetrieval
import io.github.manamiproject.manami.cache.remoteretrieval.RemoteRetrieval
import io.github.manamiproject.manami.dto.entities.InfoLink

internal class RelatedAnimeCache(
        private val remoteRetrieval: AnimeRetrieval
) : AbstractAnimeDataCache<InfoLink, Set<InfoLink>>({ key ->
    remoteRetrieval.fetchRelatedAnime(key)
})