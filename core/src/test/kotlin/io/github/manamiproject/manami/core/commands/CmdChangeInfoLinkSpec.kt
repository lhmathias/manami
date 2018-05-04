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
class CmdChangeInfoLinkSpec : Spek({

    val persistence: Persistence = PersistenceFacade

    given("a command with a valid anime") {
        val newValue = InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}1535")
        val anime = Anime(
                "Death Note",
                InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}15355555"),
                37,
                AnimeType.TV,
                "/death_note"
        )

        persistence.addAnime(anime)

        val cmdChangeInfoLink = CmdChangeInfoLink(anime, newValue, persistence)

        on("executing command") {
            val result = cmdChangeInfoLink.execute()

            it("must return true") {
                assertThat(result).isTrue()
            }

            it("must change it's entry in persistence") {
                assertThat(persistence.fetchAnimeList()[0].infoLink).isEqualTo(newValue)
            }
        }
    }

    given("a command with a valid anime which has been executed already") {
        val newValue = InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}15355555")
        val anime = Anime(
                "Death Note",
                InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}1535"),
                37,
                AnimeType.TV,
                "/death_note"
        )

        persistence.addAnime(anime)

        val cmdChangeInfoLink = CmdChangeInfoLink(anime, newValue, persistence)
        cmdChangeInfoLink.execute()

        on("undo command") {
            cmdChangeInfoLink.undo()

            it("must restore the initial value in persistence") {
                assertThat(persistence.fetchAnimeList()[0].infoLink).isEqualTo(anime.infoLink)
            }
        }
    }
})