package io.github.manamiproject.manami.cache.populator

import io.github.manamiproject.manami.dto.entities.Anime
import io.github.manamiproject.manami.dto.entities.InfoLink

interface CacheEntrySource {

    fun getAnimeCacheEntries(): Map<InfoLink, Anime?>

    fun getRelatedAnimeCacheEntries(): Map<InfoLink, Set<InfoLink>>
}