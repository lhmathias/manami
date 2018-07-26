package io.github.manamiproject.manami.core.commands

import io.github.manamiproject.manami.entities.*
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.net.URL

object CmdChangeThumbnailSpec : Spek({

    given("an anime without a thumbnail") {
        val thumbnail = URL("https://myanimelist.cdn-dena.com/images/anime/9/9453t.jpg")
        val anime = Anime(
                "Death Note",
        InfoLink("${NormalizedAnimeBaseUrls.MAL.value}1535"),
        location = "/deathnote"
        )

        val persistenceMock = PersistenceMockCreatorForCommandSpecs.createAnimeListPersistenceMock()
        persistenceMock.addAnime(anime)

        val command = CmdChangeThumbnail(anime, thumbnail, persistenceMock)

        on("executing change thumbnail spec") {
            val result = command.execute()

            it("must return true") {
                assertThat(result).isTrue()
            }

            it("must change it's entry in persistence") {
                assertThat(persistenceMock.fetchAnimeList()[0].thumbnail).isEqualTo(thumbnail)
            }
        }
    }

    given("a FilterListEntry without a thumbnail") {
        val thumbnail = URL("https://myanimelist.cdn-dena.com/images/anime/9/9453t.jpg")
        val filterListEntry = FilterListEntry(
            "Death Note",
            InfoLink("${NormalizedAnimeBaseUrls.MAL.value}1535")
        )

        val persistenceMock = PersistenceMockCreatorForCommandSpecs.createFilterListPersistenceMock()
        persistenceMock.filterAnime(filterListEntry)

        val command = CmdChangeThumbnail(filterListEntry, thumbnail, persistenceMock)

        on("executing change thumbnail spec") {
            val result = command.execute()

            it("must return true") {
                assertThat(result).isTrue()
            }

            it("must change it's entry in persistence") {
                assertThat(persistenceMock.fetchFilterList()[0].thumbnail).isEqualTo(thumbnail)
            }
        }
    }

    given("a WatchListEntry without a thumbnail") {
        val thumbnail = URL("https://myanimelist.cdn-dena.com/images/anime/9/9453t.jpg")
        val watchListEntry = WatchListEntry(
            "Death Note",
            InfoLink("${NormalizedAnimeBaseUrls.MAL.value}1535")
        )

        val persistenceMock = PersistenceMockCreatorForCommandSpecs.createWatchListPersistenceMock()
        persistenceMock.watchAnime(watchListEntry)

        val command = CmdChangeThumbnail(watchListEntry, thumbnail, persistenceMock)

        on("executing change thumbnail spec") {
            val result = command.execute()

            it("must return true") {
                assertThat(result).isTrue()
            }

            it("must change it's entry in persistence") {
                assertThat(persistenceMock.fetchWatchList()[0].thumbnail).isEqualTo(thumbnail)
            }
        }
    }
})