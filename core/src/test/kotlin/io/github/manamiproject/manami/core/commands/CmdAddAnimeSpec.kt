package io.github.manamiproject.manami.core.commands

import com.nhaarman.mockito_kotlin.*
import io.github.manamiproject.manami.core.commands.PersistenceMockCreatorForCommandSpecs.createSimpleAnimeListPersistenceMock
import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.entities.InfoLink
import io.github.manamiproject.manami.entities.NORMALIZED_ANIME_DOMAIN
import io.github.manamiproject.manami.persistence.Persistence
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on


object CmdAddAnimeSpec : Spek({

    given("a command with a valid anime") {
        val anime = Anime(
                "Death Note",
                InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}1535")
        )

        val persistenceMock : Persistence = createSimpleAnimeListPersistenceMock(anime)
        val cmdAddAnime = CmdAddAnime(anime, persistenceMock)

        on("executing command") {
            val result = cmdAddAnime.execute()

            it("must return true") {
                assertThat(result).isTrue()
            }

            it("must exist in persistence") {
                assertThat(persistenceMock.animeEntryExists(anime.infoLink)).isTrue()
            }
        }
    }

    given("a command which has been executed already") {
        val anime = Anime(
                "Death Note",
                InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}1535")
        )

        val persistenceMock : Persistence = createSimpleAnimeListPersistenceMock(anime)

        val cmdAddAnime = CmdAddAnime(anime, persistenceMock)
        cmdAddAnime.execute()

        on("undo command") {
            cmdAddAnime.undo()

            it("must result in anime being removed") {
                assertThat(persistenceMock.animeEntryExists(anime.infoLink)).isFalse()
            }
        }
    }

    given("a command with an invalid anime") {
        val anime = Anime("    ", InfoLink("some-url"))

        val persistenceMock = mock<Persistence> { }
        val cmdAddAnime = CmdAddAnime(anime, persistenceMock)

        on("executing command") {
            val result = cmdAddAnime.execute()

            it("must return false") {
                assertThat(result).isFalse()
            }
        }
    }
})