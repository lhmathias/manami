package io.github.manami.persistence.inmemory.filterlist;

import io.github.manami.dto.comparator.MinimalEntryComByTitleAsc
import io.github.manami.dto.entities.*
import io.github.manami.persistence.FilterListHandler
import java.util.*
import java.util.concurrent.ConcurrentHashMap

//FIXME: @Named
class InMemoryFilterListHandler : FilterListHandler {

    private val filterList: MutableMap<InfoLink, FilterEntry> = ConcurrentHashMap()

    override fun filterAnime(anime: MinimalEntry): Boolean {
        if (!isValidMinimalEntry(anime) || filterList.containsKey(anime.infoLink)) {
            return false
        }

        val entry = when (anime) {
            is Anime, is WatchListEntry -> FilterEntry.valueOf(anime)
            is FilterEntry -> anime
            else -> return false
        }

        filterList.put(entry.infoLink, entry)

        return true
    }


    override fun fetchFilterList(): MutableList<FilterEntry> {
        val sortList = filterList.values.toMutableList()
        Collections.sort(sortList, MinimalEntryComByTitleAsc())
        return sortList.toMutableList()
    }


    override fun filterEntryExists(infoLink: InfoLink): Boolean {
        return filterList.containsKey(infoLink)
    }


    override fun removeFromFilterList(infoLink: InfoLink): Boolean {
        if (infoLink.isValid()) {
            return filterList.remove(infoLink) != null
        }

        return false
    }

    fun clear() {
        filterList.clear()
    }


    fun updateOrCreate(entry: FilterEntry) {
        if (isValidMinimalEntry(entry)) {
            filterList.put(entry.infoLink, entry)
        }
    }
}
