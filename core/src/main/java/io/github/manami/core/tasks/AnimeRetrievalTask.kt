package io.github.manami.core.tasks

import io.github.manami.cache.Cache
import io.github.manami.dto.entities.Anime
import io.github.manami.dto.entities.InfoLink
import java.util.*

/**
 * Retrieves an entity of an {@link Anime} by providing the info link URL.
 */
internal class AnimeRetrievalTask(
        private val cache: Cache,
        private val infoLink: InfoLink
) : AbstractTask(), BackgroundTask {


    override fun execute() {
        val anime: Optional<Anime> = cache.fetchAnime(infoLink)

        if (anime.isPresent) {
            anime.get() //FIXME: Do I really need a task for that? If so the result has to be returned somehow
        }
    }
}
