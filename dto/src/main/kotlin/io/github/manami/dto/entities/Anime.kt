package io.github.manami.dto.entities

import io.github.manami.dto.AnimeType
import java.net.URL
import java.util.*
import java.util.UUID.randomUUID

/**
 * Represents a "physical" Anime with all it's saved meta information stored in your HDD.
 */
data class Anime @JvmOverloads constructor(
        override var title: String,
        override var infoLink: InfoLink,
        private var numberOfEpisodes: Int = 0,
        /** Type of the Anime (e.g.: TV, Special, OVA, ONA, etc.). */
        var type: AnimeType = AnimeType.TV,
        /** Location on the HDD. */
        var location: String = "/",
        /** Url for a picture. */
        override var thumbnail: URL = MinimalEntry.NO_IMG_THUMB,
        var picture: URL = MinimalEntry.NO_IMG,
        var id: UUID = randomUUID()
) : MinimalEntry {

    var episodes: Int = numberOfEpisodes
        set(value) {
            if (value >= 0) {
                field = value
            }
        }


    fun isValidAnime() = isValidMinimalEntry() && location.isNotBlank()

    override fun isValidMinimalEntry() = title.isNotBlank()
}