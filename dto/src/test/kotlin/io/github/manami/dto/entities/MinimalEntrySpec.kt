package io.github.manami.dto.entities

import io.github.manami.dto.AnimeType
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.net.URL

@RunWith(JUnitPlatform::class)
class MinimalEntrySpec : Spek({

    given("a valid FilterListEntry") {
        val filterListEntry = FilterListEntry(
                "Death Note",
                InfoLink("http://myanimelist.net/anime/1535"),
                URL("https://myanimelist.cdn-dena.com/images/anime/9/9453t.jpg")
        )

        on("checking if the FilterListEntry is a valid MinimalEntry") {
            val result = filterListEntry.isValidMinimalEntry()


            it("must return true") {
                assertThat(result).isTrue()
            }
        }
    }


    given("a FilterListEntry without title") {
        val filterListEntry = FilterListEntry(
                "",
                InfoLink("http://myanimelist.net/anime/1535"),
                URL("https://myanimelist.cdn-dena.com/images/anime/9/9453t.jpg")
        )

        on("checking if the FilterListEntry is a valid MinimalEntry") {
            val result = filterListEntry.isValidMinimalEntry()


            it("must return false") {
                assertThat(result).isFalse()
            }
        }
    }


    given("a FilterListEntry with a blank title") {
        val filterListEntry = FilterListEntry(
                "   ",
                InfoLink("http://myanimelist.net/anime/1535"),
                URL("https://myanimelist.cdn-dena.com/images/anime/9/9453t.jpg")
        )

        on("checking if the FilterListEntry is a valid MinimalEntry") {
            val result = filterListEntry.isValidMinimalEntry()


            it("must return false") {
                assertThat(result).isFalse()
            }
        }
    }


    given("a FilterListEntry without a valid infoLink") {
        val filterListEntry = FilterListEntry(
                "Death Note",
                InfoLink(""),
                URL("https://myanimelist.cdn-dena.com/images/anime/9/9453t.jpg")
        )

        on("checking if the FilterListEntry is a valid MinimalEntry") {
            val result = filterListEntry.isValidMinimalEntry()


            it("must return false. A FilterListEntry must provide a valid InfoLink, because the infoLink is it's identifier.") {
                assertThat(result).isFalse()
            }
        }
    }


    given("a valid WatchListEntry") {
        val watchListEntry = WatchListEntry("Death Note",
                InfoLink("http://myanimelist.net/anime/1535"),
                URL("https://myanimelist.cdn-dena.com/images/anime/9/9453t.jpg")
        )

        on("checking if the WatchListEntry is a valid MinimalEntry") {
            val result = watchListEntry.isValidMinimalEntry()


            it("must return true") {
                assertThat(result).isTrue()
            }
        }
    }


    given("a WatchListEntry without a valid infoLink") {
        val watchListEntry = WatchListEntry(
                "Death Note",
                InfoLink(""),
                URL("https://myanimelist.cdn-dena.com/images/anime/9/9453t.jpg")
        )

        on("checking if the WatchListEntry is a valid MinimalEntry") {
            val result = watchListEntry.isValidMinimalEntry()


            it("must return false. A WatchListEntry must provide a valid InfoLink, because the infoLink is it's identifier.") {
                assertThat(result).isFalse()
            }
        }
    }


    given("a WatchListEntry without title") {
        val watchListEntry = WatchListEntry(
                "",
                InfoLink("http://myanimelist.net/anime/1535"),
                URL("https://myanimelist.cdn-dena.com/images/anime/9/9453t.jpg")
        )

        on("checking if the WatchListEntry is a valid MinimalEntry") {
            val result = watchListEntry.isValidMinimalEntry()


            it("must return false") {
                assertThat(result).isFalse()
            }
        }
    }


    given("a WatchListEntry with a blank title") {
        val watchListEntry = WatchListEntry(
                "   ",
                InfoLink("http://myanimelist.net/anime/1535"),
                URL("https://myanimelist.cdn-dena.com/images/anime/9/9453t.jpg")
        )

        on("checking if the WatchListEntry is a valid MinimalEntry") {
            val result = watchListEntry.isValidMinimalEntry()


            it("must return false") {
                assertThat(result).isFalse()
            }
        }
    }


    given("a valid Anime") {
        val anime = Anime(
                "Death Note",
                InfoLink("http://myanimelist.net/anime/1535"),
                37,
                AnimeType.TV,
                "/death_note",
                URL("https://myanimelist.cdn-dena.com/images/anime/9/9453t.jpg"),
                URL("https://myanimelist.cdn-dena.com/images/anime/9/9453.jpg")
        )

        on("checking if the Anime is a valid MinimalEntry") {
            val result = anime.isValidMinimalEntry()


            it("must return true") {
                assertThat(result).isTrue()
            }
        }
    }


    given("an Anime without title") {
        val anime = Anime(
                "",
                InfoLink("http://myanimelist.net/anime/1535")
        )

        on("checking if the Anime is a valid MinimalEntry") {
            val result = anime.isValidMinimalEntry()


            it("must return false") {
                assertThat(result).isFalse()
            }
        }
    }


    given("an Anime with a blank title") {
        val anime = Anime(
                "   ",
                InfoLink("http://myanimelist.net/anime/1535")
        )

        on("checking if the Anime is a valid MinimalEntry") {
            val result = anime.isValidMinimalEntry()


            it("must return false") {
                assertThat(result).isFalse()
            }
        }
    }

    given("an Anime without a valid infoLink") {
        val anime = Anime(
                "Death Note",
                InfoLink("")
        )

        on("checking if the Anime is a valid MinimalEntry") {
            val result = anime.isValidMinimalEntry()


            it("must return true, because Anime have an actual ID as an identifier. They may exist on your HDD, but might not have a corresponding entry on sites like MAL due to their database rules.") {
                assertThat(result).isTrue()
            }
        }
    }
})