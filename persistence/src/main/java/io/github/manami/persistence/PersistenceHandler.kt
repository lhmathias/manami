package io.github.manami.persistence


import io.github.manami.dto.entities.Anime
import io.github.manami.dto.entities.MinimalEntry

interface PersistenceHandler : ApplicationPersistence {

    fun clearAll()

    fun addAnimeList(list: MutableList<Anime>)

    fun addFilterList(list: MutableList<out MinimalEntry>)

    fun addWatchList(list: MutableList<out MinimalEntry>)
}
