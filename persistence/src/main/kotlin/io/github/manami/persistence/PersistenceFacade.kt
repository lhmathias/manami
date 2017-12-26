package io.github.manami.persistence

import com.google.common.eventbus.EventBus
import io.github.manami.dto.entities.*
import io.github.manami.persistence.events.AnimeListChangedEvent
import java.util.*
import javax.inject.Inject
import javax.inject.Named

/**
 * This is a facade which is used by the application to hide which strategy is actually used.
 */
@Named
class PersistenceFacade
    @Inject
    @Named("inMemoryStrategy") //Currently used db persistence strategy.
    constructor(
        val strategy: PersistenceHandler,
        val eventBus: EventBus
) : PersistenceHandler {

    override fun filterAnime(anime: MinimalEntry): Boolean {
        if (anime.isValidMinimalEntry()) {
            if (strategy.filterAnime(anime)) {
                eventBus.post(AnimeListChangedEvent())
                return true
            }

        }
        return false
    }


    override fun fetchFilterList(): MutableList<FilterListEntry> {
        return strategy.fetchFilterList()
    }


    override fun filterListEntryExists(infoLink: InfoLink): Boolean {
        return strategy.filterListEntryExists(infoLink)
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
        if (anime.isValidAnime()) {
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
        if (anime.isValidMinimalEntry()) {
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
