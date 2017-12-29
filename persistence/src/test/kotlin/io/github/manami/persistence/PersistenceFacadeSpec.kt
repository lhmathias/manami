package io.github.manami.persistence

import com.google.common.eventbus.EventBus
import io.github.manami.dto.entities.FilterListEntry
import io.github.manami.dto.entities.InfoLink
import io.github.manami.dto.entities.WatchListEntry
import io.github.manami.persistence.events.FilterListChangedEvent
import io.github.manami.persistence.events.WatchListChangedEvent
import io.github.manami.persistence.inmemory.InMemoryPersistenceHandler
import io.github.manami.persistence.inmemory.animelist.InMemoryAnimeListHandler
import io.github.manami.persistence.inmemory.filterlist.InMemoryFilterListHandler
import io.github.manami.persistence.inmemory.watchlist.InMemoryWatchListHandler
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.mockito.Mockito.*
import java.net.URL

class PersistenceFacadeSpec : Spek({

    var inMemoryPersistenceHandler = InMemoryPersistenceHandler(
        InMemoryAnimeListHandler(),
        InMemoryFilterListHandler(),
        InMemoryWatchListHandler()
    )
    var eventBusMock = mock(EventBus::class.java)
    var persistenceFacade = PersistenceFacade(inMemoryPersistenceHandler, eventBusMock)


    beforeEachTest {
        inMemoryPersistenceHandler = InMemoryPersistenceHandler(
            InMemoryAnimeListHandler(),
            InMemoryFilterListHandler(),
            InMemoryWatchListHandler()
        )
        eventBusMock = mock(EventBus::class.java)
        persistenceFacade = PersistenceFacade(inMemoryPersistenceHandler, eventBusMock)
    }


    given("a FilterListEntry without a title") {
        val entry = FilterListEntry(
            "",
            InfoLink("http://myanimelist.net/anime/1535")
        )

        on("filtering that entry") {
            val result = persistenceFacade.filterAnime(entry)

            it("must return false, because the entry has not been added to the filter list") {
                assertThat(result).isFalse()
            }

            it("must not fire a FilterListChangedEvent or any other list change event.") {
                verify(eventBusMock, never()).post(any(FilterListChangedEvent::class.java))
            }

            it("must not increase filter list") {
                assertThat(persistenceFacade.fetchFilterList().size).isZero()
            }
        }
    }


    given("a FilterListEntry without a valid InfoLink") {
        val entry = FilterListEntry(
                "Death Note",
                InfoLink("")
        )

        on("filtering that entry") {
            val result = persistenceFacade.filterAnime(entry)

            it("must return false, because the entry has not been added to the filter list") {
                assertThat(result).isFalse()
            }

            it("must not fire a FilterListChangedEvent or any other list change event") {
                verify(eventBusMock, never()).post(any(FilterListChangedEvent::class.java))
            }

            it("must not increase filter list") {
                assertThat(persistenceFacade.fetchFilterList().size).isZero()
            }
        }
    }


    given("a FilterListEntry without a thumbnail") {
        val infoLink = "http://myanimelist.net/anime/1535"
        val entry = FilterListEntry(
                "Death Note",
                InfoLink(infoLink)
        )

        on("filtering that entry") {
            val result = persistenceFacade.filterAnime(entry)

            it("must return true, because it is valid using a default thumbnail.") {
                assertThat(result).isTrue()
            }

            it("must fire a FilterListChangedEvent, but none of the other list change events") {
                verify(eventBusMock, times(1)).post(any(FilterListChangedEvent::class.java))
            }

            it("must increase filter list") {
                assertThat(persistenceFacade.fetchFilterList().size).isOne()
            }
        }

        on("checking if the FilterListEntry exists on an empty filterlist") {
            val result = persistenceFacade.filterListEntryExists(InfoLink(infoLink))

            it("must return false") {
                assertThat(result).isFalse()
            }
        }
    }
    

    given("a valid FilterListEntry") {
        val infoLink = "http://myanimelist.net/anime/1535"
        val entry = FilterListEntry(
                "Death Note",
                InfoLink(infoLink),
                URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg")
        )

        on("filtering that entry") {
            val result = persistenceFacade.filterAnime(entry)

            it("must return true, because it has been added to the filter list") {
                assertThat(result).isTrue()
            }

            it("must fire a FilterListChangedEvent, but none of the other list change events") {
                verify(eventBusMock, times(1)).post(any(FilterListChangedEvent::class.java))
            }

            it("must increase filter list") {
                assertThat(persistenceFacade.fetchFilterList().size).isOne()
            }
        }

        on("checking if the FilterListEntry exists on an empty filterlist") {
            val result = persistenceFacade.filterListEntryExists(InfoLink(infoLink))

            it("must return false") {
                assertThat(result).isFalse()
            }
        }
    }


    given("one filter entry in the filter list") {
        val infoLink = "http://myanimelist.net/anime/1535"

        beforeEachTest {
            persistenceFacade.filterAnime(
                    FilterListEntry(
                            "Death Note",
                            InfoLink(infoLink)
                    )
            )

            reset(eventBusMock) // otherwise we've got 2 invocations of post(FilterListChangedEvent())
        }

        on("checking if FilterListEntry exists") {
            val result = persistenceFacade.filterListEntryExists(InfoLink(infoLink))

            it("must return true") {
                assertThat(result).isTrue()
            }
        }

        on("removing the filter entry") {
            val result = persistenceFacade.removeFromFilterList(InfoLink(infoLink))

            it("must return true, because the entry has been removed") {
                assertThat(result).isTrue()
            }

            it("must result in an empty filter list") {
                assertThat(persistenceFacade.fetchFilterList()).isEmpty()
            }

            it("must fire a FilterListChangedEvent, but none of the other list change events") {
                verify(eventBusMock, times(1)).post(any(FilterListChangedEvent::class.java))
            }
        }
    }


    given("a WatchListEntry without a title") {
        val entry = WatchListEntry(
                "",
                InfoLink("http://myanimelist.net/anime/1535")
        )

        on("adding that entry to the watchlist") {
            val result = persistenceFacade.watchAnime(entry)

            it("must return false, because the entry has not been added to the watchlist") {
                assertThat(result).isFalse()
            }

            it("must not fire a WatchListChangedEvent or any other list change event") {
                verify(eventBusMock, never()).post(any(WatchListChangedEvent::class.java))
            }

            it("must not increase watchlist") {
                assertThat(persistenceFacade.fetchWatchList().size).isZero()
            }
        }
    }


    given("a WatchListEntry without a valid InfoLink") {
        val entry = WatchListEntry(
                "Death Note",
                InfoLink("")
        )

        on("adding that entry to the watchlist") {
            val result = persistenceFacade.watchAnime(entry)

            it("must return false, because the entry has not been added to the watchlist") {
                assertThat(result).isFalse()
            }

            it("must not fire a WatchListChangedEvent or any other list change event") {
                verify(eventBusMock, never()).post(any(WatchListChangedEvent::class.java))
            }

            it("must not increase watchlist") {
                assertThat(persistenceFacade.fetchWatchList().size).isZero()
            }
        }
    }


    given("a WatchListEntry without a thumbnail") {
        val infoLink = "http://myanimelist.net/anime/1535"
        val entry = WatchListEntry(
                "Death Note",
                InfoLink(infoLink)
        )

        on("adding that entry to the watchlist") {
            val result = persistenceFacade.watchAnime(entry)

            it("must return true, because it is valid using a default thumbnail.") {
                assertThat(result).isTrue()
            }

            it("must fire a WatchListChangedEvent") {
                verify(eventBusMock, times(1)).post(any(WatchListChangedEvent::class.java))
            }

            it("must increase watchlist") {
                assertThat(persistenceFacade.fetchWatchList().size).isOne()
            }
        }

        on("checking if the WatchListEntry exists on an empty watchlist") {
            val result = persistenceFacade.watchListEntryExists(InfoLink(infoLink))

            it("must return false") {
                assertThat(result).isFalse()
            }
        }
    }


    given("a valid WatchListEntry") {
        val infoLink = "http://myanimelist.net/anime/1535"
        val entry = WatchListEntry(
                "Death Note",
                InfoLink(infoLink),
                URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg")
        )

        on("adding that entry to the watchlist") {
            val result = persistenceFacade.watchAnime(entry)

            it("must return true, because it has been added to the watchlist") {
                assertThat(result).isTrue()
            }

            it("must fire a WatchListChangedEvent") {
                verify(eventBusMock, times(1)).post(any(WatchListChangedEvent::class.java))
            }

            it("must increase watchlist") {
                assertThat(persistenceFacade.fetchWatchList().size).isOne()
            }
        }

        on("checking if the WatchListEntry exists on an empty watchlist") {
            val result = persistenceFacade.watchListEntryExists(InfoLink(infoLink))

            it("must return false") {
                assertThat(result).isFalse()
            }
        }
    }


    given("one filter entry in the watchlist") {
        val infoLink = "http://myanimelist.net/anime/1535"

        beforeEachTest {
            persistenceFacade.watchAnime(
                    WatchListEntry(
                            "Death Note",
                            InfoLink(infoLink)
                    )
            )

            reset(eventBusMock) // otherwise we've got 2 invocations of post(FilterListChangedEvent())
        }

        on("checking if WatchListEntry exists") {
            val result = persistenceFacade.watchListEntryExists(InfoLink(infoLink))

            it("must return true") {
                assertThat(result).isTrue()
            }
        }

        on("removing the watchlist entry") {
            val result = persistenceFacade.removeFromWatchList(InfoLink(infoLink))

            it("must return true, because the entry has been removed") {
                assertThat(result).isTrue()
            }

            it("must result in an empty watchlist") {
                assertThat(persistenceFacade.fetchWatchList()).isEmpty()
            }

            it("must fire a WatchListChangedEvent") {
                verify(eventBusMock, times(1)).post(any(WatchListChangedEvent::class.java))
            }
        }
    }
})