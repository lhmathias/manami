package io.github.manamiproject.manami.persistence.inmemory.animelist

import io.github.manamiproject.manami.dto.entities.Anime
import io.github.manamiproject.manami.dto.entities.InfoLink
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

class InMemoryAnimeListSpec : Spek({

    val inMemoryAnimeList = InMemoryAnimeList()

    given("an invalid anime") {
        val anime = Anime("", InfoLink(""))

        on("adding anime to list") {
            val result = inMemoryAnimeList.addAnime(anime)

            it("return false, because invalid anime won't be added") {
                assertThat(result).isFalse()
            }

            it("must not increase the list") {
                assertThat(inMemoryAnimeList.fetchAnimeList()).isEmpty()
            }
        }
    }
})