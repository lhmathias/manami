package io.github.manami.dto.entities

import io.github.manami.dto.AnimeType
import java.net.URL
import java.util.*
import java.util.UUID.randomUUID

/**
 * Represents an Anime with all it's saved meta information.
 */

data class Anime @JvmOverloads constructor(
        override var title: String,
        override var infoLink: InfoLink,
        private var initialEpisodes: Int = 0,
        /** Type of the Anime (e.g.: TV, Special, OVA, ONA, etc.). */
        var type: AnimeType = AnimeType.TV,
        /** Location on the HDD. */
        var location: String = "",
        /** Url for a picture. */
        override var thumbnail: URL = MinimalEntry.NO_IMG_THUMB,
        var picture: URL = MinimalEntry.NO_IMG,
        var id: UUID = randomUUID()
) : MinimalEntry {

    var episodes: Int = initialEpisodes
        set(value) {
            if (value >= 0) {
                field = value
            }
        }


    fun getTypeAsString() = type.value


    companion object {
        /**
         * Checks if an anime entry is valid.
         *
         * @param anime
         * @return
         */
        @JvmStatic
        fun isValidAnime(anime: Anime) = anime.title.isNotBlank() && anime.location.isNotBlank()
    }
}