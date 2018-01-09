package io.github.manami.core.services

import io.github.manami.cache.Cache
import io.github.manami.dto.entities.Anime
import io.github.manami.dto.entities.InfoLink
import java.util.*

/**
 * Retrieves an entity of an {@link Anime} by providing the info link URL.
 */
class AnimeRetrievalService(
        private val cache: Cache,
        private val infoLink: InfoLink
) : AbstractService<Void>(), BackgroundService {


    override fun execute() {
        val anime: Optional<Anime> = cache.fetchAnime(infoLink)

        if (anime.isPresent && !isInterrupt()) {
            setChanged()
            notifyObservers(true)
            setChanged()
            notifyObservers(anime.get())
        }
    }
}
