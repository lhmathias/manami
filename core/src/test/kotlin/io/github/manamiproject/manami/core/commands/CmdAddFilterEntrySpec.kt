package io.github.manamiproject.manami.core.commands

import io.github.manamiproject.manami.dto.entities.FilterListEntry
import io.github.manamiproject.manami.dto.entities.InfoLink
import io.github.manamiproject.manami.dto.entities.NORMALIZED_ANIME_DOMAIN
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
class CmdAddFilterEntrySpec : Spek({

    val persistence: Persistence = PersistenceFacade

    beforeEachTest {
        persistence.clearAll()
    }

    given("a command with a valid filter list entry") {
        val entry = FilterListEntry(
                "Death Note",
                InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}1535"),
                URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg")
        )

        val cmdAddFilterEntry = CmdAddFilterEntry(entry, persistence)

        on("executing command") {
            val result = cmdAddFilterEntry.execute()

            it("must return true") {
                assertThat(result).isTrue()
            }

            it("must exist in persistence") {
                assertThat(persistence.filterListEntryExists(entry.infoLink)).isTrue()
            }
        }
    }

    given("a command which has been executed already") {
        val entry = FilterListEntry(
                "Death Note",
                InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}1535"),
                URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg")
        )

        val cmdAddFilterEntry = CmdAddFilterEntry(entry, persistence)
        cmdAddFilterEntry.execute()

        on("undo command") {
            cmdAddFilterEntry.undo()

            it("must result in entry being removed") {
                assertThat(persistence.filterListEntryExists(entry.infoLink)).isFalse()
            }
        }
    }

    given("a command with an invalid entry") {
        val entry = FilterListEntry("    ", InfoLink("some-url"))

        val cmdAddFilterEntry = CmdAddFilterEntry(entry, persistence)

        on("executing command") {
            val result = cmdAddFilterEntry.execute()

            it("must return false") {
                assertThat(result).isFalse()
            }

            it("must exist in persistence") {
                assertThat(persistence.filterListEntryExists(entry.infoLink)).isFalse()
            }
        }
    }
})