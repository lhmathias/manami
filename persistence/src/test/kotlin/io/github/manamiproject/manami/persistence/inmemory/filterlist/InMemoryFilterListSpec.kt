package io.github.manamiproject.manami.persistence.inmemory.filterlist

import io.github.manamiproject.manami.dto.entities.Anime
import io.github.manamiproject.manami.dto.entities.InfoLink
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

class InMemoryFilterListSpec : Spek({

    val inMemoryFilterList = InMemoryFilterList()

    given("an invalid anime") {
        val anime = Anime("", InfoLink(""))

        on("filtering anime") {
            val result = inMemoryFilterList.filterAnime(anime)

            it("return false, because invalid anime won't be added") {
                assertThat(result).isFalse()
            }

            it("must not increase the list") {
                assertThat(inMemoryFilterList.fetchFilterList()).isEmpty()
            }
        }

        on("removing it from list") {
            val result = inMemoryFilterList.removeFromFilterList(anime)

            it("must always return false, because invalid entries can't be removed, because by definition they can't be added anyway") {
                assertThat(result).isFalse()
            }
        }
    }
})