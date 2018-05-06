package io.github.manamiproject.manami.core.commands

import com.nhaarman.mockito_kotlin.mock
import io.github.manamiproject.manami.core.commands.PersistenceMockCreatorForCommandSpecs.createFilterListPersistenceMock
import io.github.manamiproject.manami.entities.FilterListEntry
import io.github.manamiproject.manami.entities.InfoLink
import io.github.manamiproject.manami.entities.NORMALIZED_ANIME_DOMAIN
import io.github.manamiproject.manami.persistence.Persistence
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.net.URL


object CmdAddFilterEntrySpec : Spek({

    given("a command with a valid filter list entry") {
        val entry = FilterListEntry(
                "Death Note",
                InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}1535"),
                URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg")
        )

        val persistenceMock: Persistence = createFilterListPersistenceMock(entry)
        val cmdAddFilterEntry = CmdAddFilterEntry(entry, persistenceMock)

        on("executing command") {
            val result = cmdAddFilterEntry.execute()

            it("must return true") {
                assertThat(result).isTrue()
            }

            it("must exist in persistence") {
                assertThat(persistenceMock.filterListEntryExists(entry.infoLink)).isTrue()
            }
        }
    }

    given("a command which has been executed already") {
        val entry = FilterListEntry(
                "Death Note",
                InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}1535"),
                URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg")
        )

        val persistenceMock: Persistence = createFilterListPersistenceMock(entry)

        val cmdAddFilterEntry = CmdAddFilterEntry(entry, persistenceMock)
        cmdAddFilterEntry.execute()

        on("undo command") {
            cmdAddFilterEntry.undo()

            it("must result in entry being removed") {
                assertThat(persistenceMock.filterListEntryExists(entry.infoLink)).isFalse()
            }
        }
    }

    given("a command with an invalid entry") {
        val persistenceMock = mock<Persistence> { }
        val entry = FilterListEntry("    ", InfoLink("some-url"))
        val cmdAddFilterEntry = CmdAddFilterEntry(entry, persistenceMock)

        on("executing command") {
            val result = cmdAddFilterEntry.execute()

            it("must return false") {
                assertThat(result).isFalse()
            }

            it("must exist in persistence") {
                assertThat(persistenceMock.filterListEntryExists(entry.infoLink)).isFalse()
            }
        }
    }
})