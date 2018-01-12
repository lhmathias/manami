package io.github.manami.dto.entities

import io.github.manami.dto.AnimeType
import java.net.URL
import java.util.*
import java.util.UUID.randomUUID

/**
 * Represents a "physical" Anime with all it's saved meta information stored in your HDD.
 */
data class Anime(
        /** Main title of the anime. */
        override var title: String,
        /** URL to a website which contains additional information. */
        override var infoLink: InfoLink,
        /** Amount of episodes. 1 for Movies. */
        private var numberOfEpisodes: Int = 0,
        /** Type of the Anime (e.g.: TV, Special, OVA, ONA, etc.). */
        var type: AnimeType = AnimeType.TV,
        /** Location on the HDD. */
        var location: String = "/",
        /** Url for a thumbnail. */
        override var thumbnail: URL = MinimalEntry.NO_IMG_THUMB,
        /** Url for a picture. */
        var picture: URL = MinimalEntry.NO_IMG,
        /** Identifier within the current anime list. */
        var id: UUID = randomUUID()
) : MinimalEntry {

    var episodes: Int = numberOfEpisodes
        set(value) {
            if (value >= 0) {
                field = value
            }
        }


    override fun isValid() = title.isNotBlank() && location.isNotBlank()
}