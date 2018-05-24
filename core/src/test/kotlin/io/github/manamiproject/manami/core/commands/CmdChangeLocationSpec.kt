package io.github.manamiproject.manami.core.commands

import io.github.manamiproject.manami.core.commands.PersistenceMockCreatorForCommandSpecs.createAnimeListPersistenceMock
import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.entities.InfoLink
import io.github.manamiproject.manami.entities.NormalizedAnimeBaseUrls
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on


object CmdChangeLocationSpec : Spek({

    given("a command with a valid anime") {
        val newValue = "/death_note"
        val anime = Anime(
                "Death Note",
                InfoLink("${NormalizedAnimeBaseUrls.MAL.value}1535"),
                location = "/deathnote"
        )

        val persistenceMock = createAnimeListPersistenceMock()

        persistenceMock.addAnime(anime)

        val cmdChangeLocation = CmdChangeLocation(anime, newValue, persistenceMock)

        on("executing command") {
            val result = cmdChangeLocation.execute()

            it("must return true") {
                assertThat(result).isTrue()
            }

            it("must change it's entry in persistence") {
                assertThat(persistenceMock.fetchAnimeList()[0].location).isEqualTo(newValue)
            }
        }
    }

    given("a command with a valid anime which has been executed already") {
        val newValue = "/deathnote"
        val anime = Anime(
                "Death Note",
                InfoLink("${NormalizedAnimeBaseUrls.MAL.value}1535"),
                location = "/death_note"
        )

        val persistenceMock = createAnimeListPersistenceMock()

        persistenceMock.addAnime(anime)

        val cmdChangeLocation = CmdChangeLocation(anime, newValue, persistenceMock)
        cmdChangeLocation.execute()

        on("undo command") {
            cmdChangeLocation.undo()

            it("must restore the initial value in persistence") {
                assertThat(persistenceMock.fetchAnimeList()[0].location).isEqualTo(anime.location)
            }
        }
    }
})