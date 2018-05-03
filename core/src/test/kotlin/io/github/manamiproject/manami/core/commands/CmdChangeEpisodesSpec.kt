package io.github.manamiproject.manami.core.commands

import io.github.manamiproject.manami.entities.AnimeType
import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.entities.InfoLink
import io.github.manamiproject.manami.entities.NORMALIZED_ANIME_DOMAIN
import io.github.manamiproject.manami.persistence.Persistence
import io.github.manamiproject.manami.persistence.PersistenceFacade
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class CmdChangeEpisodesSpec : Spek({

    val persistence: Persistence = PersistenceFacade

    beforeEachTest {
        persistence.clearAll()
    }

    given("a command with a valid anime") {
        val newValue = 37
        val anime = Anime(
                "Death Note",
                InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}1535"),
                10,
                AnimeType.TV,
                "/death_note"
        )

        persistence.addAnime(anime)

        val cmdChangeEpisodes = CmdChangeEpisodes(anime, newValue, persistence)

        on("executing command") {
            val result = cmdChangeEpisodes.execute()

            it("must return true") {
                assertThat(result).isTrue()
            }

            it("must change it's entry in persistence") {
                assertThat(persistence.fetchAnimeList()[0].episodes).isEqualTo(newValue)
            }
        }
    }

    given("a command with a valid anime which has been executed already") {
        val newValue = 10
        val anime = Anime(
                "Death Note",
                InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}1535"),
                37,
                AnimeType.TV,
                "/death_note"
        )

        persistence.addAnime(anime)

        val cmdChangeEpisodes = CmdChangeEpisodes(anime, newValue, persistence)
        cmdChangeEpisodes.execute()

        on("undo command") {
            cmdChangeEpisodes.undo()

            it("must restore the initial value in persistence") {
                assertThat(persistence.fetchAnimeList()[0].episodes).isEqualTo(anime.episodes)
            }
        }
    }
})