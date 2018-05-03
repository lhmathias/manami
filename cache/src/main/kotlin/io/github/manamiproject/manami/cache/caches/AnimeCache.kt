package io.github.manamiproject.manami.cache.caches

import io.github.manamiproject.manami.cache.AnimeRetrieval
import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.entities.InfoLink


internal class AnimeCache(
        private val remoteRetrieval: AnimeRetrieval
) : AbstractAnimeDataCache<InfoLink, Anime?>({ key ->
    remoteRetrieval.fetchAnime(key)
})