package io.github.manami.persistence;

import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.eventbus.EventBus;

import io.github.manami.dto.AnimeType;
import io.github.manami.dto.entities.AbstractMinimalEntry;
import io.github.manami.dto.entities.Anime;
import io.github.manami.dto.entities.FilterEntry;
import io.github.manami.dto.entities.WatchListEntry;
import io.github.manami.dto.events.AnimeListChangedEvent;
import io.github.manami.persistence.inmemory.InMemoryPersistenceHandler;
import io.github.manami.persistence.inmemory.animelist.InMemoryAnimeListHandler;
import io.github.manami.persistence.inmemory.filterlist.InMemoryFilterListHandler;
import io.github.manami.persistence.inmemory.watchlist.InMemoryWatchListHandler;

public class PersistenceFacadeTest {

    private PersistenceFacade persistenceFacade;
    private EventBus eventBusMock;
    private InMemoryPersistenceHandler inMemoryPersistenceHandler;


    @BeforeMethod
    public void setUp() throws IOException {
        inMemoryPersistenceHandler = new InMemoryPersistenceHandler(new InMemoryAnimeListHandler(), new InMemoryFilterListHandler(), new InMemoryWatchListHandler());
        eventBusMock = mock(EventBus.class);
        persistenceFacade = new PersistenceFacade(inMemoryPersistenceHandler, eventBusMock);
    }


    @Test(groups = "unitTest")
    public void testFilterAnimeIsNull() {
        // given

        // when
        final boolean result = persistenceFacade.filterAnime(null);

        // then
        verify(eventBusMock, times(0)).post(any(AnimeListChangedEvent.class));
        assertEquals(result, false);
        assertEquals(persistenceFacade.fetchFilterList().size(), 0);
    }


    @Test(groups = "unitTest")
    public void testFilterAnimeIsEntryWithoutTitle() {
        // given
        final FilterEntry entry = new FilterEntry("", "http://myanimelist.net/anime/1535");

        // when
        final boolean result = persistenceFacade.filterAnime(entry);

        // then
        verify(eventBusMock, times(0)).post(any(AnimeListChangedEvent.class));
        assertEquals(result, false);
        assertEquals(persistenceFacade.fetchFilterList().size(), 0);
    }


    @Test(groups = "unitTest")
    public void testFilterAnimeIsEntryWithoutInfoLink() {
        // given
        final FilterEntry entry = new FilterEntry("Death Note", "");

        // when
        final boolean result = persistenceFacade.filterAnime(entry);

        // then
        verify(eventBusMock, times(0)).post(any(AnimeListChangedEvent.class));
        assertEquals(result, false);
        assertEquals(persistenceFacade.fetchFilterList().size(), 0);
    }


    @Test(groups = "unitTest")
    public void testFilterAnimeIsEntryWithoutThumbnail() {
        // given
        final FilterEntry entry = new FilterEntry("Death Note", "http://myanimelist.net/anime/1535");

        // when
        final boolean result = persistenceFacade.filterAnime(entry);

        // then
        verify(eventBusMock, times(1)).post(any(AnimeListChangedEvent.class));
        assertEquals(result, true);
        assertEquals(persistenceFacade.fetchFilterList().size(), 1);
    }


    @Test(groups = "unitTest")
    public void testFilterAnimeIsFullEntry() {
        // given
        final FilterEntry entry = new FilterEntry("Death Note", "http://cdn.myanimelist.net/images/anime/9/9453t.jpg", "http://myanimelist.net/anime/1535");

        // when
        final boolean result = persistenceFacade.filterAnime(entry);

        // then
        verify(eventBusMock, times(1)).post(any(AnimeListChangedEvent.class));
        assertEquals(result, true);
        assertEquals(persistenceFacade.fetchFilterList().size(), 1);
    }


    @Test(groups = "unitTest")
    public void testFilterEntryExists() {
        // given
        final String infoLink = "http://myanimelist.net/anime/1535";
        final FilterEntry entry = new FilterEntry("Death Note", infoLink);
        persistenceFacade.filterAnime(entry);

        // when
        final boolean result = persistenceFacade.filterEntryExists(infoLink);

        // then
        assertEquals(result, true);
    }


    @Test(groups = "unitTest")
    public void testFilterEntryNotExists() {
        // given

        // when
        final boolean result = persistenceFacade.filterEntryExists("http://myanimelist.net/anime/1535");

        // then
        verify(eventBusMock, times(0)).post(any(AnimeListChangedEvent.class));
        assertEquals(result, false);
    }


    @Test(groups = "unitTest")
    public void testFilterAnimeList() {
        // given
        final FilterEntry entry = new FilterEntry("Death Note", "http://myanimelist.net/anime/1535");
        persistenceFacade.filterAnime(entry);

        // when
        final List<FilterEntry> fetchFilterList = persistenceFacade.fetchFilterList();

        // then
        assertEquals(fetchFilterList.size(), 1);
    }


    @Test(groups = "unitTest")
    public void testFetchWatchList() {
        // given
        final WatchListEntry entry = new WatchListEntry("Death Note", "http://myanimelist.net/anime/1535");
        persistenceFacade.watchAnime(entry);

        // when
        final List<WatchListEntry> fetchWatchList = persistenceFacade.fetchWatchList();

        // then
        assertEquals(fetchWatchList.size(), 1);
    }


    @Test(groups = "unitTest")
    public void testRemoveFromFilterListWorks() {
        // given
        final String infoLink = "http://myanimelist.net/anime/1535";
        final FilterEntry entry = new FilterEntry("Death Note", infoLink);
        persistenceFacade.filterAnime(entry);

        // when
        final boolean result = persistenceFacade.removeFromFilterList(infoLink);

        // then
        verify(eventBusMock, times(2)).post(any(AnimeListChangedEvent.class));
        assertEquals(result, true);
        assertEquals(persistenceFacade.fetchFilterList().isEmpty(), true);
    }


    @Test(groups = "unitTest")
    public void testRemoveFromFilterListNullAsArgument() {
        // given

        // when
        final boolean result = persistenceFacade.removeFromFilterList(null);

        // then
        verify(eventBusMock, times(0)).post(any(AnimeListChangedEvent.class));
        assertEquals(result, false);
    }


    @Test(groups = "unitTest")
    public void testWatchListEntryExists() {
        // given
        final String infoLink = "http://myanimelist.net/anime/1535";
        final WatchListEntry entry = new WatchListEntry("Death Note", infoLink);
        persistenceFacade.watchAnime(entry);

        // when
        final boolean result = persistenceFacade.watchListEntryExists(infoLink);

        // then
        assertEquals(result, true);
    }


    @Test(groups = "unitTest")
    public void testWatchListEntryNotExists() {
        // given

        // when
        final boolean result = persistenceFacade.watchListEntryExists("http://myanimelist.net/anime/1535");

        // then
        verify(eventBusMock, times(0)).post(any(AnimeListChangedEvent.class));
        assertEquals(result, false);
    }


    @Test(groups = "unitTest")
    public void testWatchAnimeIsNull() {
        // given

        // when
        final boolean result = persistenceFacade.watchAnime(null);

        // then
        verify(eventBusMock, times(0)).post(any(AnimeListChangedEvent.class));
        assertEquals(result, false);
        assertEquals(persistenceFacade.fetchWatchList().size(), 0);
    }


    @Test(groups = "unitTest")
    public void testWatchAnimeIsEntryWithoutTitle() {
        // given
        final WatchListEntry entry = new WatchListEntry("", "http://myanimelist.net/anime/1535");

        // when
        final boolean result = persistenceFacade.watchAnime(entry);

        // then
        verify(eventBusMock, times(0)).post(any(AnimeListChangedEvent.class));
        assertEquals(result, false);
        assertEquals(persistenceFacade.fetchWatchList().size(), 0);
    }


    @Test(groups = "unitTest")
    public void testWatchAnimeIsEntryWithoutInfoLink() {
        // given
        final WatchListEntry entry = new WatchListEntry("Death Note", "");

        // when
        final boolean result = persistenceFacade.watchAnime(entry);

        // then
        verify(eventBusMock, times(0)).post(any(AnimeListChangedEvent.class));
        assertEquals(result, false);
        assertEquals(persistenceFacade.fetchWatchList().size(), 0);
    }


    @Test(groups = "unitTest")
    public void testWatchAnimeIsEntryWithoutThumbnail() {
        // given
        final WatchListEntry entry = new WatchListEntry("Death Note", "http://myanimelist.net/anime/1535");

        // when
        final boolean result = persistenceFacade.watchAnime(entry);

        // then
        verify(eventBusMock, times(1)).post(any(AnimeListChangedEvent.class));
        assertEquals(result, true);
        assertEquals(persistenceFacade.fetchWatchList().size(), 1);
    }


    @Test(groups = "unitTest")
    public void testWatchAnimeIsFullEntry() {
        // given
        final WatchListEntry entry = new WatchListEntry("Death Note", "http://cdn.myanimelist.net/images/anime/9/9453t.jpg", "http://myanimelist.net/anime/1535");

        // when
        final boolean result = persistenceFacade.watchAnime(entry);

        // then
        verify(eventBusMock, times(1)).post(any(AnimeListChangedEvent.class));
        assertEquals(result, true);
        assertEquals(persistenceFacade.fetchWatchList().size(), 1);
    }


    @Test(groups = "unitTest")
    public void testRemoveFromWatchListWorks() {
        // given
        final String infoLink = "http://myanimelist.net/anime/1535";
        final WatchListEntry entry = new WatchListEntry("Death Note", infoLink);
        persistenceFacade.watchAnime(entry);

        // when
        final boolean result = persistenceFacade.removeFromWatchList(infoLink);

        // then
        verify(eventBusMock, times(2)).post(any(AnimeListChangedEvent.class));
        assertEquals(result, true);
        assertEquals(persistenceFacade.fetchWatchList().isEmpty(), true);
    }


    @Test(groups = "unitTest")
    public void testRemoveFromWatchListNullAsArgument() {
        // given

        // when
        final boolean result = persistenceFacade.removeFromWatchList(null);

        // then
        verify(eventBusMock, times(0)).post(any(AnimeListChangedEvent.class));
        assertEquals(result, false);
    }


    @Test(groups = "unitTest")
    public void testaddAnimeIsNull() {
        // given

        // when
        final boolean result = persistenceFacade.addAnime(null);

        // then
        verify(eventBusMock, times(0)).post(any(AnimeListChangedEvent.class));
        assertEquals(result, false);
        assertEquals(persistenceFacade.fetchAnimeList().size(), 0);
    }


    @Test(groups = "unitTest")
    public void testaddAnimeIsFullEntry() {
        // given
        final Anime entry = new Anime();
        entry.setEpisodes(37);
        entry.setInfoLink("http://myanimelist.net/anime/1535");
        entry.setLocation("/death_note");
        entry.setPicture("http://cdn.myanimelist.net/images/anime/9/9453.jpg");
        entry.setThumbnail("http://cdn.myanimelist.net/images/anime/9/9453t.jpg");
        entry.setTitle("Death Note");
        entry.setType(AnimeType.TV);

        // when
        final boolean result = persistenceFacade.addAnime(entry);

        // then
        verify(eventBusMock, times(1)).post(any(AnimeListChangedEvent.class));
        assertEquals(result, true);
        assertEquals(persistenceFacade.fetchAnimeList().size(), 1);
    }


    @Test(groups = "unitTest")
    public void testaddAnimeIsEntryWithoutEpisodes() {
        // given
        final Anime entry = new Anime();
        entry.setInfoLink("http://myanimelist.net/anime/1535");
        entry.setLocation("/death_note");
        entry.setPicture("http://cdn.myanimelist.net/images/anime/9/9453.jpg");
        entry.setThumbnail("http://cdn.myanimelist.net/images/anime/9/9453t.jpg");
        entry.setTitle("Death Note");
        entry.setType(AnimeType.TV);

        // when
        final boolean result = persistenceFacade.addAnime(entry);

        // then
        verify(eventBusMock, times(1)).post(any(AnimeListChangedEvent.class));
        assertEquals(result, true);
        assertEquals(persistenceFacade.fetchAnimeList().size(), 1);
    }


    @Test(groups = "unitTest")
    public void testaddAnimeIsEntryWithoutInfoLink() {
        // given
        final Anime entry = new Anime();
        entry.setEpisodes(37);
        entry.setLocation("/death_note");
        entry.setPicture("http://cdn.myanimelist.net/images/anime/9/9453.jpg");
        entry.setThumbnail("http://cdn.myanimelist.net/images/anime/9/9453t.jpg");
        entry.setTitle("Death Note");
        entry.setType(AnimeType.TV);

        // when
        final boolean result = persistenceFacade.addAnime(entry);

        // then
        verify(eventBusMock, times(1)).post(any(AnimeListChangedEvent.class));
        assertEquals(result, true);
        assertEquals(persistenceFacade.fetchAnimeList().size(), 1);
    }


    @Test(groups = "unitTest")
    public void testaddAnimeIsEntryWithoutLocation() {
        // given
        final Anime entry = new Anime();
        entry.setEpisodes(37);
        entry.setInfoLink("http://myanimelist.net/anime/1535");
        entry.setPicture("http://cdn.myanimelist.net/images/anime/9/9453.jpg");
        entry.setThumbnail("http://cdn.myanimelist.net/images/anime/9/9453t.jpg");
        entry.setTitle("Death Note");
        entry.setType(AnimeType.TV);

        // when
        final boolean result = persistenceFacade.addAnime(entry);

        // then
        verify(eventBusMock, times(0)).post(any(AnimeListChangedEvent.class));
        assertEquals(result, false);
        assertEquals(persistenceFacade.fetchAnimeList().size(), 0);
    }


    @Test(groups = "unitTest")
    public void testaddAnimeIsEntryWithoutPicture() {
        // given
        final Anime entry = new Anime();
        entry.setEpisodes(37);
        entry.setInfoLink("http://myanimelist.net/anime/1535");
        entry.setLocation("/death_note");
        entry.setThumbnail("http://cdn.myanimelist.net/images/anime/9/9453t.jpg");
        entry.setTitle("Death Note");
        entry.setType(AnimeType.TV);

        // when
        final boolean result = persistenceFacade.addAnime(entry);

        // then
        verify(eventBusMock, times(1)).post(any(AnimeListChangedEvent.class));
        assertEquals(result, true);
        assertEquals(persistenceFacade.fetchAnimeList().size(), 1);
    }


    @Test(groups = "unitTest")
    public void testaddAnimeIsEntryWithoutThumbnail() {
        // given
        final Anime entry = new Anime();
        entry.setEpisodes(37);
        entry.setInfoLink("http://myanimelist.net/anime/1535");
        entry.setLocation("/death_note");
        entry.setPicture("http://cdn.myanimelist.net/images/anime/9/9453.jpg");
        entry.setTitle("Death Note");
        entry.setType(AnimeType.TV);

        // when
        final boolean result = persistenceFacade.addAnime(entry);

        // then
        verify(eventBusMock, times(1)).post(any(AnimeListChangedEvent.class));
        assertEquals(result, true);
        assertEquals(persistenceFacade.fetchAnimeList().size(), 1);
    }


    @Test(groups = "unitTest")
    public void testaddAnimeIsEntryWithoutTitle() {
        // given
        final Anime entry = new Anime();
        entry.setEpisodes(37);
        entry.setInfoLink("http://myanimelist.net/anime/1535");
        entry.setLocation("/death_note");
        entry.setPicture("http://cdn.myanimelist.net/images/anime/9/9453.jpg");
        entry.setThumbnail("http://cdn.myanimelist.net/images/anime/9/9453t.jpg");
        entry.setType(AnimeType.TV);

        // when
        final boolean result = persistenceFacade.addAnime(entry);

        // then
        verify(eventBusMock, times(0)).post(any(AnimeListChangedEvent.class));
        assertEquals(result, false);
        assertEquals(persistenceFacade.fetchAnimeList().size(), 0);
    }


    @Test(groups = "unitTest")
    public void testaddAnimeIsEntryWithoutType() {
        // given
        final Anime entry = new Anime();
        entry.setEpisodes(37);
        entry.setInfoLink("http://myanimelist.net/anime/1535");
        entry.setLocation("/death_note");
        entry.setPicture("http://cdn.myanimelist.net/images/anime/9/9453.jpg");
        entry.setThumbnail("http://cdn.myanimelist.net/images/anime/9/9453t.jpg");
        entry.setTitle("Death Note");

        // when
        final boolean result = persistenceFacade.addAnime(entry);

        // then
        verify(eventBusMock, times(0)).post(any(AnimeListChangedEvent.class));
        assertEquals(result, false);
        assertEquals(persistenceFacade.fetchAnimeList().size(), 0);
    }


    @Test(groups = "unitTest")
    public void testAnimeEntryExists() {
        // given
        final String infoLink = "http://myanimelist.net/anime/1535";
        final Anime entry = new Anime();
        entry.setEpisodes(37);
        entry.setInfoLink(infoLink);
        entry.setLocation("/death_note");
        entry.setPicture("http://cdn.myanimelist.net/images/anime/9/9453.jpg");
        entry.setThumbnail("http://cdn.myanimelist.net/images/anime/9/9453t.jpg");
        entry.setTitle("Death Note");
        entry.setType(AnimeType.TV);
        persistenceFacade.addAnime(entry);

        // when
        final boolean result = persistenceFacade.animeEntryExists(infoLink);

        // then
        assertEquals(result, true);
    }


    @Test(groups = "unitTest")
    public void testAnimeEntryNotExists() {
        // given

        // when
        final boolean result = persistenceFacade.animeEntryExists("http://myanimelist.net/anime/1535");

        // then
        verify(eventBusMock, times(0)).post(any(AnimeListChangedEvent.class));
        assertEquals(result, false);
    }


    @Test(groups = "unitTest")
    public void testAnimeList() {
        // given
        final Anime entry = new Anime();
        entry.setEpisodes(37);
        entry.setInfoLink("http://myanimelist.net/anime/1535");
        entry.setLocation("/death_note");
        entry.setPicture("http://cdn.myanimelist.net/images/anime/9/9453.jpg");
        entry.setThumbnail("http://cdn.myanimelist.net/images/anime/9/9453t.jpg");
        entry.setTitle("Death Note");
        entry.setType(AnimeType.TV);
        persistenceFacade.addAnime(entry);

        // when
        final List<Anime> animeList = persistenceFacade.fetchAnimeList();

        // then
        assertEquals(animeList.size(), 1);
    }


    @Test(groups = "unitTest")
    public void testRemoveFromAnimeListWorks() {
        // given
        final Anime entry = new Anime();
        entry.setEpisodes(37);
        entry.setInfoLink("http://myanimelist.net/anime/1535");
        entry.setLocation("/death_note");
        entry.setPicture("http://cdn.myanimelist.net/images/anime/9/9453.jpg");
        entry.setThumbnail("http://cdn.myanimelist.net/images/anime/9/9453t.jpg");
        entry.setTitle("Death Note");
        entry.setType(AnimeType.TV);
        persistenceFacade.addAnime(entry);

        // when
        final boolean result = persistenceFacade.removeAnime(entry.getId());

        // then
        verify(eventBusMock, times(2)).post(any(AnimeListChangedEvent.class));
        assertEquals(result, true);
        assertEquals(persistenceFacade.fetchAnimeList().isEmpty(), true);
    }


    @Test(groups = "unitTest")
    public void testRemoveFromAnimeListNullAsArgument() {
        // given

        // when
        final boolean result = persistenceFacade.removeAnime(null);

        // then
        verify(eventBusMock, times(0)).post(any(AnimeListChangedEvent.class));
        assertEquals(result, false);
    }


    @Test(groups = "unitTest")
    public void testThatClearAllWorks() {
        // given
        final Anime entry = new Anime();
        entry.setEpisodes(37);
        entry.setInfoLink("http://myanimelist.net/anime/1535");
        entry.setLocation("/death_note");
        entry.setPicture("http://cdn.myanimelist.net/images/anime/9/9453.jpg");
        entry.setThumbnail("http://cdn.myanimelist.net/images/anime/9/9453t.jpg");
        entry.setTitle("Death Note");
        entry.setType(AnimeType.TV);
        persistenceFacade.addAnime(entry);

        final FilterEntry filterEntry = new FilterEntry("Gintama", "http://cdn.myanimelist.net/images/anime/3/72078t.jpg", "http://myanimelist.net/anime/28977");
        persistenceFacade.filterAnime(filterEntry);

        final WatchListEntry watchEntry = new WatchListEntry("Steins;Gate", "http://cdn.myanimelist.net/images/anime/5/73199t.jpg", "http://myanimelist.net/anime/9253");
        persistenceFacade.watchAnime(watchEntry);

        // when
        persistenceFacade.clearAll();

        // then
        verify(eventBusMock, times(4)).post(any(AnimeListChangedEvent.class));
        assertEquals(persistenceFacade.fetchAnimeList().isEmpty(), true);
        assertEquals(persistenceFacade.fetchWatchList().isEmpty(), true);
        assertEquals(persistenceFacade.fetchFilterList().isEmpty(), true);
    }


    @Test(groups = "unitTest")
    public void testAddFilterListWithNull() {
        // given

        // when
        persistenceFacade.addFilterList(null);

        // then
        verify(eventBusMock, times(0)).post(any(AnimeListChangedEvent.class));
        assertNotNull(persistenceFacade.fetchFilterList());
        assertEquals(persistenceFacade.fetchFilterList().isEmpty(), true);
    }


    @Test(groups = "unitTest")
    public void testThatAddFilterListWorks() {
        // given
        final List<FilterEntry> list = newArrayList();

        final FilterEntry entry = new FilterEntry("Death Note", "http://cdn.myanimelist.net/images/anime/9/9453t.jpg", "http://myanimelist.net/anime/1535");
        list.add(entry);

        final FilterEntry gintama = new FilterEntry("Gintama", "http://cdn.myanimelist.net/images/anime/3/72078t.jpg", "http://myanimelist.net/anime/28977");
        list.add(gintama);

        final FilterEntry steinsGate = new FilterEntry("Steins;Gate", "http://cdn.myanimelist.net/images/anime/5/73199t.jpg", "http://myanimelist.net/anime/9253");
        list.add(steinsGate);

        // when
        persistenceFacade.addFilterList(list);

        // then
        verify(eventBusMock, times(1)).post(any(AnimeListChangedEvent.class));
        assertEquals(persistenceFacade.fetchFilterList().size(), list.size());
    }


    @Test(groups = "unitTest")
    public void testAddWatchListWithNull() {
        // given

        // when
        persistenceFacade.addWatchList(null);

        // then
        verify(eventBusMock, times(0)).post(any(AnimeListChangedEvent.class));
        assertNotNull(persistenceFacade.fetchWatchList());
        assertEquals(persistenceFacade.fetchWatchList().isEmpty(), true);
    }


    @Test(groups = "unitTest")
    public void testThatAddWatchListWorks() {
        // given
        final List<WatchListEntry> list = newArrayList();

        final WatchListEntry entry = new WatchListEntry("Death Note", "http://cdn.myanimelist.net/images/anime/9/9453t.jpg", "http://myanimelist.net/anime/1535");
        list.add(entry);

        final WatchListEntry gintama = new WatchListEntry("Gintama", "http://cdn.myanimelist.net/images/anime/3/72078t.jpg", "http://myanimelist.net/anime/28977");
        list.add(gintama);

        final WatchListEntry steinsGate = new WatchListEntry("Steins;Gate", "http://cdn.myanimelist.net/images/anime/5/73199t.jpg", "http://myanimelist.net/anime/9253");
        list.add(steinsGate);

        // when
        persistenceFacade.addWatchList(list);

        // then
        verify(eventBusMock, times(1)).post(any(AnimeListChangedEvent.class));
        assertEquals(persistenceFacade.fetchWatchList().size(), list.size());
    }


    @Test(groups = "unitTest")
    public void testThatAddAnimeListWorks() {
        // given
        final List<Anime> list = newArrayList();

        final Anime entry = new Anime();
        entry.setEpisodes(37);
        entry.setInfoLink("http://myanimelist.net/anime/1535");
        entry.setLocation("/death_note");
        entry.setPicture("http://cdn.myanimelist.net/images/anime/9/9453.jpg");
        entry.setThumbnail("http://cdn.myanimelist.net/images/anime/9/9453t.jpg");
        entry.setTitle("Death Note");
        entry.setType(AnimeType.TV);
        list.add(entry);

        final Anime steinsGate = new Anime();
        steinsGate.setEpisodes(24);
        steinsGate.setInfoLink("http://myanimelist.net/anime/9253");
        steinsGate.setLocation("/steins_gate");
        steinsGate.setPicture("http://cdn.myanimelist.net/images/anime/5/73199.jpg");
        steinsGate.setThumbnail("http://cdn.myanimelist.net/images/anime/5/73199t.jpg");
        steinsGate.setTitle("Steins;Gate");
        steinsGate.setType(AnimeType.TV);
        list.add(steinsGate);

        // when
        persistenceFacade.addAnimeList(list);

        // then
        verify(eventBusMock, times(1)).post(any(AnimeListChangedEvent.class));
        assertEquals(persistenceFacade.fetchAnimeList().size(), list.size());
    }


    @Test(groups = "unitTest")
    public void testAddAnimeListWithNull() {
        // given

        // when
        persistenceFacade.addAnimeList(null);

        // then
        verify(eventBusMock, times(0)).post(any(AnimeListChangedEvent.class));
        assertNotNull(persistenceFacade.fetchAnimeList());
        assertEquals(persistenceFacade.fetchAnimeList().isEmpty(), true);
    }


    @Test(groups = "unitTest")
    public void testUpdateOrCreateWithNull() {
        // given

        // when
        persistenceFacade.updateOrCreate(null);

        // then
        verify(eventBusMock, times(0)).post(any(AnimeListChangedEvent.class));
        assertEquals(persistenceFacade.fetchAnimeList().isEmpty(), true);
        assertEquals(persistenceFacade.fetchWatchList().isEmpty(), true);
        assertEquals(persistenceFacade.fetchFilterList().isEmpty(), true);
    }


    @Test(groups = "unitTest")
    public void testUpdateOrCreateForNewAnimeEntry() {
        // given
        final Anime entry = new Anime();
        entry.setEpisodes(37);
        entry.setInfoLink("http://myanimelist.net/anime/1535");
        entry.setLocation("/death_note");
        entry.setPicture("http://cdn.myanimelist.net/images/anime/9/9453.jpg");
        entry.setThumbnail("http://cdn.myanimelist.net/images/anime/9/9453t.jpg");
        entry.setTitle("Death Note");
        entry.setType(AnimeType.TV);

        // when
        persistenceFacade.updateOrCreate(entry);

        // then
        verify(eventBusMock, times(1)).post(any(AnimeListChangedEvent.class));
        assertEquals(persistenceFacade.fetchAnimeList().isEmpty(), false);
        assertEquals(persistenceFacade.fetchAnimeList().get(0), entry);
    }


    @Test(groups = "unitTest")
    public void testUpdateOrCreateForModifiedAnimeEntry() {
        // given
        final Anime entry = new Anime();
        entry.setEpisodes(35);
        entry.setInfoLink("http://myanimelist.net/anime/1535");
        entry.setLocation("/death_note");
        entry.setPicture("http://cdn.myanimelist.net/images/anime/9/9453.jpg");
        entry.setThumbnail("http://cdn.myanimelist.net/images/anime/9/9453t.jpg");
        entry.setTitle("Death Note");
        entry.setType(AnimeType.TV);

        persistenceFacade.addAnime(entry);

        final int episodes = 37;
        entry.setEpisodes(episodes);

        // when
        persistenceFacade.updateOrCreate(entry);

        // then
        verify(eventBusMock, times(2)).post(any(AnimeListChangedEvent.class));
        assertEquals(persistenceFacade.fetchAnimeList().isEmpty(), false);
        assertEquals(persistenceFacade.fetchAnimeList().get(0).getEpisodes(), episodes);
    }


    @Test(groups = "unitTest")
    public void testUpdateOrCreateForNewFilterEntry() {
        // given
        final FilterEntry entry = new FilterEntry("Death Note", "http://cdn.myanimelist.net/images/anime/9/9453t.jpg", "http://myanimelist.net/anime/1535");

        // when
        persistenceFacade.updateOrCreate(entry);

        // then
        verify(eventBusMock, times(1)).post(any(AnimeListChangedEvent.class));
        assertEquals(persistenceFacade.fetchFilterList().isEmpty(), false);
        assertEquals(persistenceFacade.fetchFilterList().get(0), entry);
    }


    @Test(groups = "unitTest")
    public void testUpdateOrCreateForModifiedFilterEntry() {
        // given
        final FilterEntry entry = new FilterEntry("Death Note", AbstractMinimalEntry.NO_IMG_THUMB, "http://myanimelist.net/anime/1535");

        persistenceFacade.filterAnime(entry);

        final String thumbnail = "http://cdn.myanimelist.net/images/anime/9/9453t.jpg";
        entry.setThumbnail(thumbnail);

        // when
        persistenceFacade.updateOrCreate(entry);

        // then
        verify(eventBusMock, times(2)).post(any(AnimeListChangedEvent.class));
        assertEquals(persistenceFacade.fetchFilterList().isEmpty(), false);
        assertEquals(persistenceFacade.fetchFilterList().get(0).getThumbnail(), thumbnail);
    }


    @Test(groups = "unitTest")
    public void testUpdateOrCreateForNewWatchListEntry() {
        // given
        final WatchListEntry entry = new WatchListEntry("Death Note", "http://cdn.myanimelist.net/images/anime/9/9453t.jpg", "http://myanimelist.net/anime/1535");

        // when
        persistenceFacade.updateOrCreate(entry);

        // then
        verify(eventBusMock, times(1)).post(any(AnimeListChangedEvent.class));
        assertEquals(persistenceFacade.fetchWatchList().isEmpty(), false);
        assertEquals(persistenceFacade.fetchWatchList().get(0), entry);
    }


    @Test(groups = "unitTest")
    public void testUpdateOrCreateForModifiedWatchListEntry() {
        // given
        final WatchListEntry entry = new WatchListEntry("Death Note", AbstractMinimalEntry.NO_IMG_THUMB, "http://myanimelist.net/anime/1535");

        persistenceFacade.watchAnime(entry);

        final String thumbnail = "http://cdn.myanimelist.net/images/anime/9/9453t.jpg";
        entry.setThumbnail(thumbnail);

        // when
        persistenceFacade.updateOrCreate(entry);

        // then
        verify(eventBusMock, times(2)).post(any(AnimeListChangedEvent.class));
        assertEquals(persistenceFacade.fetchWatchList().isEmpty(), false);
        assertEquals(persistenceFacade.fetchWatchList().get(0).getThumbnail(), thumbnail);
    }
}