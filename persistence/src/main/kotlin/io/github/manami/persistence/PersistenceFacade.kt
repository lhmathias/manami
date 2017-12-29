package io.github.manami.persistence

import com.google.common.eventbus.EventBus
import io.github.manami.dto.entities.*
import io.github.manami.persistence.events.AnimeListChangedEvent
import io.github.manami.persistence.events.FilterListChangedEvent
import io.github.manami.persistence.events.WatchListChangedEvent
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
                eventBus.post(FilterListChangedEvent)

                if(removeFromWatchList(anime.infoLink)) {
                    eventBus.post(WatchListChangedEvent)
                }

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
            eventBus.post(FilterListChangedEvent)
            return true
        }

        return false
    }


    override fun addAnime(anime: Anime): Boolean {
        if (anime.isValidAnime() && strategy.addAnime(anime)) {
            if (anime.infoLink.isValid()) {
                if(removeFromFilterList(anime.infoLink)) {
                    eventBus.post(FilterListChangedEvent)
                }

                if(removeFromWatchList(anime.infoLink)) {
                    eventBus.post(WatchListChangedEvent)
                }
            }

            eventBus.post(AnimeListChangedEvent)
            return true
        }

        return false
    }


    override fun fetchAnimeList(): MutableList<Anime> {
        return strategy.fetchAnimeList()
    }


    override fun animeEntryExists(infoLink: InfoLink): Boolean {
        return strategy.animeEntryExists(infoLink)
    }


    override fun removeAnime(id: UUID): Boolean {
        if (strategy.removeAnime(id)) {
            eventBus.post(AnimeListChangedEvent)
            return true
        }

        return false
    }


    override fun watchAnime(anime: MinimalEntry): Boolean {
        if (anime.isValidMinimalEntry()) {
            if (strategy.watchAnime(anime)) {
                eventBus.post(WatchListChangedEvent)

                if(removeFromFilterList(anime.infoLink)) {
                    eventBus.post(FilterListChangedEvent)
                }

                return true
            }
        }

        return false
    }


    override fun fetchWatchList(): MutableList<WatchListEntry> {
        return strategy.fetchWatchList()
    }


    override fun watchListEntryExists(infoLink: InfoLink): Boolean {
        return strategy.watchListEntryExists(infoLink)
    }


    override fun removeFromWatchList(infoLink: InfoLink): Boolean {
        if (!infoLink.isValid()) {
            return false
        }

        if (strategy.removeFromWatchList(infoLink)) {
            eventBus.post(WatchListChangedEvent)
            return true
        }

        return false
    }


    override fun clearAll() {
        strategy.clearAll()
        eventBus.post(AnimeListChangedEvent)
        eventBus.post(FilterListChangedEvent)
        eventBus.post(WatchListChangedEvent)
    }


    override fun addAnimeList(list: MutableList<Anime>) {
        val validAnimeList: MutableList<Anime> = mutableListOf()

        list.filter { anime -> anime.isValidAnime() }.map(validAnimeList::add)

        strategy.addAnimeList(validAnimeList)
        eventBus.post(AnimeListChangedEvent)
    }


    override fun addFilterList(list: MutableList<FilterListEntry>) {
        val validEntryList: MutableList<FilterListEntry> = mutableListOf()

        validEntryList.filter { filterListEntry -> filterListEntry.isValidMinimalEntry() }.map(validEntryList::add)

        strategy.addFilterList(validEntryList)
        eventBus.post(FilterListChangedEvent)
    }


    override fun addWatchList(list: MutableList<WatchListEntry>) {
        val validEntryList: MutableList<WatchListEntry> = mutableListOf()

        validEntryList.filter { watchListEntry -> watchListEntry.isValidMinimalEntry() }.map(validEntryList::add)


        strategy.addWatchList(validEntryList)
        eventBus.post(WatchListChangedEvent)
    }


    override fun updateOrCreate(entry: MinimalEntry) {
        strategy.updateOrCreate(entry)
        when (entry) {
            is Anime -> eventBus.post(AnimeListChangedEvent)
            is FilterListEntry -> eventBus.post(FilterListChangedEvent)
            is WatchListEntry -> eventBus.post(WatchListChangedEvent)
        }
    }
}
