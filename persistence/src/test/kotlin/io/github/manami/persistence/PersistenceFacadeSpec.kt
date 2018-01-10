package io.github.manami.persistence

import com.google.common.eventbus.EventBus
import io.github.manami.dto.AnimeType
import io.github.manami.dto.entities.Anime
import io.github.manami.dto.entities.FilterListEntry
import io.github.manami.dto.entities.InfoLink
import io.github.manami.dto.entities.WatchListEntry
import io.github.manami.persistence.events.AnimeListChangedEvent
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
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import java.net.URL
import java.util.*

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


    given("a FilterListEntry without a title and an empty list") {
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
                verify(eventBusMock, never()).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(WatchListChangedEvent::class.java))
            }

            it("must not increase filter list") {
                assertThat(persistenceFacade.fetchFilterList().size).isZero()
            }

            it("must not exist on the filterList") {
                assertThat(persistenceFacade.filterListEntryExists(entry.infoLink)).isFalse()
            }
        }

        on("calling updateOrCreate with that entry") {
            persistenceFacade.updateOrCreate(entry)

            it("must not fire a FilterListChangedEvent or any other list change event.") {
                verify(eventBusMock, never()).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(WatchListChangedEvent::class.java))
            }

            it("must not increase filter list") {
                assertThat(persistenceFacade.fetchFilterList().size).isZero()
            }

            it("must not exist on the filterList") {
                assertThat(persistenceFacade.filterListEntryExists(entry.infoLink)).isFalse()
            }
        }
    }


    given("a FilterListEntry without a valid InfoLink and an empty list") {
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
                verify(eventBusMock, never()).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(WatchListChangedEvent::class.java))
            }

            it("must not increase filter list") {
                assertThat(persistenceFacade.fetchFilterList().size).isZero()
            }

            it("must not exist on the filterList") {
                assertThat(persistenceFacade.filterListEntryExists(entry.infoLink)).isFalse()
            }
        }

        on("calling updateOrCreate with that entry") {
            persistenceFacade.updateOrCreate(entry)

            it("must not fire a FilterListChangedEvent or any other list change event") {
                verify(eventBusMock, never()).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(WatchListChangedEvent::class.java))
            }

            it("must not increase filter list") {
                assertThat(persistenceFacade.fetchFilterList().size).isZero()
            }

            it("must not exist on the filterList") {
                assertThat(persistenceFacade.filterListEntryExists(entry.infoLink)).isFalse()
            }
        }
    }


    given("a FilterListEntry without a thumbnail and an empty list") {
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
                verify(eventBusMock, never()).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, times(1)).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(WatchListChangedEvent::class.java))
            }

            it("must increase filter list") {
                assertThat(persistenceFacade.fetchFilterList().size).isOne()
            }

            it("must exist on the filterList") {
                assertThat(persistenceFacade.filterListEntryExists(entry.infoLink)).isTrue()
            }
        }

        on("calling updateOrCreate with that entry") {
            persistenceFacade.updateOrCreate(entry)

            it("must fire a FilterListChangedEvent, but none of the other list change events") {
                verify(eventBusMock, never()).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, times(1)).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(WatchListChangedEvent::class.java))
            }

            it("must increase filter list") {
                assertThat(persistenceFacade.fetchFilterList().size).isOne()
            }

            it("must exist on the filterList") {
                assertThat(persistenceFacade.filterListEntryExists(entry.infoLink)).isTrue()
            }
        }
    }
    

    given("a valid FilterListEntry and an empty list") {
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
                verify(eventBusMock, never()).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, times(1)).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(WatchListChangedEvent::class.java))
            }

            it("must increase filter list") {
                assertThat(persistenceFacade.fetchFilterList().size).isOne()
            }

            it("must exist on the filterList") {
                assertThat(persistenceFacade.filterListEntryExists(entry.infoLink)).isTrue()
            }
        }

        on("calling updateOrCreate with that entry") {
            persistenceFacade.updateOrCreate(entry)

            it("must fire a FilterListChangedEvent, but none of the other list change events") {
                verify(eventBusMock, never()).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, times(1)).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(WatchListChangedEvent::class.java))
            }

            it("must increase filter list") {
                assertThat(persistenceFacade.fetchFilterList().size).isOne()
            }

            it("must exist on the filterList") {
                assertThat(persistenceFacade.filterListEntryExists(entry.infoLink)).isTrue()
            }
        }
    }


    given("one filter entry in the filter list") {
        val infoLink = InfoLink("http://myanimelist.net/anime/1535")

        beforeEachTest {
            persistenceFacade.filterAnime(
                    FilterListEntry(
                            "Death Note",
                            infoLink
                    )
            )

            reset(eventBusMock) // otherwise we've got 2 invocations of post(???ListChangedEvent())
        }

        on("removing the filter entry") {
            val result = persistenceFacade.removeFromFilterList(infoLink)

            it("must return true, because the entry has been removed") {
                assertThat(result).isTrue()
            }

            it("must result in an empty filter list") {
                assertThat(persistenceFacade.fetchFilterList()).isEmpty()
            }

            it("must fire a FilterListChangedEvent, but none of the other list change events") {
                verify(eventBusMock, never()).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, times(1)).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(WatchListChangedEvent::class.java))
            }
        }

        on("changing it's title using a new instance") {
            val newTitle = "My new Title"

            persistenceFacade.updateOrCreate(FilterListEntry(
                    newTitle,
                    infoLink
            ))

            it("must fire a FilterListChangedEvent, but none of the other list change events") {
                verify(eventBusMock, never()).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, times(1)).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(WatchListChangedEvent::class.java))
            }

            it("must contain the changed value") {
                assertThat(persistenceFacade.fetchFilterList()[0].title).isEqualTo(newTitle)
            }
        }

        on("changing it's thumbnail using the instance from fetching the list") {
            val newThumbnail = URL("https://myanimelist.cdn-dena.com/images/anime/5/87048t.jpg")
            val entry = persistenceFacade.fetchFilterList()[0].apply { thumbnail = newThumbnail }
            persistenceFacade.updateOrCreate(entry)

            it("must fire a FilterListChangedEvent, but none of the other list change events") {
                verify(eventBusMock, never()).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, times(1)).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(WatchListChangedEvent::class.java))
            }

            it("must contain the changed value") {
                assertThat(persistenceFacade.fetchFilterList()[0].thumbnail).isEqualTo(newThumbnail)
            }
        }
    }


    given("a WatchListEntry without a title and an empty list") {
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
                verify(eventBusMock, never()).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(WatchListChangedEvent::class.java))
            }

            it("must not increase watchlist") {
                assertThat(persistenceFacade.fetchWatchList().size).isZero()
            }

            it("must not exist on the watchlist") {
                assertThat(persistenceFacade.watchListEntryExists(entry.infoLink)).isFalse()
            }
        }

        on("calling updateOrCreate with that entry") {
            persistenceFacade.updateOrCreate(entry)

            it("must not fire a WatchListChangedEvent or any other list change event") {
                verify(eventBusMock, never()).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(WatchListChangedEvent::class.java))
            }

            it("must not increase watchlist") {
                assertThat(persistenceFacade.fetchWatchList().size).isZero()
            }

            it("must not exist on the watchlist") {
                assertThat(persistenceFacade.watchListEntryExists(entry.infoLink)).isFalse()
            }
        }
    }


    given("a WatchListEntry without a valid InfoLink and an empty list") {
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
                verify(eventBusMock, never()).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(WatchListChangedEvent::class.java))
            }

            it("must not increase watchlist") {
                assertThat(persistenceFacade.fetchWatchList().size).isZero()
            }

            it("must not exist on the watchlist") {
                assertThat(persistenceFacade.watchListEntryExists(entry.infoLink)).isFalse()
            }
        }

        on("calling updateOrCreate with that entry") {
            persistenceFacade.updateOrCreate(entry)

            it("must not fire a WatchListChangedEvent or any other list change event") {
                verify(eventBusMock, never()).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(WatchListChangedEvent::class.java))
            }

            it("must not increase watchlist") {
                assertThat(persistenceFacade.fetchWatchList().size).isZero()
            }

            it("must not exist on the watchlist") {
                assertThat(persistenceFacade.watchListEntryExists(entry.infoLink)).isFalse()
            }
        }
    }


    given("a WatchListEntry without a thumbnail and an empty list") {
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
                verify(eventBusMock, never()).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, times(1)).post(isA(WatchListChangedEvent::class.java))
            }

            it("must increase watchlist") {
                assertThat(persistenceFacade.fetchWatchList().size).isOne()
            }

            it("must exist on the watchlist") {
                assertThat(persistenceFacade.watchListEntryExists(entry.infoLink)).isTrue()
            }
        }

        on("calling updateOrCreate with that entry") {
            persistenceFacade.updateOrCreate(entry)

            it("must fire a WatchListChangedEvent") {
                verify(eventBusMock, never()).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, times(1)).post(isA(WatchListChangedEvent::class.java))
            }

            it("must increase watchlist") {
                assertThat(persistenceFacade.fetchWatchList().size).isOne()
            }

            it("must exist on the watchlist") {
                assertThat(persistenceFacade.watchListEntryExists(entry.infoLink)).isTrue()
            }
        }
    }


    given("a valid WatchListEntry and an empty list") {
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
                verify(eventBusMock, never()).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, times(1)).post(isA(WatchListChangedEvent::class.java))
            }

            it("must increase watchlist") {
                assertThat(persistenceFacade.fetchWatchList().size).isOne()
            }

            it("must exist on the watchlist") {
                assertThat(persistenceFacade.watchListEntryExists(entry.infoLink)).isTrue()
            }
        }

        on("calling updateOrCreate with that entry") {
            persistenceFacade.updateOrCreate(entry)

            it("must fire a WatchListChangedEvent") {
                verify(eventBusMock, never()).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, times(1)).post(isA(WatchListChangedEvent::class.java))
            }

            it("must increase watchlist") {
                assertThat(persistenceFacade.fetchWatchList().size).isOne()
            }

            it("must exist on the watchlist") {
                assertThat(persistenceFacade.watchListEntryExists(entry.infoLink)).isTrue()
            }
        }
    }


    given("one filter entry in the watchlist") {
        val infoLink = InfoLink("http://myanimelist.net/anime/1535")

        beforeEachTest {
            persistenceFacade.watchAnime(
                    WatchListEntry(
                            "Death Note",
                            infoLink
                    )
            )

            reset(eventBusMock) // otherwise we've got 2 invocations of post(???ListChangedEvent())
        }

        on("removing the watchlist entry") {
            val result = persistenceFacade.removeFromWatchList(infoLink)

            it("must return true, because the entry has been removed") {
                assertThat(result).isTrue()
            }

            it("must result in an empty watchlist") {
                assertThat(persistenceFacade.fetchWatchList()).isEmpty()
            }

            it("must fire a WatchListChangedEvent") {
                verify(eventBusMock, never()).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, times(1)).post(isA(WatchListChangedEvent::class.java))
            }
        }

        on("changing it's title using a new object instance") {
            val newTitle = "My new Title"

            persistenceFacade.updateOrCreate(WatchListEntry(
                    newTitle,
                    infoLink
            ))

            it("must fire a WatchListChangedEvent, but none of the other list change events") {
                verify(eventBusMock, never()).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, times(1)).post(isA(WatchListChangedEvent::class.java))
            }

            it("must contain the changed value") {
                assertThat(persistenceFacade.fetchWatchList()[0].title).isEqualTo(newTitle)
            }
        }

        on("changing it's thumbnail using the instance from fetching the list") {
            val newThumbnail = URL("https://myanimelist.cdn-dena.com/images/anime/5/87048t.jpg")
            val entry = persistenceFacade.fetchWatchList()[0].apply { thumbnail = newThumbnail }
            persistenceFacade.updateOrCreate(entry)

            it("must fire a WatchListChangedEvent, but none of the other list change events") {
                verify(eventBusMock, never()).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, times(1)).post(isA(WatchListChangedEvent::class.java))
            }

            it("must contain the changed value") {
                assertThat(persistenceFacade.fetchWatchList()[0].thumbnail).isEqualTo(newThumbnail)
            }
        }
    }


    given("an Anime without a title and an empty list") {
        val entry = Anime(
                "",
                InfoLink("http://myanimelist.net/anime/1535")
        )

        on("adding that entry") {
            val result = persistenceFacade.addAnime(entry)

            it("must return false, because the entry has not been added to the animelist") {
                assertThat(result).isFalse()
            }

            it("must not fire an AnimeListChangedEvent or any other list change event.") {
                verify(eventBusMock, never()).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(WatchListChangedEvent::class.java))
            }

            it("must not increase animelist") {
                assertThat(persistenceFacade.fetchAnimeList().size).isZero()
            }

            it("must not exist in the list") {
                assertThat(persistenceFacade.animeEntryExists(entry.infoLink))
            }
        }

        on("calling updateOrCreate with that entry") {
            persistenceFacade.updateOrCreate(entry)

            it("must not fire an AnimeListChangedEvent or any other list change event.") {
                verify(eventBusMock, never()).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(WatchListChangedEvent::class.java))
            }

            it("must not increase animelist") {
                assertThat(persistenceFacade.fetchAnimeList().size).isZero()
            }

            it("must not exist in the list") {
                assertThat(persistenceFacade.animeEntryExists(entry.infoLink))
            }
        }
    }


    given("an Anime without a valid InfoLink and an empty list") {
        val entry = Anime(
                "Death Note",
                InfoLink("")
        )

        on("adding that entry") {
            val result = persistenceFacade.addAnime(entry)

            it("must return true, because the entry has been added. A valid infoLink is not necessary for an anime.") {
                assertThat(result).isTrue()
            }

            it("must fire an AnimeListChangedEvent or any other list change event") {
                verify(eventBusMock, times(1)).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(WatchListChangedEvent::class.java))
            }

            it("must increase animelist") {
                assertThat(persistenceFacade.fetchAnimeList().size).isOne()
            }

            //TODO: okay so what happens if we check the animeExists function. Especially with multiple entries having no infolink
        }

        on("calling updateOrCreate with that entry") {
            persistenceFacade.updateOrCreate(entry)

            it("must fire an AnimeListChangedEvent or any other list change event") {
                verify(eventBusMock, times(1)).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(WatchListChangedEvent::class.java))
            }

            it("must increase animelist") {
                assertThat(persistenceFacade.fetchAnimeList().size).isOne()
            }

            //TODO: okay so what happens if we check the animeExists function. Especially with multiple entries having no infolink
        }
    }


    given("a valid minimal Anime and an empty list") {
        val entry = Anime(
                "Death Note",
                InfoLink("http://myanimelist.net/anime/1535")
        )

        on("adding that entry") {
            val result = persistenceFacade.addAnime(entry)

            it("must return true, because the entry has been added to the animelist") {
                assertThat(result).isTrue()
            }

            it("must fire an AnimeListChangedEvent, but no other list change event") {
                verify(eventBusMock, times(1)).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(WatchListChangedEvent::class.java))
            }

            it("must increase animelist") {
                assertThat(persistenceFacade.fetchAnimeList().size).isOne()
            }

            it("must exist on the list") {
                assertThat(persistenceFacade.animeEntryExists(entry.infoLink)).isTrue()
            }
        }

        on("calling updateOrCreate with that entry") {
            persistenceFacade.updateOrCreate(entry)

            it("must fire an AnimeListChangedEvent, but no other list change event") {
                verify(eventBusMock, times(1)).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(WatchListChangedEvent::class.java))
            }

            it("must increase animelist") {
                assertThat(persistenceFacade.fetchAnimeList().size).isOne()
            }

            it("must exist on the list") {
                assertThat(persistenceFacade.animeEntryExists(entry.infoLink)).isTrue()
            }
        }
    }


    given("a valid Anime and an empty list") {
        val entry = Anime(
            "Death Note",
            InfoLink("http://myanimelist.net/anime/1535"),
            37,
            AnimeType.TV,
            "/death_note",
            URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg"),
            URL("http://cdn.myanimelist.net/images/anime/9/9453.jpg")
        )

        on("adding that entry") {
            val result = persistenceFacade.addAnime(entry)

            it("must return true, because the entry has been added to the animelist") {
                assertThat(result).isTrue()
            }

            it("must fire an AnimeListChangedEvent, but no other list change event") {
                verify(eventBusMock, times(1)).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(WatchListChangedEvent::class.java))
            }

            it("must increase animelist") {
                assertThat(persistenceFacade.fetchAnimeList().size).isOne()
            }

            it("must exist on the list") {
                assertThat(persistenceFacade.animeEntryExists(entry.infoLink)).isTrue()
            }
        }

        on("calling updateOrCreate with that entry") {
            persistenceFacade.updateOrCreate(entry)

            it("must fire an AnimeListChangedEvent, but no other list change event") {
                verify(eventBusMock, times(1)).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(WatchListChangedEvent::class.java))
            }

            it("must increase animelist") {
                assertThat(persistenceFacade.fetchAnimeList().size).isOne()
            }

            it("must exist on the list") {
                assertThat(persistenceFacade.animeEntryExists(entry.infoLink)).isTrue()
            }
        }
    }


    given("one anime entry in the list") {
        val infoLink = InfoLink("http://myanimelist.net/anime/1535")
        var id: UUID = UUID.randomUUID()

        beforeEachTest {
            persistenceFacade.addAnime(
                    Anime(
                            "Death Note",
                            infoLink
                    )
            )

            id = persistenceFacade.fetchAnimeList()[0].id

            reset(eventBusMock) // otherwise we've got 2 invocations of post(???ListChangedEvent())
        }

        on("removing the entry") {
            val result = persistenceFacade.removeAnime(id)

            it("must return true, because the entry has been removed") {
                assertThat(result).isTrue()
            }

            it("must result in an empty list") {
                assertThat(persistenceFacade.fetchAnimeList()).isEmpty()
            }

            it("must fire a WatchListChangedEvent") {
                verify(eventBusMock, times(1)).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(WatchListChangedEvent::class.java))
            }

            it("must not exist on the list, because it has been removed") {
                assertThat(persistenceFacade.animeEntryExists(infoLink)).isFalse()
            }
        }

        on("changing it's title using a new object instance") {
            val newTitle = "My new Title"

            persistenceFacade.updateOrCreate(
                Anime(
                    "Death Note",
                    InfoLink("http://myanimelist.net/anime/1535"),
                    37,
                    AnimeType.TV,
                    "/death_note",
                    URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg"),
                    URL("http://cdn.myanimelist.net/images/anime/9/9453.jpg")
                )
            )

            it("must fire an AnimeListChangedEvent, but none of the other list change events") {
                verify(eventBusMock, times(1)).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(WatchListChangedEvent::class.java))
            }

            it("must not change the new value, because unlike Watch- and FilterListEntries Animes own a specific UUID. Their InfoLinks can change.") {
                assertThat(persistenceFacade.fetchAnimeList()[0].title).isNotEqualTo(newTitle)
            }

            it("must result in an additional entry") {
                assertThat(persistenceFacade.fetchAnimeList().size).isEqualTo(2)
            }
        }

        on("changing it's title using the instance from fetching the list") {
            val newTitle = "My new Title"
            val entry = persistenceFacade.fetchAnimeList()[0].apply { title = newTitle }
            persistenceFacade.updateOrCreate(entry)

            it("must fire an AnimeListChangedEvent, but none of the other list change events") {
                verify(eventBusMock, times(1)).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(WatchListChangedEvent::class.java))
            }

            it("must contain the changed value") {
                assertThat(persistenceFacade.fetchAnimeList()[0].title).isEqualTo(newTitle)
            }
        }

        on("changing it's thumbnail using the instance from fetching the list") {
            val newThumbnail = URL("https://myanimelist.cdn-dena.com/images/anime/5/87048t.jpg")
            val entry = persistenceFacade.fetchAnimeList()[0].apply { thumbnail = newThumbnail }
            persistenceFacade.updateOrCreate(entry)

            it("must fire an AnimeListChangedEvent, but none of the other list change events") {
                verify(eventBusMock, times(1)).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(WatchListChangedEvent::class.java))
            }

            it("must contain the changed value") {
                assertThat(persistenceFacade.fetchAnimeList()[0].thumbnail).isEqualTo(newThumbnail)
            }
        }

        on("changing it's picture using the instance from fetching the list") {
            val newPicture = URL("https://myanimelist.cdn-dena.com/images/anime/5/87048t.jpg")
            val entry = persistenceFacade.fetchAnimeList()[0].apply { picture = newPicture }
            persistenceFacade.updateOrCreate(entry)

            it("must fire an AnimeListChangedEvent, but none of the other list change events") {
                verify(eventBusMock, times(1)).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(WatchListChangedEvent::class.java))
            }

            it("must contain the changed value") {
                assertThat(persistenceFacade.fetchAnimeList()[0].picture).isEqualTo(newPicture)
            }
        }

        on("changing it's number of episodes using the instance from fetching the list") {
            val newValueForEpisodes = 178
            val entry = persistenceFacade.fetchAnimeList()[0].apply { episodes = newValueForEpisodes }
            persistenceFacade.updateOrCreate(entry)

            it("must fire an AnimeListChangedEvent, but none of the other list change events") {
                verify(eventBusMock, times(1)).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(WatchListChangedEvent::class.java))
            }

            it("must contain the changed value") {
                assertThat(persistenceFacade.fetchAnimeList()[0].episodes).isEqualTo(newValueForEpisodes)
            }
        }

        on("changing it's infolink using the instance from fetching the list") {
            val newInfoLink = InfoLink("https://myanimelist.net/anime/123456789")
            val entry = persistenceFacade.fetchAnimeList()[0].apply { this.infoLink = newInfoLink }
            persistenceFacade.updateOrCreate(entry)

            it("must fire an AnimeListChangedEvent, but none of the other list change events") {
                verify(eventBusMock, times(1)).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(WatchListChangedEvent::class.java))
            }

            it("must contain the changed value") {
                assertThat(persistenceFacade.fetchAnimeList()[0].infoLink).isEqualTo(newInfoLink)
            }
        }

        on("changing it's type using the instance from fetching the list") {
            val newAnimeType = AnimeType.ONA
            val entry = persistenceFacade.fetchAnimeList()[0].apply { type = newAnimeType }
            persistenceFacade.updateOrCreate(entry)

            it("must fire an AnimeListChangedEvent, but none of the other list change events") {
                verify(eventBusMock, times(1)).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(WatchListChangedEvent::class.java))
            }

            it("must contain the changed value") {
                assertThat(persistenceFacade.fetchAnimeList()[0].type).isEqualTo(newAnimeType)
            }
        }

        on("changing it's type using the instance from fetching the list") {
            val newLocation = "some/new/path"
            val entry = persistenceFacade.fetchAnimeList()[0].apply { location = newLocation }
            persistenceFacade.updateOrCreate(entry)

            it("must fire an AnimeListChangedEvent, but none of the other list change events") {
                verify(eventBusMock, times(1)).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(WatchListChangedEvent::class.java))
            }

            it("must contain the changed value") {
                assertThat(persistenceFacade.fetchAnimeList()[0].location).isEqualTo(newLocation)
            }
        }
    }


    given("one entry in each list") {

        beforeEachTest {
            persistenceFacade.addAnime(
                    Anime(
                            "Death Note",
                            InfoLink("http://myanimelist.net/anime/1535")
                    )
            )

            persistenceFacade.filterAnime(
                    FilterListEntry(
                            "Gintama.",
                            InfoLink("https://myanimelist.net/anime/34096")
                    )
            )

            persistenceFacade.watchAnime(
                    WatchListEntry(
                            "Kimi no Na wa.",
                            InfoLink("https://myanimelist.net/anime/32281")
                    )
            )

            reset(eventBusMock) // otherwise we've got 2 invocations of post(????ListChangedEvent())
        }

        on("clearing all") {
            persistenceFacade.clearAll()

            it("must result in an empty list") {
                assertThat(persistenceFacade.fetchAnimeList()).isEmpty()
            }

            it("must result in an empty filterlist") {
                assertThat(persistenceFacade.fetchFilterList()).isEmpty()
            }

            it("must result in an empty watchlist") {
                assertThat(persistenceFacade.fetchWatchList()).isEmpty()
            }

            it("must fire an event for each list") {
                verify(eventBusMock, times(1)).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, times(1)).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, times(1)).post(isA(WatchListChangedEvent::class.java))
            }
        }
    }

    given("a list of valid FilterListEntries and an empty filterlist") {
        val list: MutableList<FilterListEntry> = mutableListOf(
            FilterListEntry(
                "Death Note",
                InfoLink("http://myanimelist.net/anime/1535")
            ),
            FilterListEntry(
                "Gintama",
                InfoLink("http://myanimelist.net/anime/28977")
            ),
            FilterListEntry(
                "Steins;Gate",
                InfoLink("http://myanimelist.net/anime/9253")
            )
        )

        on("adding this filterlist") {
            persistenceFacade.addFilterList(list)

            it("must fire a FilterListChangedEvent, but none of the other list change events") {
                verify(eventBusMock, never()).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, times(1)).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(WatchListChangedEvent::class.java))
            }

            it("must contain the exact same amount of entries that has been inserted") {
                assertThat(persistenceFacade.fetchFilterList().size).isEqualTo(list.size)
            }
        }
    }


    given("a list of one valid and two invalid FilterListEntries and an empty filterlist") {
        val list: MutableList<FilterListEntry> = mutableListOf(
                FilterListEntry(
                        "",
                        InfoLink("http://myanimelist.net/anime/1535")
                ),
                FilterListEntry(
                        "Gintama",
                        InfoLink("http://myanimelist.net/anime/28977")
                ),
                FilterListEntry(
                        "Steins;Gate",
                        InfoLink("")
                )
        )

        on("adding this filterlist") {
            persistenceFacade.addFilterList(list)

            it("must fire a FilterListChangedEvent, but none of the other list change events") {
                verify(eventBusMock, never()).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, times(1)).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(WatchListChangedEvent::class.java))
            }

            it("must contain only the one valid entry") {
                assertThat(persistenceFacade.fetchFilterList().size).isOne()
            }
        }
    }


    given("a list of valid WatchListEntries and an empty watchlist") {
        val list: MutableList<WatchListEntry> = mutableListOf(
                WatchListEntry(
                        "Death Note",
                        InfoLink("http://myanimelist.net/anime/1535")
                ),
                WatchListEntry(
                        "Gintama",
                        InfoLink("http://myanimelist.net/anime/28977")
                ),
                WatchListEntry(
                        "Steins;Gate",
                        InfoLink("http://myanimelist.net/anime/9253")
                )
        )

        on("adding this watchlist") {
            persistenceFacade.addWatchList(list)

            it("must fire a WatchListChangedEvent, but none of the other list change events") {
                verify(eventBusMock, never()).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, times(1)).post(isA(WatchListChangedEvent::class.java))
            }

            it("must contain the exact same amount of entries that has been inserted") {
                assertThat(persistenceFacade.fetchWatchList().size).isEqualTo(list.size)
            }
        }
    }


    given("a list of one valid and two invalid WatchListEntries and an empty Watchlist") {
        val list: MutableList<WatchListEntry> = mutableListOf(
                WatchListEntry(
                        "",
                        InfoLink("http://myanimelist.net/anime/1535")
                ),
                WatchListEntry(
                        "Gintama",
                        InfoLink("http://myanimelist.net/anime/28977")
                ),
                WatchListEntry(
                        "Steins;Gate",
                        InfoLink("")
                )
        )

        on("adding this watchlist") {
            persistenceFacade.addWatchList(list)

            it("must fire a WatchListChangedEvent, but none of the other list change events") {
                verify(eventBusMock, never()).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, times(1)).post(isA(WatchListChangedEvent::class.java))
            }

            it("must contain only the one valid entry") {
                assertThat(persistenceFacade.fetchWatchList().size).isOne()
            }
        }
    }


    given("a list of valid Anime and an empty list") {
        val list: MutableList<Anime> = mutableListOf(
                Anime(
                        "Death Note",
                        InfoLink("http://myanimelist.net/anime/1535")
                ),
                Anime(
                        "Gintama",
                        InfoLink("http://myanimelist.net/anime/28977")
                ),
                Anime(
                        "Steins;Gate",
                        InfoLink("http://myanimelist.net/anime/9253")
                )
        )

        on("adding this animelist") {
            persistenceFacade.addAnimeList(list)

            it("must fire a AnimeListChangedEvent, but none of the other list change events") {
                verify(eventBusMock, times(1)).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(WatchListChangedEvent::class.java))
            }

            it("must contain the exact same amount of entries that has been inserted") {
                assertThat(persistenceFacade.fetchAnimeList().size).isEqualTo(list.size)
            }
        }
    }


    given("a list of one valid and two invalid Anime and an empty animelist") {
        val list: MutableList<Anime> = mutableListOf(
                Anime(
                        "",
                        InfoLink("http://myanimelist.net/anime/1535")
                ),
                Anime(
                        "Gintama",
                        InfoLink("http://myanimelist.net/anime/28977")
                ),
                Anime(
                        "Steins;Gate",
                        InfoLink("http://myanimelist.net/anime/9253")
                ).apply { location = "" }
        )

        on("adding this animelist") {
            persistenceFacade.addAnimeList(list)

            it("must fire a AnimeListChangedEvent, but none of the other list change events") {
                verify(eventBusMock, times(1)).post(isA(AnimeListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(FilterListChangedEvent::class.java))
                verify(eventBusMock, never()).post(isA(WatchListChangedEvent::class.java))
            }

            it("must contain only the one valid entry") {
                assertThat(persistenceFacade.fetchAnimeList().size).isOne()
            }
        }
    }
})