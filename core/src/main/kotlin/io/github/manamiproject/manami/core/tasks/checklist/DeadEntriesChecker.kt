package io.github.manamiproject.manami.core.tasks.checklist

import io.github.manamiproject.manami.cache.Cache
import io.github.manamiproject.manami.common.EventBus
import io.github.manamiproject.manami.core.events.checklist.DeadFilterListEntryChecklistEvent
import io.github.manamiproject.manami.core.events.checklist.DeadWatchListEntryChecklistEvent
import io.github.manamiproject.manami.entities.FilterListEntry
import io.github.manamiproject.manami.entities.WatchListEntry

internal class DeadEntriesChecker(
        private val watchList: List<WatchListEntry>,
        private val filterList: List<FilterListEntry>,
        private val cache: Cache
) : Checker {

    override fun estimateWorkload(): Int {
        return watchList.size + filterList.size
    }

    override fun check() {
        watchList
            .filter(WatchListEntry::isValid)
            .forEach {
                val anime = cache.fetchAnime(it.infoLink)

                if(anime == null) {
                    EventBus.publish(DeadWatchListEntryChecklistEvent(it))
                }
            }

        filterList
                .filter(FilterListEntry::isValid)
                .forEach {
                    val anime = cache.fetchAnime(it.infoLink)

                    if(anime == null) {
                        EventBus.publish(DeadFilterListEntryChecklistEvent(it))
                    }
                }
    }
}