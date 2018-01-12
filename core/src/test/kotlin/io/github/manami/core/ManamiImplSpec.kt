package io.github.manami.core

import com.google.common.eventbus.EventBus
import io.github.manami.cache.Cache
import io.github.manami.core.commands.CommandService
import io.github.manami.core.config.Config
import io.github.manami.core.tasks.TaskConductor
import io.github.manami.dto.entities.FilterListEntry
import io.github.manami.dto.entities.InfoLink
import io.github.manami.dto.entities.WatchListEntry
import io.github.manami.persistence.PersistenceHandler
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import java.nio.file.Paths

class ManamiImplSpec : Spek({

    val cmdService: CommandService = mock(CommandService::class.java)
    val config: Config = mock(Config::class.java)
    val persistence: PersistenceHandler = mock(PersistenceHandler::class.java)
    val taskConductor: TaskConductor = mock(TaskConductor::class.java)
    val cache: Cache = mock(Cache::class.java)
    val eventBus: EventBus = mock(EventBus::class.java)

    given("a manami instance") {
        val manami = ManamiImpl(
                cmdService,
                config,
                persistence,
                taskConductor,
                cache,
                eventBus
        )

        beforeEachTest {
            reset(cmdService)
            reset(config)
            reset(persistence)
            reset(taskConductor)
            reset(cache)
            reset(eventBus)
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
            val entry = InfoLink("https://myanimelist.net/anime/1535")
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
            val entry = InfoLink("https://myanimelist.net/anime/1535")
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
            val entry = InfoLink("https://myanimelist.net/anime/1535")
            manami.animeEntryExists(entry)

            it("must simply delegate to the persistence layer") {
                verify(persistence, times(1)).watchListEntryExists(entry)
            }
        }

        on("calling updateOrCreate") {
            val entry = WatchListEntry(
                "Death Note",
                    InfoLink("https://myanimelist.net/anime/1535")
            )
            manami.updateOrCreate(entry)

            it("must simply delegate to the persistence layer") {
                verify(persistence, times(1)).updateOrCreate(entry)
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