package io.github.manamiproject.manami.core.tasks

import io.github.manamiproject.manami.cache.Cache
import io.github.manamiproject.manami.common.LoggerDelegate
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
        val directlyRelatedAnime: Set<InfoLink> = mutableSetOf()

        val relatedAnimeResult: Set<InfoLink> =  list.map {
            cache.fetchRelatedAnime(it)
        }.toSet()

        directlyRelatedAnime.filter { it.isValid() }
        .filter { !persistence.animeEntryExists(it) }
        .filter { !persistence.watchListEntryExists(it) }
        .filter { !persistence.filterListEntryExists(it) }
        .filter { !relatedAnimeResult.contains(it) }

    }
}