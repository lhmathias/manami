package io.github.manamiproject.manami.core.tasks

import io.github.manamiproject.manami.cache.Cache
import io.github.manamiproject.manami.cache.CacheFacade
import io.github.manamiproject.manami.core.ListRandomizer.randomizeOrder
import io.github.manamiproject.manami.dto.entities.Anime
import io.github.manamiproject.manami.dto.entities.InfoLink
import io.github.manamiproject.manami.dto.entities.MinimalEntry
import io.github.manamiproject.manami.persistence.Persistence


/**
 * This task is called whenever a new list is opened. It creates cache entries if necessary.
 * Always start {@link BackgroundTask}s using the {@link TaskConductor}!
 */
internal class CacheInitializationTask(
        private val persistence: Persistence
) : AbstractTask() {

    private val cache: Cache = CacheFacade

    override fun execute() {
        val animeList: MutableList<Anime> = persistence.fetchAnimeList()

        randomizeOrder(animeList)

        if(!animeList.isEmpty()) {
            initializeRecommendations(animeList)
        }

        val infoLinksToInitialize: MutableList<InfoLink> = mutableListOf()

        animeList.map { it.infoLink }.toCollection(infoLinksToInitialize)
        persistence.fetchWatchList().map { it.infoLink }.toCollection(infoLinksToInitialize)

        randomizeOrder(infoLinksToInitialize)

        if(!infoLinksToInitialize.isEmpty()) {
            initializeEntriesAndRelatedAnime(infoLinksToInitialize)
        }
    }

    private fun initializeRecommendations(animeList: MutableList<out MinimalEntry>) {
        animeList.forEach{ cache.fetchRecommendations(it.infoLink) }
    }

    private fun initializeEntriesAndRelatedAnime(infoLinksToInitialize: MutableList<InfoLink>) {
        infoLinksToInitialize.forEach {
            cache.fetchAnime(it)
            cache.fetchRelatedAnime(it)
        }
    }
}