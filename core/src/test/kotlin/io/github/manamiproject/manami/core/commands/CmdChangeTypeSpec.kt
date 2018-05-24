package io.github.manamiproject.manami.core.commands

import io.github.manamiproject.manami.core.commands.PersistenceMockCreatorForCommandSpecs.createAnimeListPersistenceMock
import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.entities.AnimeType
import io.github.manamiproject.manami.entities.InfoLink
import io.github.manamiproject.manami.entities.NormalizedAnimeBaseUrls
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on


object CmdChangeTypeSpec : Spek({

    given("a command with a valid anime") {
        val newValue = AnimeType.TV
        val anime = Anime(
                "Death Note",
                InfoLink("${NormalizedAnimeBaseUrls.MAL.value}1535"),
                type = AnimeType.OVA
        )

        val persistenceMock = createAnimeListPersistenceMock()

        persistenceMock.addAnime(anime)

        val cmdChangeType = CmdChangeType(anime, newValue, persistenceMock)

        on("executing command") {
            val result = cmdChangeType.execute()

            it("must return true") {
                assertThat(result).isTrue()
            }

            it("must change it's entry in persistence") {
                assertThat(persistenceMock.fetchAnimeList()[0].type).isEqualTo(newValue)
            }
        }
    }

    given("a command with a valid anime which has been executed already") {
        val newValue = AnimeType.OVA
        val anime = Anime(
                "Death Note",
                InfoLink("${NormalizedAnimeBaseUrls.MAL.value}1535"),
                type = AnimeType.TV
        )

        val persistenceMock = createAnimeListPersistenceMock()

        persistenceMock.addAnime(anime)

        val cmdChangeType = CmdChangeType(anime, newValue, persistenceMock)
        cmdChangeType.execute()

        on("undo command") {
            cmdChangeType.undo()

            it("must restore the initial value in persistence") {
                assertThat(persistenceMock.fetchAnimeList()[0].type).isEqualTo(anime.type)
            }
        }
    }
})