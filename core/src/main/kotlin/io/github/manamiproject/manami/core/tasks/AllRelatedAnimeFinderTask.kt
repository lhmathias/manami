package io.github.manamiproject.manami.core.tasks

import io.github.manamiproject.manami.cache.Cache
import io.github.manamiproject.manami.common.EventBus
import io.github.manamiproject.manami.common.LoggerDelegate
import io.github.manamiproject.manami.common.Queue
import io.github.manamiproject.manami.core.tasks.events.ProgressState
import io.github.manamiproject.manami.dto.entities.InfoLink
import io.github.manamiproject.manami.persistence.Persistence
import org.slf4j.Logger


/**
 * Finds all related anime for infolinks, not only the directly related.
 * Always start {@link BackgroundTask}s using the {@link ServiceRepository}!
 */
internal class AllRelatedAnimeFinderTask(
        private val cache: Cache,
        private val persistence: Persistence,
        private val list: List<InfoLink>
) : AbstractTask() {

    private val log: Logger by LoggerDelegate()

    override fun execute() {
        val result : MutableSet<InfoLink> = mutableSetOf()
        val toBeChecked : Queue<InfoLink> = Queue()
        var counter = 0

        list.map {
            EventBus.publish(ProgressState(++counter, list.size))
            cache.fetchRelatedAnime(it)
        }
        .flatten()
        .filter { it.isValid() }
        .filter { !persistence.animeEntryExists(it) }
        .filter { !persistence.watchListEntryExists(it) }
        .filter { !persistence.filterListEntryExists(it) }
        .filter { !list.contains(it) }
        .forEach {
            toBeChecked.enqueue(it)
            result.add(it)
        }

        val alreadyChecked : MutableSet<InfoLink> = mutableSetOf()

        while(!toBeChecked.isEmpty()) {
            toBeChecked.dequeue()?.let { infoLink ->
                cache.fetchRelatedAnime(infoLink)
                .filter { it.isValid() }
                .filter { !persistence.animeEntryExists(it) }
                .filter { !persistence.watchListEntryExists(it) }
                .filter { !persistence.filterListEntryExists(it) }
                .filter { !list.contains(it) }
                .filter { !alreadyChecked.contains(it) }
                .map(toBeChecked::enqueue)

                alreadyChecked.add(infoLink)
            }
        }

        //TODO: fire event with result
    }
}