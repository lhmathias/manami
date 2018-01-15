package io.github.manami.persistence.inmemory.watchlist

import io.github.manami.dto.comparator.MinimalEntryCompByTitleAsc
import io.github.manami.dto.entities.*
import io.github.manami.persistence.WatchListHandler
import java.util.concurrent.ConcurrentHashMap


internal class InMemoryWatchListHandler : WatchListHandler {

    private val watchList: MutableMap<InfoLink, WatchListEntry> = ConcurrentHashMap()


    override fun fetchWatchList(): MutableList<WatchListEntry> {
        return watchList.values.sortedWith(MinimalEntryCompByTitleAsc).toMutableList()
    }


    override fun watchListEntryExists(infoLink: InfoLink): Boolean {
        return watchList.containsKey(infoLink)
    }


    override fun watchAnime(anime: MinimalEntry): Boolean {
        if (!anime.isValid() || watchList.containsKey(anime.infoLink)) {
            return false
        }

        val entry = when (anime) {
            is Anime, is FilterListEntry -> WatchListEntry.valueOf(anime)
            is WatchListEntry -> anime
            else -> return false
        }

        watchList.put(anime.infoLink, entry)

        return true
    }


    override fun removeFromWatchList(anime: MinimalEntry): Boolean {
        if (anime.isValid()) {
            return watchList.remove(anime.infoLink) != null
        }

        return false
    }

    fun clear() {
        watchList.clear()
    }


    fun updateOrCreate(entry: WatchListEntry) {
        if (entry.isValid()) {
            watchList.put(entry.infoLink, entry)
        }
    }
}
