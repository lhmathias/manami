package io.github.manami.dto.entities

import io.github.manami.dto.entities.MinimalEntry.Companion.NO_IMG_THUMB
import java.net.URL

data class FilterListEntry @JvmOverloads constructor(
        override var title: String,
        override var infoLink: InfoLink,
        override var thumbnail: URL = NO_IMG_THUMB
) : MinimalEntry {

    companion object {
        @JvmStatic
        fun valueOf(anime: MinimalEntry) = FilterListEntry(anime.title, anime.infoLink, anime.thumbnail)
    }
}
