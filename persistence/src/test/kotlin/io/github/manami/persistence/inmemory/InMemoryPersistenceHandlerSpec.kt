package io.github.manami.persistence.inmemory

import io.github.manami.dto.entities.Anime
import io.github.manami.dto.entities.FilterListEntry
import io.github.manami.dto.entities.InfoLink
import io.github.manami.dto.entities.WatchListEntry
import io.github.manami.persistence.inmemory.animelist.InMemoryAnimeListHandler
import io.github.manami.persistence.inmemory.filterlist.InMemoryFilterListHandler
import io.github.manami.persistence.inmemory.watchlist.InMemoryWatchListHandler
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.mockito.Mockito.*


@RunWith(JUnitPlatform::class)
class InMemoryPersistenceHandlerSpec : Spek({

    val animeListHandler = mock(InMemoryAnimeListHandler::class.java)
    val filterListHandler = mock(InMemoryFilterListHandler::class.java)
    val watchListHandler =  mock(InMemoryWatchListHandler::class.java)


    beforeEachTest {
        reset(animeListHandler)
        reset(filterListHandler)
        reset(watchListHandler)
    }


    given("an instance of an InMemoryPersistenceHandler and an anime entry") {
        val animeEntry = Anime(
            "Death Note",
            InfoLink("http://myanimelist.net/anime/1535")
        )

        val inMemoryPersistenceHandler = InMemoryPersistenceHandler(
            animeListHandler,
            filterListHandler,
            watchListHandler
        )

        on("filtering an anime") {
            inMemoryPersistenceHandler.filterAnime(animeEntry)

            it("must delegate to the InMemoryFilterListHandler") {
                verify(filterListHandler, times(1)).filterAnime(animeEntry)
            }
        }

        on("fetching the filterlist") {
            inMemoryPersistenceHandler.fetchFilterList()

            it("must delegate to the InMemoryFilterListHandler") {
                verify(filterListHandler, times(1)).fetchFilterList()
            }
        }

        on("checking if an anime entry exists in the filterlist") {
            inMemoryPersistenceHandler.filterListEntryExists(animeEntry.infoLink)

            it("must delegate to the InMemoryFilterListHandler") {
                verify(filterListHandler, times(1)).filterListEntryExists(animeEntry.infoLink)
            }
        }

        on("removing entry from filterlist") {
            inMemoryPersistenceHandler.removeFromFilterList(animeEntry)

            it("must delegate to the InMemoryFilterListHandler") {
                verify(filterListHandler, times(1)).removeFromFilterList(animeEntry)
            }
        }

        on("adding an anime") {
            inMemoryPersistenceHandler.addAnime(animeEntry)

            it("must delegate to the InMemoryAnimeListHandler") {
                verify(animeListHandler, times(1)).addAnime(animeEntry)
            }
        }

        on("fetching the animelist") {
            inMemoryPersistenceHandler.fetchAnimeList()

            it("must delegate to the InMemoryAnimeListHandler") {
                verify(animeListHandler, times(1)).fetchAnimeList()
            }
        }

        on("checking if an anime entry exists in the animelist") {
            inMemoryPersistenceHandler.fetchAnimeList()

            it("must delegate to the InMemoryAnimeListHandler") {
                verify(animeListHandler, times(1)).fetchAnimeList()
            }
        }

        on("removing an anime") {
            inMemoryPersistenceHandler.removeAnime(animeEntry)

            it("must delegate to the InMemoryAnimeListHandler") {
                verify(animeListHandler, times(1)).removeAnime(animeEntry)
            }
        }

        on("adding an entry to the watchlist") {
            inMemoryPersistenceHandler.watchAnime(animeEntry)

            it("must delegate to the InMemoryWatchListHandler") {
                verify(watchListHandler, times(1)).watchAnime(animeEntry)
            }
        }

        on("fetching the watchlist") {
            inMemoryPersistenceHandler.fetchWatchList()

            it("must delegate to the InMemoryWatchListHandler") {
                verify(watchListHandler, times(1)).fetchWatchList()
            }
        }

        on("checking if an anime entry exists in the watchlist") {
            inMemoryPersistenceHandler.watchListEntryExists(animeEntry.infoLink)

            it("must delegate to the InMemoryWatchListHandler") {
                verify(watchListHandler, times(1)).watchListEntryExists(animeEntry.infoLink)
            }
        }

        on("removing an anime entry from the watchlist") {
            inMemoryPersistenceHandler.removeFromWatchList(animeEntry)

            it("must delegate to the InMemoryWatchListHandler") {
                verify(watchListHandler, times(1)).removeFromWatchList(animeEntry)
            }
        }

        on("clearing all lists") {
            inMemoryPersistenceHandler.clearAll()

            it("must delegate to all three list handlers") {
                verify(animeListHandler, times(1)).clear()
                verify(filterListHandler, times(1)).clear()
                verify(watchListHandler, times(1)).clear()
            }
        }
    }


    given("an instance of a persistence handler and a list of Anime") {
        val list = mutableListOf(
                Anime(
                        "Made in Abyss",
                        InfoLink("https://myanimelist.net/anime/34599")
                ),
                Anime(
                        "Death Note",
                        InfoLink("http://myanimelist.net/anime/1535")
                )
        )

        val inMemoryPersistenceHandler = InMemoryPersistenceHandler(
            animeListHandler,
            filterListHandler,
            watchListHandler
        )

        on("adding an anime list") {
            inMemoryPersistenceHandler.addAnimeList(list)

            it("must delegate to the InMemoryAnimeListHandler") {
                verify(animeListHandler, times(1)).addAnime(list[0])
                verify(animeListHandler, times(1)).addAnime(list[1])
            }
        }
    }


    given("an instance of a persistence handler and a list of FilterListEntry") {
        val list = mutableListOf(
            FilterListEntry(
                "Made in Abyss",
                InfoLink("https://myanimelist.net/anime/34599")
            ),
            FilterListEntry(
                    "Death Note",
                    InfoLink("http://myanimelist.net/anime/1535")
            )
        )

        val inMemoryPersistenceHandler = InMemoryPersistenceHandler(
            animeListHandler,
            filterListHandler,
            watchListHandler
        )

        on("adding a filter list") {
            inMemoryPersistenceHandler.addFilterList(list)

            it("must delegate to the InMemoryFilterListHandler") {
                verify(filterListHandler, times(1)).filterAnime(list[0])
                verify(filterListHandler, times(1)).filterAnime(list[1])
            }
        }
    }


    given("an instance of a persistence handler and a list of WatchListEntry") {
        val list = mutableListOf(
                WatchListEntry(
                        "Made in Abyss",
                        InfoLink("https://myanimelist.net/anime/34599")
                ),
                WatchListEntry(
                        "Death Note",
                        InfoLink("http://myanimelist.net/anime/1535")
                )
        )

        val inMemoryPersistenceHandler = InMemoryPersistenceHandler(
            animeListHandler,
            filterListHandler,
            watchListHandler
        )

        on("adding a filter list") {
            inMemoryPersistenceHandler.addWatchList(list)

            it("must delegate to the InMemoryWatchListHandler") {
                verify(watchListHandler, times(1)).watchAnime(list[0])
                verify(watchListHandler, times(1)).watchAnime(list[1])
            }
        }
    }


    given("an instance of a persistence handler and an Anime") {
        val entry = Anime(
            "Death Note",
            InfoLink("http://myanimelist.net/anime/1535")
        )

        val inMemoryPersistenceHandler = InMemoryPersistenceHandler(
            animeListHandler,
            filterListHandler,
            watchListHandler
        )

        on("updating an Anime") {
            inMemoryPersistenceHandler.updateOrCreate(entry)

            it("must delegate to the InMemoryAnimeListHandler") {
                verify(animeListHandler, times(1)).updateOrCreate(entry)
            }
        }
    }


    given("an instance of a persistence handler and a FilterListEntry") {
        val entry = FilterListEntry(
            "Death Note",
            InfoLink("http://myanimelist.net/anime/1535")
        )

        val inMemoryPersistenceHandler = InMemoryPersistenceHandler(
            animeListHandler,
            filterListHandler,
            watchListHandler
        )

        on("updating a FilterListEntry") {
            inMemoryPersistenceHandler.updateOrCreate(entry)

            it("must delegate to the InMemoryFilterListHandler") {
                verify(filterListHandler, times(1)).updateOrCreate(entry)
            }
        }
    }


    given("an instance of a persistence handler and a WatchListEntry") {
        val entry = WatchListEntry(
            "Death Note",
            InfoLink("http://myanimelist.net/anime/1535")
        )

        val inMemoryPersistenceHandler = InMemoryPersistenceHandler(
            animeListHandler,
            filterListHandler,
            watchListHandler
        )

        on("updating a WatchListEntry") {
            inMemoryPersistenceHandler.updateOrCreate(entry)

            it("must delegate to the InMemoryWatchListHandler") {
                verify(watchListHandler, times(1)).updateOrCreate(entry)
            }
        }
    }
})