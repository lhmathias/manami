package io.github.manamiproject.manami.core

import io.github.manamiproject.manami.core.commands.CommandService
import io.github.manamiproject.manami.core.config.Config
import io.github.manamiproject.manami.core.tasks.TaskConductor
import io.github.manamiproject.manami.entities.InfoLink
import io.github.manamiproject.manami.entities.NormalizedAnimeBaseUrls
import io.github.manamiproject.manami.entities.WatchListEntry
import io.github.manamiproject.manami.persistence.Persistence
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.mockito.Mockito.*
import java.nio.file.Paths

object ManamiSpec : Spek({

    val persistence: Persistence = mock(Persistence::class.java)
    val cmdService: CommandService = mock(CommandService::class.java)
    val taskConductor: TaskConductor = mock(TaskConductor::class.java)
    val manami = Manami

    given("a manami instance") {

        beforeEachTest {
            Config.file = Paths.get("./myList.xml")
            reset(cmdService)
            reset(taskConductor)
        }

        on("exporting any file") {
            val file = Paths.get(".")
            manami.export(file)

            it("must simply delegate to the persistence layer") {
                verify(persistence, times(1)).exportToJsonFile(file)
            }
        }

        on("importing a json file") {
            val file = Paths.get("test.json")
            manami.importFile(file)

            it("must simply delegate to the persistence layer") {
                verify(persistence, times(1)).importJsonFile(file)
            }
        }

        on("importing a MAL file") {
            val file = Paths.get("test.xml")
            manami.importFile(file)

            it("must simply delegate to the persistence layer") {
                verify(persistence, times(1)).importMalFile(file)
            }
        }

        on("fetching filterlist") {
            manami.fetchFilterList()

            it("must simply delegate to the persistence layer") {
                verify(persistence, times(1)).fetchFilterList()
            }
        }

        on("checking if entry exists in filterlist") {
            val entry = InfoLink("${NormalizedAnimeBaseUrls.MAL.value}1535")
            manami.filterListEntryExists(entry)

            it("must simply delegate to the persistence layer") {
                verify(persistence, times(1)).filterListEntryExists(entry)
            }
        }

        on("fetching animelist") {
            manami.fetchAnimeList()

            it("must simply delegate to the persistence layer") {
                verify(persistence, times(1)).fetchAnimeList()
            }
        }

        on("checking if entry exists in animelist") {
            val entry = InfoLink("${NormalizedAnimeBaseUrls.MAL.value}1535")
            manami.animeEntryExists(entry)

            it("must simply delegate to the persistence layer") {
                verify(persistence, times(1)).animeEntryExists(entry)
            }
        }

        on("fetching watchlist") {
            manami.fetchWatchList()

            it("must simply delegate to the persistence layer") {
                verify(persistence, times(1)).fetchWatchList()
            }
        }

        on("checking if entry exists in watchlist") {
            val entry = InfoLink("${NormalizedAnimeBaseUrls.MAL.value}1535")
            manami.animeEntryExists(entry)

            it("must simply delegate to the persistence layer") {
                verify(persistence, times(1)).watchListEntryExists(entry)
            }
        }

        on("searching empty string") {
            manami.search("")

            it("must not do anything") {
                verify(taskConductor, never()).safelyStart(any())
            }
        }
    }
})