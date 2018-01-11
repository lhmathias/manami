package io.github.manami.persistence

import io.github.manami.dto.entities.InfoLink
import io.github.manami.dto.entities.MinimalEntry
import io.github.manami.dto.entities.WatchListEntry

interface WatchListHandler {

    fun fetchWatchList(): MutableList<WatchListEntry>

    fun watchListEntryExists(infoLink: InfoLink): Boolean

    fun watchAnime(anime: MinimalEntry): Boolean

    fun removeFromWatchList(anime: MinimalEntry): Boolean
}
