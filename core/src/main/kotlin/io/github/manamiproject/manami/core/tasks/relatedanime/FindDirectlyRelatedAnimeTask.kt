package io.github.manamiproject.manami.core.tasks.relatedanime

import io.github.manamiproject.manami.cache.Cache
import io.github.manamiproject.manami.common.EventBus
import io.github.manamiproject.manami.common.LoggerDelegate
import io.github.manamiproject.manami.core.events.ProgressState
import io.github.manamiproject.manami.core.events.relatedanime.RelatedAnimeIdentifiedEvent
import io.github.manamiproject.manami.core.tasks.AbstractTask
import io.github.manamiproject.manami.entities.InfoLink
import io.github.manamiproject.manami.persistence.Persistence
import org.slf4j.Logger

/**
 * Finds all directly related anime for infolinks.
 * Always start {@link BackgroundTask}s using the {@link ServiceRepository}!
 */
internal class FindDirectlyRelatedAnimeTask(
        private val cache: Cache,
        private val persistence: Persistence,
        private val list: Set<InfoLink>
) : AbstractTask() {

    private val log: Logger by LoggerDelegate()

    override fun execute() {
        var counter = 0

        val relatedAnimeResult: Set<InfoLink> =  list.map {
            EventBus.publish(
                ProgressState(
                    ++counter,
                    list.size
                )
            )
            cache.fetchRelatedAnime(it)
        }
        .flatten()
        .filter { it.isValid() }
        .filter { !persistence.animeEntryExists(it) }
        .filter { !persistence.watchListEntryExists(it) }
        .filter { !persistence.filterListEntryExists(it) }
        .toHashSet()

        EventBus.publish(RelatedAnimeIdentifiedEvent(relatedAnimeResult))
    }
}