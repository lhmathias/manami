package io.github.manamiproject.manami.cache.caches

import io.github.manami.cache.strategies.headlessbrowser.HeadlessBrowserRetrievalStrategy
import io.github.manamiproject.manami.dto.entities.InfoLink

class RelatedAnimeCache(
        private val headlessBrowserRetrievalStrategy: HeadlessBrowserRetrievalStrategy
) : AbstractAnimeDataCache<InfoLink, Set<InfoLink>>({ key ->
    headlessBrowserRetrievalStrategy.fetchRelatedAnime(key)
})