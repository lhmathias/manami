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


object CmdChangeTitleSpec : Spek({

    given("a command with a valid anime") {
        val newValue = "Death Note"
        val anime = Anime(
                "~Death-Note~",
                InfoLink("${NormalizedAnimeBaseUrls.MAL.value}1535")
        )

        val persistenceMock = createAnimeListPersistenceMock()

        persistenceMock.addAnime(anime)

        val cmdChangeTitle = CmdChangeTitle(anime, newValue, persistenceMock)

        on("executing command") {
            val result = cmdChangeTitle.execute()

            it("must return true") {
                assertThat(result).isTrue()
            }

            it("must change it's entry in persistence") {
                assertThat(persistenceMock.fetchAnimeList()[0].title).isEqualTo(newValue)
            }
        }
    }

    given("a command with a valid anime which has been executed already") {
        val newValue = "~Death-Note~"
        val anime = Anime(
                "Death Note",
                InfoLink("${NormalizedAnimeBaseUrls.MAL.value}1535")
        )

        val persistenceMock = createAnimeListPersistenceMock()

        persistenceMock.addAnime(anime)

        val cmdChangeTitle = CmdChangeTitle(anime, newValue, persistenceMock)
        cmdChangeTitle.execute()

        on("undo command") {
            cmdChangeTitle.undo()

            it("must restore the initial value in persistence") {
                assertThat(persistenceMock.fetchAnimeList()[0].title).isEqualTo(anime.title)
            }
        }
    }
})