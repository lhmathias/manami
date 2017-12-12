package io.github.manami.persistence.inmemory

import io.github.manami.dto.entities.*
import io.github.manami.dto.entities.Anime.Companion.isValidAnime
import io.github.manami.persistence.PersistenceHandler
import io.github.manami.persistence.inmemory.animelist.InMemoryAnimeListHandler
import io.github.manami.persistence.inmemory.filterlist.InMemoryFilterListHandler
import io.github.manami.persistence.inmemory.watchlist.InMemoryWatchListHandler
import java.util.*

//FIXME: @Named("inMemoryStrategy")
//FIXME:   @Inject
class InMemoryPersistenceHandler(
        private val animeListHandler: InMemoryAnimeListHandler,
        private val filterListHandler: InMemoryFilterListHandler,
        private val watchListHandler: InMemoryWatchListHandler
) : PersistenceHandler {


    override fun filterAnime(anime: MinimalEntry): Boolean {
        if (isValidMinimalEntry(anime)) {
            if (anime.infoLink.isValid()) {
                watchListHandler.removeFromWatchList(anime.infoLink)
            }

            return filterListHandler.filterAnime(anime)
        }

        return false
    }


    override fun fetchFilterList(): MutableList<FilterEntry> {
        return filterListHandler.fetchFilterList()
    }


    override fun filterEntryExists(infoLink: InfoLink): Boolean {
        return filterListHandler.filterEntryExists(infoLink)
    }


    override fun removeFromFilterList(infoLink: InfoLink): Boolean {
        if (infoLink.isValid()) {
            return filterListHandler.removeFromFilterList(infoLink)
        }

        return false
    }


    override fun addAnime(anime: Anime): Boolean {
        if (isValidAnime(anime)) {
            if (anime.infoLink.isValid()) {
                filterListHandler.removeFromFilterList(anime.infoLink)
                watchListHandler.removeFromWatchList(anime.infoLink)
            }

            return animeListHandler.addAnime(anime)
        }

        return false
    }


    override fun fetchWatchList(): MutableList<WatchListEntry> {
        return watchListHandler.fetchWatchList()
    }


    override fun watchListEntryExists(infoLink: InfoLink): Boolean {
        return watchListHandler.watchListEntryExists(infoLink)
    }


    override fun watchAnime(anime: MinimalEntry): Boolean {
        if (isValidMinimalEntry(anime)) {
            if (anime.infoLink.isValid()) {
                filterListHandler.removeFromFilterList(anime.infoLink)
            }

            return watchListHandler.watchAnime(anime)
        }

        return false
    }


    override fun removeFromWatchList(infoLink: InfoLink): Boolean {
        if (infoLink.isValid()) {
            return watchListHandler.removeFromWatchList(infoLink)
        }

        return false
    }


    override fun fetchAnimeList(): MutableList<Anime> {
        return animeListHandler.fetchAnimeList()
    }


    override fun animeEntryExists(infoLink: InfoLink): Boolean {
        return animeListHandler.animeEntryExists(infoLink)
    }


    override fun removeAnime(id: UUID): Boolean {
        return animeListHandler.removeAnime(id)
    }


    override fun clearAll() {
        animeListHandler.clear()
        watchListHandler.clear()
        filterListHandler.clear()
    }


    override fun addAnimeList(list: MutableList<Anime>) {
        list.forEach { anime -> animeListHandler.addAnime(anime) }
    }


    override fun addFilterList(list: MutableList<out MinimalEntry>) {
        list.forEach { anime -> filterListHandler.filterAnime(anime) }
    }


    override fun addWatchList(list: MutableList<out MinimalEntry>) {
        list.forEach { anime -> watchListHandler.watchAnime(anime) }
    }


    override fun updateOrCreate(entry: MinimalEntry) {
        when (entry) {
            is Anime -> animeListHandler.updateOrCreate(entry)
            is FilterEntry -> filterListHandler.updateOrCreate(entry)
            is WatchListEntry -> watchListHandler.updateOrCreate(entry)
        }
    }
}
