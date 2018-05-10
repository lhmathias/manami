package io.github.manamiproject.manami.core.tasks.checklist

import io.github.manamiproject.manami.cache.Cache
import io.github.manamiproject.manami.common.EventBus
import io.github.manamiproject.manami.core.events.checklist.DeadInfoLinkForAnimeChecklistEvent
import io.github.manamiproject.manami.core.events.checklist.EpisodesDifferChecklistEvent
import io.github.manamiproject.manami.core.events.checklist.TitlesDifferChecklistEvent
import io.github.manamiproject.manami.core.events.checklist.TypesDifferChecklistEvent
import io.github.manamiproject.manami.entities.Anime

internal class MetaDataChecker(
        private val animeList: List<Anime>,
        private val cache: Cache
) : Checker {

    override fun estimateWorkload(): Int {
        return animeList.size
    }

    override fun check() {
        animeList
                .filter(Anime::isValid)
                .filter { it.infoLink.isValid() }
                .forEach(this::fetchCacheEntryAndDiff)
    }

    private fun fetchCacheEntryAndDiff(anime: Anime) {
        val cacheEntry = cache.fetchAnime(anime.infoLink)

        if(cacheEntry == null) {
            EventBus.publish(DeadInfoLinkForAnimeChecklistEvent(anime))
        } else {
            diff(anime, cacheEntry)
        }
    }

    private fun diff(anime: Anime, cacheEntry: Anime) {
        if (anime.title != cacheEntry.title) {
            EventBus.publish(TitlesDifferChecklistEvent(anime, cacheEntry.title))
        }

        if (anime.episodes != cacheEntry.episodes) {
            EventBus.publish(EpisodesDifferChecklistEvent(anime, cacheEntry.episodes))
        }

        if (anime.type != cacheEntry.type) {
           EventBus.publish(TypesDifferChecklistEvent(anime, cacheEntry.type))
        }
    }
}