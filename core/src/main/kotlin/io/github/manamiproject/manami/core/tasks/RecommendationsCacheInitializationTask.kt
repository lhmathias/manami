package io.github.manamiproject.manami.core.tasks

import io.github.manamiproject.manami.cache.Cache
import io.github.manamiproject.manami.cache.CacheFacade
import io.github.manamiproject.manami.common.randomizeOrder
import io.github.manamiproject.manami.persistence.Persistence


/**
 * This task is called whenever a new list is opened. It creates cache entries for recommendations
 * if necessary.
 * Always start {@link BackgroundTask}s using the {@link TaskConductor}!
 */
internal class RecommendationsCacheInitializationTask(
        private val persistence: Persistence
) : AbstractTask() {

    private val cache: Cache = CacheFacade

    override fun execute() {
        persistence.fetchAnimeList()
            .randomizeOrder()
            .forEach{ cache.fetchRecommendations(it.infoLink) }
    }
}