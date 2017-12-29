package io.github.manami.persistence.inmemory

import io.github.manami.dto.entities.*
import io.github.manami.persistence.PersistenceHandler
import io.github.manami.persistence.inmemory.animelist.InMemoryAnimeListHandler
import io.github.manami.persistence.inmemory.filterlist.InMemoryFilterListHandler
import io.github.manami.persistence.inmemory.watchlist.InMemoryWatchListHandler
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@Named("inMemoryStrategy")
internal class InMemoryPersistenceHandler @Inject constructor(
        private val animeListHandler: InMemoryAnimeListHandler,
        private val filterListHandler: InMemoryFilterListHandler,
        private val watchListHandler: InMemoryWatchListHandler
) : PersistenceHandler {


    override fun filterAnime(anime: MinimalEntry): Boolean {
        return filterListHandler.filterAnime(anime)
    }


    override fun fetchFilterList(): MutableList<FilterListEntry> {
        return filterListHandler.fetchFilterList()
    }


    override fun filterListEntryExists(infoLink: InfoLink): Boolean {
        return filterListHandler.filterListEntryExists(infoLink)
    }


    override fun removeFromFilterList(infoLink: InfoLink): Boolean {
        return filterListHandler.removeFromFilterList(infoLink)
    }


    override fun addAnime(anime: Anime): Boolean {
        return animeListHandler.addAnime(anime)
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


    override fun watchAnime(anime: MinimalEntry): Boolean {
        return watchListHandler.watchAnime(anime)
    }


    override fun fetchWatchList(): MutableList<WatchListEntry> {
        return watchListHandler.fetchWatchList()
    }


    override fun watchListEntryExists(infoLink: InfoLink): Boolean {
        return watchListHandler.watchListEntryExists(infoLink)
    }


    override fun removeFromWatchList(infoLink: InfoLink): Boolean {
        return watchListHandler.removeFromWatchList(infoLink)
    }


    override fun clearAll() {
        animeListHandler.clear()
        watchListHandler.clear()
        filterListHandler.clear()
    }


    override fun addAnimeList(list: MutableList<Anime>) {
        list.forEach { anime -> animeListHandler.addAnime(anime) }
    }


    override fun addFilterList(list: MutableList<FilterListEntry>) {
        list.forEach { anime -> filterListHandler.filterAnime(anime) }
    }


    override fun addWatchList(list: MutableList<WatchListEntry>) {
        list.forEach { anime -> watchListHandler.watchAnime(anime) }
    }


    override fun updateOrCreate(entry: MinimalEntry) {
        when (entry) {
            is Anime -> animeListHandler.updateOrCreate(entry)
            is FilterListEntry -> filterListHandler.updateOrCreate(entry)
            is WatchListEntry -> watchListHandler.updateOrCreate(entry)
        }
    }
}
