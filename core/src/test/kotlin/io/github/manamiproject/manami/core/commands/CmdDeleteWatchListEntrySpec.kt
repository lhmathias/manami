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


object CmdDeleteWatchListEntrySpec : Spek({

    given("a command with a valid watch list entry") {
        val entry = WatchListEntry(
                "Death Note",
                InfoLink("${NormalizedAnimeBaseUrls.MAL.value}1535"),
                URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg")
        )

        val persistenceMock: Persistence = createSimpleWatchListPersistenceMock(entry)

        val cmdDeleteWatchListEntry = CmdDeleteWatchListEntry(entry, persistenceMock)
        persistenceMock.watchAnime(entry)

        on("executing command") {
            val result = cmdDeleteWatchListEntry.execute()

            it("must return true") {
                assertThat(result).isTrue()
            }

            it("must not exist in persistence") {
                assertThat(persistenceMock.watchListEntryExists(entry.infoLink)).isFalse()
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

        persistenceMock.watchAnime(entry)

        val cmdDeleteWatchListEntry = CmdDeleteWatchListEntry(entry, persistenceMock)
        cmdDeleteWatchListEntry.execute()

        on("undo command") {
            cmdDeleteWatchListEntry.undo()

            it("must result in entry being restored") {
                assertThat(persistenceMock.watchListEntryExists(entry.infoLink)).isTrue()
            }
        }
    }

    given("a command with an invalid entry") {
        val persistenceMock = mock<Persistence> { }
        val entry = WatchListEntry("    ", InfoLink("some-url"))
        val cmdDeleteWatchListEntry = CmdDeleteWatchListEntry(entry, persistenceMock)

        on("executing command") {
            val result = cmdDeleteWatchListEntry.execute()

            it("must return false") {
                assertThat(result).isFalse()
            }
        }
    }
})