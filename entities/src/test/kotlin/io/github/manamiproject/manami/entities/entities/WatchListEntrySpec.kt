package io.github.manamiproject.manami.entities.entities

import io.github.manamiproject.manami.entities.AnimeType
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.net.URL

@RunWith(JUnitPlatform::class)
class WatchListEntrySpec : Spek({

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
            val result = WatchListEntry.valueOf(anime)

            it("must generate a valid watch list entry") {
                assertThat(result).isNotNull()
                assertThat(result.title).isEqualTo(anime.title)
                assertThat(result.infoLink).isEqualTo(anime.infoLink)
                assertThat(result.thumbnail).isEqualTo(anime.thumbnail)
            }
        }
    }


    given("a valid filter list entry") {
        val filterEntry = FilterListEntry(
                "Death Note",
                InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}1535"),
                URL("https://myanimelist.cdn-dena.com/images/anime/9/9453t.jpg")
        )

        on("creating watch list entry from filter entry") {
            val result = WatchListEntry.valueOf(filterEntry)

            it("must generate a valid watch list entry") {
                assertThat(result).isNotNull()
                assertThat(result.title).isEqualTo(filterEntry.title)
                assertThat(result.infoLink).isEqualTo(filterEntry.infoLink)
                assertThat(result.thumbnail).isEqualTo(filterEntry.thumbnail)
            }
        }
    }
})