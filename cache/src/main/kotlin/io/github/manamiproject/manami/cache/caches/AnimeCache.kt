package io.github.manamiproject.manami.cache.caches

import io.github.manamiproject.manami.cache.AnimeFetcher
import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.entities.InfoLink


internal class AnimeCache(
        private val remoteFetcher: AnimeFetcher
) : AbstractAnimeDataCache<InfoLink, Anime?>({ key ->
    remoteFetcher.fetchAnime(key)
})