package io.github.manamiproject.manami.cache.caches

import io.github.manami.cache.strategies.headlessbrowser.HeadlessBrowserRetrievalStrategy
import io.github.manamiproject.manami.dto.entities.Anime
import io.github.manamiproject.manami.dto.entities.InfoLink
import java.util.*


class AnimeCache(
        private val headlessBrowserRetrievalStrategy: HeadlessBrowserRetrievalStrategy
) : AbstractAnimeDataCache<InfoLink, Optional<Anime>>({ key ->
    headlessBrowserRetrievalStrategy.fetchAnime(key)
})