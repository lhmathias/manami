package io.github.manamiproject.manami.persistence

import io.github.manamiproject.manami.dto.entities.InfoLink
import io.github.manamiproject.manami.dto.entities.MinimalEntry
import io.github.manamiproject.manami.dto.entities.WatchListEntry

interface WatchList {

    fun fetchWatchList(): MutableList<WatchListEntry>

    fun watchListEntryExists(infoLink: InfoLink): Boolean

    fun watchAnime(anime: MinimalEntry): Boolean

    fun removeFromWatchList(anime: MinimalEntry): Boolean
}
