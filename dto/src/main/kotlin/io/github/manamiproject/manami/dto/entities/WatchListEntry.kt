package io.github.manamiproject.manami.dto.entities

import java.net.URL

data class WatchListEntry(
        override var title: Title,
        override var infoLink: InfoLink,
        override var thumbnail: URL = MinimalEntry.NO_IMG_THUMB
) : MinimalEntry {

    companion object {
        fun valueOf(anime: MinimalEntry) = WatchListEntry(anime.title, anime.infoLink, anime.thumbnail)
    }
}
