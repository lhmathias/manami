package io.github.manamiproject.manami.core.commands

import com.nhaarman.mockito_kotlin.mock
import io.github.manamiproject.manami.core.commands.PersistenceMockCreatorForCommandSpecs.createSimpleWatchListPersistenceMock
import io.github.manamiproject.manami.entities.InfoLink
import io.github.manamiproject.manami.entities.NormalizedAnimeBaseUrls
import io.github.manamiproject.manami.entities.WatchListEntry
import io.github.manamiproject.manami.persistence.Persistence
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.net.URL


object CmdAddWatchListEntrySpec : Spek({

    given("a command with a valid watch list entry") {
        val entry = WatchListEntry(
                "Death Note",
                InfoLink("${NormalizedAnimeBaseUrls.MAL.value}1535"),
                URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg")
        )

        val persistenceMock: Persistence = createSimpleWatchListPersistenceMock(entry)
        val cmdAddWatchListEntry = CmdAddWatchListEntry(entry, persistenceMock)

        on("executing command") {
            val result = cmdAddWatchListEntry.execute()

            it("must return true") {
                assertThat(result).isTrue()
            }

            it("must exist in persistence") {
                assertThat(persistenceMock.watchListEntryExists(entry.infoLink)).isTrue()
            }
        }
    }

    given("a command which has been executed already") {
        val entry = WatchListEntry(
                "Death Note",
                InfoLink("${NormalizedAnimeBaseUrls.MAL.value}1535"),
                URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg")
        )

        val persistenceMock: Persistence = createSimpleWatchListPersistenceMock(entry)

        val cmdAddWatchListEntry = CmdAddWatchListEntry(entry, persistenceMock)
        cmdAddWatchListEntry.execute()

        on("undo command") {
            cmdAddWatchListEntry.undo()

            it("must result in entry being removed") {
                assertThat(persistenceMock.watchListEntryExists(entry.infoLink)).isFalse()
            }
        }
    }

    given("a command with an invalid entry") {
        val persistenceMock = mock<Persistence> { }
        val entry = WatchListEntry("    ", InfoLink("some-url"))
        val cmdAddWatchListEntry = CmdAddWatchListEntry(entry, persistenceMock)

        on("executing command") {
            val result = cmdAddWatchListEntry.execute()

            it("must return false") {
                assertThat(result).isFalse()
            }

            it("must exist in persistence") {
                assertThat(persistenceMock.watchListEntryExists(entry.infoLink)).isFalse()
            }
        }
    }
})