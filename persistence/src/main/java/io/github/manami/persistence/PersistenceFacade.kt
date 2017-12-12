package io.github.manami.persistence

import com.google.common.eventbus.EventBus
import io.github.manami.dto.entities.*
import io.github.manami.dto.entities.Anime.Companion.isValidAnime
import io.github.manami.dto.events.AnimeListChangedEvent
import java.util.*

/**
 * This is a facade which is used by the application to hide which strategy is actually used.
 */
//FIXME: @Named
//FIXME: @Inject
class PersistenceFacade(
        /** Currently used db persistence strategy. */
        //FIXME: @Named("inMemoryStrategy")
        val strategy: PersistenceHandler,
        val eventBus: EventBus
) : PersistenceHandler {

    override fun filterAnime(anime: MinimalEntry): Boolean {
        if (isValidMinimalEntry(anime)) {
            if (strategy.filterAnime(anime)) {
                eventBus.post(AnimeListChangedEvent())
                return true
            }
        }

        return false
    }


    override fun fetchFilterList(): MutableList<FilterEntry> {
        return strategy.fetchFilterList()
    }


    override fun filterEntryExists(infoLink: InfoLink): Boolean {
        return strategy.filterEntryExists(infoLink)
    }


    override fun removeFromFilterList(infoLink: InfoLink): Boolean {
        if (!infoLink.isValid()) {
            return false
        }

        if (strategy.removeFromFilterList(infoLink)) {
            eventBus.post(AnimeListChangedEvent())
            return true
        }

        return false
    }


    override fun addAnime(anime: Anime): Boolean {
        if (isValidAnime(anime)) {
            if (strategy.addAnime(anime)) {
                eventBus.post(AnimeListChangedEvent())
                return true
            }
        }

        return false
    }


    override fun fetchAnimeList(): MutableList<Anime> {
        return strategy.fetchAnimeList()
    }


    override fun animeEntryExists(infoLink: InfoLink): Boolean {
        return strategy.animeEntryExists(infoLink)
    }


    override fun fetchWatchList(): MutableList<WatchListEntry> {
        return strategy.fetchWatchList()
    }


    override fun watchListEntryExists(infoLink: InfoLink): Boolean {
        return strategy.watchListEntryExists(infoLink)
    }


    override fun watchAnime(anime: MinimalEntry): Boolean {
        if (isValidMinimalEntry(anime)) {
            if (strategy.watchAnime(anime)) {
                eventBus.post(AnimeListChangedEvent())
                return true
            }
        }

        return false
    }


    override fun removeFromWatchList(infoLink: InfoLink): Boolean {
        if (!infoLink.isValid()) {
            return false
        }

        if (strategy.removeFromWatchList(infoLink)) {
            eventBus.post(AnimeListChangedEvent())
            return true
        }

        return false
    }


    override fun removeAnime(id: UUID): Boolean {
        if (strategy.removeAnime(id)) {
            eventBus.post(AnimeListChangedEvent())
            return true
        }

        return false
    }


    override fun clearAll() {
        strategy.clearAll()
        eventBus.post(AnimeListChangedEvent())
    }


    override fun addAnimeList(list: MutableList<Anime>) {
        strategy.addAnimeList(list)
        eventBus.post(AnimeListChangedEvent())
    }


    override fun addFilterList(list: MutableList<out MinimalEntry>) {
        strategy.addFilterList(list)
        eventBus.post(AnimeListChangedEvent())
    }


    override fun addWatchList(list: MutableList<out MinimalEntry>) {
        strategy.addWatchList(list)
        eventBus.post(AnimeListChangedEvent())
    }


    override fun updateOrCreate(entry: MinimalEntry) {
        strategy.updateOrCreate(entry)
        eventBus.post(AnimeListChangedEvent())
    }
}
