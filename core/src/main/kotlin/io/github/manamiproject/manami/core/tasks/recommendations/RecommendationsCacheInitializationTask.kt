package io.github.manamiproject.manami.core.tasks.recommendations

import io.github.manamiproject.manami.cache.Cache
import io.github.manamiproject.manami.cache.CacheFacade
import io.github.manamiproject.manami.common.randomizeOrder
import io.github.manamiproject.manami.core.tasks.AbstractTask
import io.github.manamiproject.manami.persistence.Persistence


/**
 * This task is called whenever a new list is opened. It creates cache entries for recommendations
 * if necessary.
 * Always start {@link BackgroundTask}s using the {@link TaskConductor}!
 */
internal class RecommendationsCacheInitializationTask(
        private val cache: Cache,
        private val persistence: Persistence
) : AbstractTask() {

    override fun execute() {
        persistence.fetchAnimeList()
            .randomizeOrder()
            .forEach{ cache.fetchRecommendations(it.infoLink) }
    }
}