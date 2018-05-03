package io.github.manamiproject.manami.persistence.inmemory.watchlist

import io.github.manamiproject.manami.entities.*
import io.github.manamiproject.manami.entities.comparator.MinimalEntryCompByTitleAsc
import io.github.manamiproject.manami.persistence.WatchList
import java.util.concurrent.ConcurrentHashMap


internal class InMemoryWatchList : WatchList {

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

        watchList[entry.infoLink] = entry

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
            watchList[entry.infoLink] = entry
        }
    }
}
