package io.github.manamiproject.manami.persistence.inmemory.watchlist

import io.github.manamiproject.manami.dto.entities.Anime
import io.github.manamiproject.manami.dto.entities.InfoLink
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith


@RunWith(JUnitPlatform::class)
class InMemoryWatchListSpec : Spek({

    val inMemoryWatchList = InMemoryWatchList()

    given("an invalid anime") {
        val anime = Anime("", InfoLink(""))

        on("adding anime to watchlist") {
            val result = inMemoryWatchList.watchAnime(anime)

            it("return false, because invalid anime won't be added") {
                assertThat(result).isFalse()
            }

            it("must not increase the list") {
                assertThat(inMemoryWatchList.fetchWatchList()).isEmpty()
            }
        }

        on("removing it from list") {
            val result = inMemoryWatchList.removeFromWatchList(anime)

            it("must always return false, because invalid entries can't be removed, because by definition they can't be added anyway") {
                assertThat(result).isFalse()
            }
        }
    }
})