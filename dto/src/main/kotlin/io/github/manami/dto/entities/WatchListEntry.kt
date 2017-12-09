package io.github.manami.dto.entities

import java.net.URL

data class WatchListEntry @JvmOverloads constructor(
        override var title: String,
        override var infoLink: InfoLink,
        override var thumbnail: URL = MinimalEntry.NO_IMG
) : MinimalEntry {

    companion object {
        @JvmStatic
        fun valueOf(anime: MinimalEntry) = WatchListEntry(anime.title, anime.infoLink, anime.thumbnail)
    }
}
