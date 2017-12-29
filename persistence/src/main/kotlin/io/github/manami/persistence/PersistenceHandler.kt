package io.github.manami.persistence


import io.github.manami.dto.entities.Anime
import io.github.manami.dto.entities.FilterListEntry
import io.github.manami.dto.entities.WatchListEntry

interface PersistenceHandler : ApplicationPersistence {

    fun clearAll()

    fun addAnimeList(list: MutableList<Anime>)

    fun addFilterList(list: MutableList<FilterListEntry>)

    fun addWatchList(list: MutableList<WatchListEntry>)
}
