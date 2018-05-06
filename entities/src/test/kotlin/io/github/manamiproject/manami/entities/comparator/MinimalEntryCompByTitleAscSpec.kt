package io.github.manamiproject.manami.entities.comparator

import io.github.manamiproject.manami.entities.entities.InfoLink
import io.github.manamiproject.manami.entities.entities.NORMALIZED_ANIME_DOMAIN
import io.github.manamiproject.manami.entities.entities.WatchListEntry
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.net.URL

class MinimalEntryCompByTitleAscSpec : Spek({

    val comparator = MinimalEntryCompByTitleAsc

    given("two different and valid watch list entries") {
        val gintama = WatchListEntry("Gintama",
                InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}28977"),
                URL("https://myanimelist.cdn-dena.com/images/anime/3/72078t.jpg")
        )

        val steinsGate = WatchListEntry(
                "Steins;Gate",
                InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}9253"),
                URL("https://myanimelist.cdn-dena.com/images/anime/5/73199t.jpg")
        )


        on("comparing both with steins;gate as first parameter") {
            val result: Int = comparator.compare(steinsGate, gintama)

            it("must return a value > 0 to indicate that [S]teins;gate comes after [G]intama") {
                assertThat(result).isGreaterThan(0)
            }
        }


        on("comparing both with gintama as first parameter") {
            val result: Int = comparator.compare(gintama, steinsGate)

            it("must return a value < 0 to indicate that [G]intama comes before [S]teins;gate") {
                assertThat(result).isLessThan(0)
            }
        }


        on("comparing one entry with itself") {
            val result: Int = comparator.compare(steinsGate, steinsGate)

            it("must return 0 to indicate that the titles are equal") {
                assertThat(result).isZero()
            }
        }
    }


    given("a valid watch list entry and a watch list entry with blank title") {
        val steinsGate = WatchListEntry(
                "Steins;Gate",
                InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}9253"),
                URL("https://myanimelist.cdn-dena.com/images/anime/5/73199t.jpg")
        )

        val emptyTitle = WatchListEntry("",
                InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}33352"),
                URL("https://myanimelist.cdn-dena.com/images/anime/11/89398t.jpg")
        )


        on("comparing two entries. First entry's title is empty.") {
            val result: Int = comparator.compare(emptyTitle, steinsGate)

            it("must return 0 to indicate that the titles are not comparable.") {
                assertThat(result).isZero()
            }
        }


        on("comparing two entries. Second entry's title is empty.") {
            val result: Int = comparator.compare(steinsGate, emptyTitle)

            it("must return 0 to indicate that the titles are not comparable.") {
                assertThat(result).isZero()
            }
        }
    }
})