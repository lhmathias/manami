package io.github.manami.persistence.inmemory.filterlist

import io.github.manami.dto.comparator.MinimalEntryCompByTitleAsc
import io.github.manami.dto.entities.*
import io.github.manami.persistence.FilterListHandler
import java.util.concurrent.ConcurrentHashMap


internal class InMemoryFilterListHandler : FilterListHandler {

    private val filterList: MutableMap<InfoLink, FilterListEntry> = ConcurrentHashMap()

    override fun filterAnime(anime: MinimalEntry): Boolean {
        if (!anime.isValid() || filterList.containsKey(anime.infoLink)) {
            return false
        }

        val entry = when (anime) {
            is Anime, is WatchListEntry -> FilterListEntry.valueOf(anime)
            is FilterListEntry -> anime
            else -> return false
        }

        filterList.put(entry.infoLink, entry)

        return true
    }


    override fun fetchFilterList(): MutableList<FilterListEntry> {
        return filterList.values.sortedWith(MinimalEntryCompByTitleAsc).toMutableList()
    }


    override fun filterListEntryExists(infoLink: InfoLink): Boolean {
        return filterList.containsKey(infoLink)
    }


    override fun removeFromFilterList(anime: MinimalEntry): Boolean {
        if (anime.isValid()) {
            return filterList.remove(anime.infoLink) != null
        }

        return false
    }

    fun clear() {
        filterList.clear()
    }


    fun updateOrCreate(entry: FilterListEntry) {
        if (entry.isValid()) {
            filterList.put(entry.infoLink, entry)
        }
    }
}
