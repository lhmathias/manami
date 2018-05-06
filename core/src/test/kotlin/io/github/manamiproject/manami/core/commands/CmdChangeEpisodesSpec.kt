package io.github.manamiproject.manami.core.commands

import io.github.manamiproject.manami.core.commands.PersistenceMockCreatorForCommandSpecs.createAnimeListPersistenceMock
import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.entities.InfoLink
import io.github.manamiproject.manami.entities.NORMALIZED_ANIME_DOMAIN
import io.github.manamiproject.manami.persistence.PersistenceFacade
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on


object CmdChangeEpisodesSpec : Spek({

    given("a command with a valid anime") {
        val newValue = 37
        val anime = Anime(
                "Death Note",
                InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}1535"),
                numberOfEpisodes = 10
        )

        val persistenceMock = createAnimeListPersistenceMock()

        persistenceMock.addAnime(anime)

        val cmdChangeEpisodes = CmdChangeEpisodes(anime, newValue, persistenceMock)

        on("executing command") {
            val result = cmdChangeEpisodes.execute()

            it("must return true") {
                assertThat(result).isTrue()
            }

            it("must change it's entry in persistence") {
                assertThat(persistenceMock.fetchAnimeList()[0].episodes).isEqualTo(newValue)
            }
        }
    }

    given("a command with a valid anime which has been executed already") {
        val newValue = 10
        val anime = Anime(
                "Death Note",
                InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}1535"),
                37
        )

        val persistenceMock = createAnimeListPersistenceMock()

        persistenceMock.addAnime(anime)

        val cmdChangeEpisodes = CmdChangeEpisodes(anime, newValue, persistenceMock)
        cmdChangeEpisodes.execute()

        on("undo command") {
            cmdChangeEpisodes.undo()

            it("must restore the initial value in persistence") {
                assertThat(persistenceMock.fetchAnimeList()[0].episodes).isEqualTo(anime.episodes)
            }
        }
    }
})