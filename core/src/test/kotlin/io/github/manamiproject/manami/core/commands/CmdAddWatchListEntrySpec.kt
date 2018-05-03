package io.github.manamiproject.manami.core.commands

import io.github.manamiproject.manami.dto.entities.InfoLink
import io.github.manamiproject.manami.dto.entities.NORMALIZED_ANIME_DOMAIN
import io.github.manamiproject.manami.dto.entities.WatchListEntry
import io.github.manamiproject.manami.persistence.Persistence
import io.github.manamiproject.manami.persistence.PersistenceFacade
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.net.URL


@RunWith(JUnitPlatform::class)
class CmdAddWatchListEntrySpec : Spek({

    val persistence: Persistence = PersistenceFacade

    beforeEachTest {
        persistence.clearAll()
    }

    given("a command with a valid watch list entry") {
        val entry = WatchListEntry(
                "Death Note",
                InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}1535"),
                URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg")
        )

        val cmdAddWatchListEntry = CmdAddWatchListEntry(entry, persistence)

        on("executing command") {
            val result = cmdAddWatchListEntry.execute()

            it("must return true") {
                assertThat(result).isTrue()
            }

            it("must exist in persistence") {
                assertThat(persistence.watchListEntryExists(entry.infoLink)).isTrue()
            }
        }
    }

    given("a command which has been executed already") {
        val entry = WatchListEntry(
                "Death Note",
                InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}1535"),
                URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg")
        )

        val cmdAddWatchListEntry = CmdAddWatchListEntry(entry, persistence)
        cmdAddWatchListEntry.execute()

        on("undo command") {
            cmdAddWatchListEntry.undo()

            it("must result in entry being removed") {
                assertThat(persistence.watchListEntryExists(entry.infoLink)).isFalse()
            }
        }
    }

    given("a command with an invalid entry") {
        val entry = WatchListEntry("    ", InfoLink("some-url"))

        val cmdAddWatchListEntry = CmdAddWatchListEntry(entry, persistence)

        on("executing command") {
            val result = cmdAddWatchListEntry.execute()

            it("must return false") {
                assertThat(result).isFalse()
            }

            it("must exist in persistence") {
                assertThat(persistence.watchListEntryExists(entry.infoLink)).isFalse()
            }
        }
    }
})