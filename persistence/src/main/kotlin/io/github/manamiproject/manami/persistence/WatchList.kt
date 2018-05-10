package io.github.manamiproject.manami.persistence

import io.github.manamiproject.manami.entities.InfoLink
import io.github.manamiproject.manami.entities.MinimalEntry
import io.github.manamiproject.manami.entities.WatchListEntry

interface WatchList {

    fun fetchWatchList(): List<WatchListEntry>

    fun watchListEntryExists(infoLink: InfoLink): Boolean

    fun watchAnime(anime: MinimalEntry): Boolean

    fun removeFromWatchList(anime: MinimalEntry): Boolean
}
