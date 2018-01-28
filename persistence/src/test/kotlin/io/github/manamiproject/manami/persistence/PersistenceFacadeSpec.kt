package io.github.manamiproject.manami.persistence

import com.google.common.eventbus.Subscribe
import io.github.manamiproject.manami.common.EventBus
import io.github.manamiproject.manami.dto.AnimeType
import io.github.manamiproject.manami.dto.entities.Anime
import io.github.manamiproject.manami.dto.entities.FilterListEntry
import io.github.manamiproject.manami.dto.entities.InfoLink
import io.github.manamiproject.manami.dto.entities.WatchListEntry
import io.github.manamiproject.manami.persistence.events.AnimeListChangedEvent
import io.github.manamiproject.manami.persistence.events.FilterListChangedEvent
import io.github.manamiproject.manami.persistence.events.WatchListChangedEvent
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.net.URL


private class EventBusListener {

    var receivedAnimeListChangedEvent = 0
    var receivedFilterListChangedEvent = 0
    var receivedWatchListChangedEvent = 0

    @Subscribe
    fun listen(obj: AnimeListChangedEvent) {
        receivedAnimeListChangedEvent++
    }

    @Subscribe
    fun listen(obj: FilterListChangedEvent) {
        receivedFilterListChangedEvent++
    }

    @Subscribe
    fun listen(obj: WatchListChangedEvent) {
        receivedWatchListChangedEvent++
    }
}


@RunWith(JUnitPlatform::class)
class PersistenceFacadeSpec : Spek({

    val persistenceFacade = PersistenceFacade
    val eventBusListener = EventBusListener()
    EventBus.register(eventBusListener)


    beforeEachTest {
        persistenceFacade.clearAll()
        eventBusListener.receivedAnimeListChangedEvent = 0
        eventBusListener.receivedFilterListChangedEvent = 0
        eventBusListener.receivedWatchListChangedEvent = 0
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
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isZero()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
            }

            it("must not increase filter list") {
                assertThat(persistenceFacade.fetchFilterList()).hasSize(0)
            }

            it("must not exist on the filterList") {
                assertThat(persistenceFacade.filterListEntryExists(entry.infoLink)).isFalse()
            }
        }

        on("calling updateOrCreate with that entry") {
            persistenceFacade.updateOrCreate(entry)

            it("must not fire a FilterListChangedEvent or any other list change event.") {
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isZero()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
            }

            it("must not increase filter list") {
                assertThat(persistenceFacade.fetchFilterList()).hasSize(0)
            }

            it("must not exist on the filterList") {
                assertThat(persistenceFacade.filterListEntryExists(entry.infoLink)).isFalse()
            }
        }

        on("removing it from list") {
            val result = persistenceFacade.removeFromFilterList(entry)

            it("must always return false, because invalid entries can't be removed, because by definition they can't be added anyway") {
                assertThat(result).isFalse()
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
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isZero()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
            }

            it("must not increase filter list") {
                assertThat(persistenceFacade.fetchFilterList()).hasSize(0)
            }

            it("must not exist on the filterList") {
                assertThat(persistenceFacade.filterListEntryExists(entry.infoLink)).isFalse()
            }
        }

        on("calling updateOrCreate with that entry") {
            persistenceFacade.updateOrCreate(entry)

            it("must not fire a FilterListChangedEvent or any other list change event") {
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isZero()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
            }

            it("must not increase filter list") {
                assertThat(persistenceFacade.fetchFilterList()).hasSize(0)
            }

            it("must not exist on the filterList") {
                assertThat(persistenceFacade.filterListEntryExists(entry.infoLink)).isFalse()
            }
        }

        on("removing it from list") {
            val result = persistenceFacade.removeFromFilterList(entry)

            it("must always return false, because invalid entries can't be removed, because by definition they can't be added anyway") {
                assertThat(result).isFalse()
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
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isZero()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isOne()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
            }

            it("must increase filter list") {
                assertThat(persistenceFacade.fetchFilterList()).hasSize(1)
            }

            it("must exist on the filterList") {
                assertThat(persistenceFacade.filterListEntryExists(entry.infoLink)).isTrue()
            }
        }

        on("calling updateOrCreate with that entry") {
            persistenceFacade.updateOrCreate(entry)

            it("must fire a FilterListChangedEvent, but none of the other list change events") {
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isZero()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isOne()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
            }

            it("must increase filter list") {
                assertThat(persistenceFacade.fetchFilterList()).hasSize(1)
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
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isZero()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isOne()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
            }

            it("must increase filter list") {
                assertThat(persistenceFacade.fetchFilterList()).hasSize(1)
            }

            it("must exist on the filterList") {
                assertThat(persistenceFacade.filterListEntryExists(entry.infoLink)).isTrue()
            }
        }

        on("calling updateOrCreate with that entry") {
            persistenceFacade.updateOrCreate(entry)

            it("must fire a FilterListChangedEvent, but none of the other list change events") {
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isZero()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isOne()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
            }

            it("must increase filter list") {
                assertThat(persistenceFacade.fetchFilterList()).hasSize(1)
            }

            it("must exist on the filterList") {
                assertThat(persistenceFacade.filterListEntryExists(entry.infoLink)).isTrue()
            }
        }
    }


    given("one filter entry in the filter list") {
        val anime = FilterListEntry(
                "Death Note",
                InfoLink("http://myanimelist.net/anime/1535")
        )

        beforeEachTest {
            persistenceFacade.filterAnime(anime)

            //reset events counter or else we get 2 instead of 1
            eventBusListener.receivedAnimeListChangedEvent = 0
            eventBusListener.receivedFilterListChangedEvent = 0
            eventBusListener.receivedWatchListChangedEvent = 0
        }

        on("removing the filter entry") {
            val result = persistenceFacade.removeFromFilterList(anime)

            it("must return true, because the entry has been removed") {
                assertThat(result).isTrue()
            }

            it("must result in an empty filter list") {
                assertThat(persistenceFacade.fetchFilterList()).isEmpty()
            }

            it("must fire a FilterListChangedEvent, but none of the other list change events") {
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isZero()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isOne()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
            }
        }

        on("changing it's title using a new instance") {
            val newTitle = "My new Title"

            persistenceFacade.updateOrCreate(FilterListEntry(
                    newTitle,
                    anime.infoLink
            ))

            it("must fire a FilterListChangedEvent, but none of the other list change events") {
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isZero()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isOne()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
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
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isZero()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isOne()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
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
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isZero()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
            }

            it("must not increase watchlist") {
                assertThat(persistenceFacade.fetchWatchList()).hasSize(0)
            }

            it("must not exist on the watchlist") {
                assertThat(persistenceFacade.watchListEntryExists(entry.infoLink)).isFalse()
            }
        }

        on("calling updateOrCreate with that entry") {
            persistenceFacade.updateOrCreate(entry)

            it("must not fire a WatchListChangedEvent or any other list change event") {
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isZero()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
            }

            it("must not increase watchlist") {
                assertThat(persistenceFacade.fetchWatchList()).hasSize(0)
            }

            it("must not exist on the watchlist") {
                assertThat(persistenceFacade.watchListEntryExists(entry.infoLink)).isFalse()
            }
        }

        on("removing it from list") {
            val result = persistenceFacade.removeFromWatchList(entry)

            it("must always return false, because invalid entries can't be removed, because by definition they can't be added anyway") {
                assertThat(result).isFalse()
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
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isZero()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
            }

            it("must not increase watchlist") {
                assertThat(persistenceFacade.fetchWatchList()).hasSize(0)
            }

            it("must not exist on the watchlist") {
                assertThat(persistenceFacade.watchListEntryExists(entry.infoLink)).isFalse()
            }
        }

        on("calling updateOrCreate with that entry") {
            persistenceFacade.updateOrCreate(entry)

            it("must not fire a WatchListChangedEvent or any other list change event") {
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isZero()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
            }

            it("must not increase watchlist") {
                assertThat(persistenceFacade.fetchWatchList()).hasSize(0)
            }

            it("must not exist on the watchlist") {
                assertThat(persistenceFacade.watchListEntryExists(entry.infoLink)).isFalse()
            }
        }

        on("removing it from list") {
            val result = persistenceFacade.removeFromWatchList(entry)

            it("must always return false, because invalid entries can't be removed, because by definition they can't be added anyway") {
                assertThat(result).isFalse()
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
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isZero()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isOne()
            }

            it("must increase watchlist") {
                assertThat(persistenceFacade.fetchWatchList()).hasSize(1)
            }

            it("must exist on the watchlist") {
                assertThat(persistenceFacade.watchListEntryExists(entry.infoLink)).isTrue()
            }
        }

        on("calling updateOrCreate with that entry") {
            persistenceFacade.updateOrCreate(entry)

            it("must fire a WatchListChangedEvent") {
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isZero()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isOne()
            }

            it("must increase watchlist") {
                assertThat(persistenceFacade.fetchWatchList()).hasSize(1)
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
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isZero()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isOne()
            }

            it("must increase watchlist") {
                assertThat(persistenceFacade.fetchWatchList()).hasSize(1)
            }

            it("must exist on the watchlist") {
                assertThat(persistenceFacade.watchListEntryExists(entry.infoLink)).isTrue()
            }
        }

        on("calling updateOrCreate with that entry") {
            persistenceFacade.updateOrCreate(entry)

            it("must fire a WatchListChangedEvent") {
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isZero()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isOne()
            }

            it("must increase watchlist") {
                assertThat(persistenceFacade.fetchWatchList()).hasSize(1)
            }

            it("must exist on the watchlist") {
                assertThat(persistenceFacade.watchListEntryExists(entry.infoLink)).isTrue()
            }
        }
    }


    given("one filter entry in the watchlist") {
        val anime = WatchListEntry(
                "Death Note",
                InfoLink("http://myanimelist.net/anime/1535")
        )

        beforeEachTest {
            persistenceFacade.watchAnime(anime)

            //reset events counter or else we get 2 instead of 1
            eventBusListener.receivedAnimeListChangedEvent = 0
            eventBusListener.receivedFilterListChangedEvent = 0
            eventBusListener.receivedWatchListChangedEvent = 0
        }

        on("removing the watchlist entry") {
            val result = persistenceFacade.removeFromWatchList(anime)

            it("must return true, because the entry has been removed") {
                assertThat(result).isTrue()
            }

            it("must result in an empty watchlist") {
                assertThat(persistenceFacade.fetchWatchList()).isEmpty()
            }

            it("must fire a WatchListChangedEvent") {
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isZero()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isOne()
            }
        }

        on("changing it's title using a new object instance") {
            val newTitle = "My new Title"

            persistenceFacade.updateOrCreate(WatchListEntry(
                    newTitle,
                    anime.infoLink
            ))

            it("must fire a WatchListChangedEvent, but none of the other list change events") {
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isZero()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isOne()
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
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isZero()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isOne()
            }

            it("must contain the changed value") {
                assertThat(persistenceFacade.fetchWatchList()[0].thumbnail).isEqualTo(newThumbnail)
            }
        }
    }


    given("an anime without a title and an empty list") {
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
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isZero()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
            }

            it("must not increase animelist") {
                assertThat(persistenceFacade.fetchAnimeList()).hasSize(0)
            }

            it("must not exist in the list") {
                assertThat(persistenceFacade.animeEntryExists(entry.infoLink))
            }
        }

        on("calling updateOrCreate with that entry") {
            persistenceFacade.updateOrCreate(entry)

            it("must not fire an AnimeListChangedEvent or any other list change event.") {
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isZero()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
            }

            it("must not increase animelist") {
                assertThat(persistenceFacade.fetchAnimeList()).hasSize(0)
            }

            it("must not exist in the list") {
                assertThat(persistenceFacade.animeEntryExists(entry.infoLink))
            }
        }
    }


    given("an anime without a valid InfoLink and an empty list") {
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
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isOne()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
            }

            it("must increase animelist") {
                assertThat(persistenceFacade.fetchAnimeList()).hasSize(1)
            }

            //TODO: okay so what happens if we check the animeExists function. Especially with multiple entries having no infolink
        }

        on("calling updateOrCreate with that entry") {
            persistenceFacade.updateOrCreate(entry)

            it("must fire an AnimeListChangedEvent or any other list change event") {
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isOne()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
            }

            it("must increase animelist") {
                assertThat(persistenceFacade.fetchAnimeList()).hasSize(1)
            }

            //TODO: okay so what happens if we check the animeExists function. Especially with multiple entries having no infolink
        }
    }


    given("a valid minimal anime and an empty list") {
        val entry = Anime(
                "Death Note",
                InfoLink("http://myanimelist.net/anime/1535")
        )

        on("adding that entry") {
            val result = persistenceFacade.addAnime(entry)

            it("must return true, because the entry has been added to the animelist") {
                assertThat(result).isTrue()
            }

            it("must fire an animeListChangedEvent, but no other list change event") {
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isOne()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
            }

            it("must increase animelist") {
                assertThat(persistenceFacade.fetchAnimeList()).hasSize(1)
            }

            it("must exist on the list") {
                assertThat(persistenceFacade.animeEntryExists(entry.infoLink)).isTrue()
            }
        }

        on("calling updateOrCreate with that entry") {
            persistenceFacade.updateOrCreate(entry)

            it("must fire an AnimeListChangedEvent, but no other list change event") {
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isOne()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
            }

            it("must increase animelist") {
                assertThat(persistenceFacade.fetchAnimeList()).hasSize(1)
            }

            it("must exist on the list") {
                assertThat(persistenceFacade.animeEntryExists(entry.infoLink)).isTrue()
            }
        }

        on("removing anime") {
            val result = persistenceFacade.removeAnime(entry)

            it("must return false, because there the entry does not exist in the list and therefore couldn't be removed") {
                assertThat(result).isFalse()
            }
        }
    }


    given("a valid anime and an empty list") {
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
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isOne()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
            }

            it("must increase animelist") {
                assertThat(persistenceFacade.fetchAnimeList()).hasSize(1)
            }

            it("must exist on the list") {
                assertThat(persistenceFacade.animeEntryExists(entry.infoLink)).isTrue()
            }
        }

        on("calling updateOrCreate with that entry") {
            persistenceFacade.updateOrCreate(entry)

            it("must fire an AnimeListChangedEvent, but no other list change event") {
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isOne()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
            }

            it("must increase animelist") {
                assertThat(persistenceFacade.fetchAnimeList()).hasSize(1)
            }

            it("must exist on the list") {
                assertThat(persistenceFacade.animeEntryExists(entry.infoLink)).isTrue()
            }
        }
    }


    given("one anime entry in the list") {
        val anime = Anime(
                "Death Note",
                InfoLink("http://myanimelist.net/anime/1535")
        )

        beforeEachTest {
            persistenceFacade.addAnime(anime)

            //reset events counter or else we get 2 instead of 1
            eventBusListener.receivedAnimeListChangedEvent = 0
            eventBusListener.receivedFilterListChangedEvent = 0
            eventBusListener.receivedWatchListChangedEvent = 0
        }

        on("removing the entry") {
            val result = persistenceFacade.removeAnime(anime)

            it("must return true, because the entry has been removed") {
                assertThat(result).isTrue()
            }

            it("must result in an empty list") {
                assertThat(persistenceFacade.fetchAnimeList()).isEmpty()
            }

            it("must fire a WatchListChangedEvent") {
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isOne()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
            }

            it("must not exist on the list, because it has been removed") {
                assertThat(persistenceFacade.animeEntryExists(anime.infoLink)).isFalse()
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
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isOne()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
            }

            it("must not change the new value, because unlike Watch- and FilterListEntries Animes own a specific UUID. Their InfoLinks can change.") {
                assertThat(persistenceFacade.fetchAnimeList()[0].title).isNotEqualTo(newTitle)
            }

            it("must result in an additional entry") {
                assertThat(persistenceFacade.fetchAnimeList()).hasSize(2)
            }
        }

        on("changing it's title using the instance from fetching the list") {
            val newTitle = "My new Title"
            val entry = persistenceFacade.fetchAnimeList()[0].apply { title = newTitle }
            persistenceFacade.updateOrCreate(entry)

            it("must fire an AnimeListChangedEvent, but none of the other list change events") {
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isOne()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
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
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isOne()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
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
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isOne()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
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
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isOne()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
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
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isOne()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
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
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isOne()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
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
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isOne()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
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

            //reset events counter or else we get 2 instead of 1
            eventBusListener.receivedAnimeListChangedEvent = 0
            eventBusListener.receivedFilterListChangedEvent = 0
            eventBusListener.receivedWatchListChangedEvent = 0
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
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isOne()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isOne()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isOne()
            }
        }

        on("adding the title from animelist to filterlist") {
            val anime: Anime = persistenceFacade.fetchAnimeList()[0]
            val result = persistenceFacade.filterAnime(anime)

            it("must return false, because you cannot filter an anime that already resides in the animelist. To protect the user against mistakes a title has to be manually removed from the animelist first.") {
                assertThat(result).isFalse()
            }

            it("must result in animelist still having one entry") {
                assertThat(persistenceFacade.fetchAnimeList()).hasSize(1)
            }

            it("must result in filterlist still having one entry") {
                assertThat(persistenceFacade.fetchFilterList()).hasSize(1)
            }

            it("must result in watchlist still having one entry") {
                assertThat(persistenceFacade.fetchWatchList()).hasSize(1)
            }

            it("must fire an event for both animelist and filterlist") {
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isZero()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
            }
        }

        on("adding the title from animelist to watchlist") {
            val anime: Anime = persistenceFacade.fetchAnimeList()[0]
            val result = persistenceFacade.watchAnime(anime)

            it("must return false, because you cannot put an anime to the watchlist if it already resides in the animelist. To protect the user against mistakes a title has to be manually removed from the animelist first.") {
                assertThat(result).isFalse()
            }

            it("must result in animelist still having one entry") {
                assertThat(persistenceFacade.fetchAnimeList()).hasSize(1)
            }

            it("must result in filterlist still having one entry") {
                assertThat(persistenceFacade.fetchFilterList()).hasSize(1)
            }

            it("must result in watchlist still having one entry") {
                assertThat(persistenceFacade.fetchWatchList()).hasSize(1)
            }

            it("must fire an event for both animelist and filterlist") {
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isZero()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
            }
        }

        on("adding the title from filterlist to animelist") {
            val filterListEntry: FilterListEntry = persistenceFacade.fetchFilterList()[0]
            val result = persistenceFacade.addAnime(Anime(filterListEntry.title, filterListEntry.infoLink))

            it("must return true, because the title has been added to animelist") {
                assertThat(result).isTrue()
            }

            it("must result in animelist having two entries") {
                assertThat(persistenceFacade.fetchAnimeList()).hasSize(2)
            }

            it("must result in filterlist having no more entries") {
                assertThat(persistenceFacade.fetchFilterList()).isEmpty()
            }

            it("must result in watchlist still having one entry") {
                assertThat(persistenceFacade.fetchWatchList()).hasSize(1)
            }

            it("must fire an event for both animelist and filterlist") {
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isOne()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isOne()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
            }
        }

        on("adding the title from filterlist to watchlist") {
            val filterListEntry: FilterListEntry = persistenceFacade.fetchFilterList()[0]
            val result = persistenceFacade.watchAnime(filterListEntry)

            it("must return true, because the title has been added to watchlist") {
                assertThat(result).isTrue()
            }

            it("must result in animelist still having one entry") {
                assertThat(persistenceFacade.fetchAnimeList()).hasSize(1)
            }

            it("must result in filterlist having no more entries") {
                assertThat(persistenceFacade.fetchFilterList()).isEmpty()
            }

            it("must result in watchlist having two entries") {
                assertThat(persistenceFacade.fetchWatchList()).hasSize(2)
            }

            it("must fire an event for both animelist and filterlist") {
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isZero()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isOne()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isOne()
            }
        }

        on("adding the title from watchlist to animelist") {
            val watchListEntry: WatchListEntry = persistenceFacade.fetchWatchList()[0]
            val result = persistenceFacade.addAnime(Anime(watchListEntry.title, watchListEntry.infoLink))

            it("must return true, because the title has been added to animelist") {
                assertThat(result).isTrue()
            }

            it("must result in animelist having two entries") {
                assertThat(persistenceFacade.fetchAnimeList()).hasSize(2)
            }

            it("must result in filterlist still one entry") {
                assertThat(persistenceFacade.fetchFilterList()).hasSize(1)
            }

            it("must result in watchlist having no more entries") {
                assertThat(persistenceFacade.fetchWatchList()).isEmpty()
            }

            it("must fire an event for both animelist and watchlist") {
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isOne()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isOne()
            }
        }

        on("adding the title from watchlist to filterlist") {
            val watchListEntry: WatchListEntry = persistenceFacade.fetchWatchList()[0]
            val result = persistenceFacade.filterAnime(watchListEntry)

            it("must return true, because the title has been added to filterlist") {
                assertThat(result).isTrue()
            }

            it("must result in animelist still having one entry") {
                assertThat(persistenceFacade.fetchAnimeList()).hasSize(1)
            }

            it("must result in filterlist having two entries") {
                assertThat(persistenceFacade.fetchFilterList()).hasSize(2)
            }

            it("must result in watchlist having no more entries") {
                assertThat(persistenceFacade.fetchWatchList()).isEmpty()
            }

            it("must fire an event for both animelist and watchlist") {
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isZero()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isOne()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isOne()
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
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isZero()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isOne()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
            }

            it("must contain the exact same amount of entries that has been inserted") {
                assertThat(persistenceFacade.fetchFilterList()).hasSize(list.size)
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
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isZero()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isOne()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
            }

            it("must contain only the one valid entry") {
                assertThat(persistenceFacade.fetchFilterList()).hasSize(1)
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
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isZero()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isOne()
            }

            it("must contain the exact same amount of entries that has been inserted") {
                assertThat(persistenceFacade.fetchWatchList()).hasSize(list.size)
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
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isZero()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isOne()
            }

            it("must contain only the one valid entry") {
                assertThat(persistenceFacade.fetchWatchList()).hasSize(1)
            }
        }
    }


    given("a list of valid anime and an empty list") {
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
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isOne()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
            }

            it("must contain the exact same amount of entries that has been inserted") {
                assertThat(persistenceFacade.fetchAnimeList()).hasSize(list.size)
            }
        }
    }


    given("a list of one valid and two invalid anime and an empty animelist") {
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
                assertThat(eventBusListener.receivedAnimeListChangedEvent).isOne()
                assertThat(eventBusListener.receivedFilterListChangedEvent).isZero()
                assertThat(eventBusListener.receivedWatchListChangedEvent).isZero()
            }

            it("must contain only the one valid entry") {
                assertThat(persistenceFacade.fetchAnimeList()).hasSize(1)
            }
        }
    }
})