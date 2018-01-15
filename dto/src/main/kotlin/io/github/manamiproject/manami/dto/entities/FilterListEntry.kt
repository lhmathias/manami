package io.github.manamiproject.manami.dto.entities

import io.github.manamiproject.manami.dto.entities.MinimalEntry.Companion.NO_IMG_THUMB
import java.net.URL

data class FilterListEntry(
        override var title: String,
        override var infoLink: InfoLink,
        override var thumbnail: URL = NO_IMG_THUMB
) : MinimalEntry {

    companion object {
        fun valueOf(anime: MinimalEntry) = FilterListEntry(anime.title, anime.infoLink, anime.thumbnail)
    }
}
