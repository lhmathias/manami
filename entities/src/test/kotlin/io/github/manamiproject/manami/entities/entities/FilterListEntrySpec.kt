package io.github.manamiproject.manami.entities.entities

import io.github.manamiproject.manami.entities.AnimeType
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.net.URL

class FilterListEntrySpec : Spek({

    given("a valid anime") {
        val anime = Anime(
                "Death Note",
                InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}1535"),
                37,
                AnimeType.TV,
                "/anime/series/death_note",
                URL("https://myanimelist.cdn-dena.com/images/anime/9/9453t.jpg"),
                URL("https://myanimelist.cdn-dena.com/images/anime/9/9453.jpg")
        )

        on("creating watch list entry from anime") {
            val result = FilterListEntry.valueOf(anime)

            it("must generate a valid watch list entry") {
                assertThat(result).isNotNull()
                assertThat(result.title).isEqualTo(anime.title)
                assertThat(result.infoLink).isEqualTo(anime.infoLink)
                assertThat(result.thumbnail).isEqualTo(anime.thumbnail)
            }
        }
    }


    given("a valid watch list entry") {
        val watchListEntry = WatchListEntry(
                "Death Note",
                InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}1535"),
                URL("https://myanimelist.cdn-dena.com/images/anime/9/9453t.jpg")
        )

        on("creating watch list entry from filter entry") {
            val result = FilterListEntry.valueOf(watchListEntry)

            it("must generate a valid watch list entry") {
                assertThat(result).isNotNull()
                assertThat(result.title).isEqualTo(watchListEntry.title)
                assertThat(result.infoLink).isEqualTo(watchListEntry.infoLink)
                assertThat(result.thumbnail).isEqualTo(watchListEntry.thumbnail)
            }
        }
    }
})