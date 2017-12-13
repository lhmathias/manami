package io.github.manami.persistence.inmemory.watchlist

import com.google.common.collect.ImmutableList
import io.github.manami.dto.comparator.MinimalEntryComByTitleAsc
import io.github.manami.dto.entities.*
import io.github.manami.persistence.WatchListHandler
import java.util.*
import java.util.concurrent.ConcurrentHashMap


//FIXME: @Named
class InMemoryWatchListHandler : WatchListHandler {

    private val watchList: MutableMap<InfoLink, WatchListEntry> = ConcurrentHashMap()


    override fun fetchWatchList(): MutableList<WatchListEntry> {
        val sortList: MutableList<WatchListEntry> = watchList.values.toMutableList()
        Collections.sort(sortList, MinimalEntryComByTitleAsc())
        return ImmutableList.copyOf(sortList)
    }


    override fun watchListEntryExists(infoLink: InfoLink): Boolean {
        return watchList.containsKey(infoLink)
    }


    override fun watchAnime(anime: MinimalEntry): Boolean {
        if (!isValidMinimalEntry(anime) || watchList.containsKey(anime.infoLink)) {
            return false
        }

        val entry = when (anime) {
            is Anime, is FilterEntry -> WatchListEntry.valueOf(anime)
            is WatchListEntry -> anime
            else -> return false
        }

        watchList.put(anime.infoLink, entry)

        return true
    }


    override fun removeFromWatchList(infoLink: InfoLink): Boolean {
        if (infoLink.isValid()) {
            return watchList.remove(infoLink) != null
        }

        return false
    }

    fun clear() {
        watchList.clear()
    }


    fun updateOrCreate(entry: WatchListEntry) {
        if (isValidMinimalEntry(entry)) {
            watchList.put(entry.infoLink, entry)
        }
    }
}
