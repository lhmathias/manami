package io.github.manami.persistence;

import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.google.common.eventbus.EventBus;
import io.github.manami.dto.AnimeType;
import io.github.manami.dto.entities.Anime;
import io.github.manami.dto.entities.FilterListEntry;
import io.github.manami.dto.entities.InfoLink;
import io.github.manami.dto.entities.MinimalEntry;
import io.github.manami.dto.entities.WatchListEntry;
import io.github.manami.persistence.events.AnimeListChangedEvent;
import io.github.manami.persistence.inmemory.InMemoryPersistenceHandler;
import io.github.manami.persistence.inmemory.animelist.InMemoryAnimeListHandler;
import io.github.manami.persistence.inmemory.filterlist.InMemoryFilterListHandler;
import io.github.manami.persistence.inmemory.watchlist.InMemoryWatchListHandler;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class PersistenceFacadeTest {
/*

  //@Test(groups = UNIT_TEST_GROUP)
  public void testThatClearAllWorks() throws MalformedURLException {
    // given
    final Anime entry = new Anime("Death Note", new InfoLink("http://myanimelist.net/anime/1535"));
    entry.setEpisodes(37);
    entry.setLocation("/death_note");
    entry.setPicture(new URL("http://cdn.myanimelist.net/images/anime/9/9453.jpg"));
    entry.setThumbnail(new URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg"));
    entry.setType(AnimeType.TV);
    persistenceFacade.addAnime(entry);

    final FilterListEntry filterListEntry = new FilterListEntry("Gintama",
        new InfoLink("http://myanimelist.net/anime/28977"),
        new URL("http://cdn.myanimelist.net/images/anime/3/72078t.jpg"));
    persistenceFacade.filterAnime(filterListEntry);

    final WatchListEntry watchEntry = new WatchListEntry("Steins;Gate",
        new InfoLink("http://myanimelist.net/anime/9253"),
        new URL("http://cdn.myanimelist.net/images/anime/5/73199t.jpg"));
    persistenceFacade.watchAnime(watchEntry);

    // when
    persistenceFacade.clearAll();

    // then
    verify(eventBusMock, times(4)).post(any(AnimeListChangedEvent.class));
    assertThat(persistenceFacade.fetchAnimeList().isEmpty()).isTrue();
    assertThat(persistenceFacade.fetchWatchList().isEmpty()).isTrue();
    assertThat(persistenceFacade.fetchFilterList().isEmpty()).isTrue();
  }


  //@Test(groups = UNIT_TEST_GROUP)
  public void testThatAddFilterListWorks() throws MalformedURLException {
    // given
    final List<FilterListEntry> list = newArrayList();

    final FilterListEntry entry = new FilterListEntry("Death Note",
        new InfoLink("http://myanimelist.net/anime/1535"),
        new URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg"));
    list.add(entry);

    final FilterListEntry gintama = new FilterListEntry("Gintama",
        new InfoLink("http://myanimelist.net/anime/28977"),
        new URL("http://cdn.myanimelist.net/images/anime/3/72078t.jpg"));
    list.add(gintama);

    final FilterListEntry steinsGate = new FilterListEntry("Steins;Gate",
        new InfoLink("http://myanimelist.net/anime/9253"),
        new URL("http://cdn.myanimelist.net/images/anime/5/73199t.jpg"));
    list.add(steinsGate);

    // when
    persistenceFacade.addFilterList(list);

    // then
    verify(eventBusMock, times(1)).post(any(AnimeListChangedEvent.class));
    assertThat(persistenceFacade.fetchFilterList().size()).isEqualTo(list.size());
  }


  //@Test(groups = UNIT_TEST_GROUP)
  public void testThatAddWatchListWorks() throws MalformedURLException {
    // given
    final List<WatchListEntry> list = newArrayList();

    final WatchListEntry entry = new WatchListEntry("Death Note",
        new InfoLink("http://myanimelist.net/anime/1535"),
        new URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg"));
    list.add(entry);

    final WatchListEntry gintama = new WatchListEntry("Gintama",
        new InfoLink("http://myanimelist.net/anime/28977"),
        new URL("http://cdn.myanimelist.net/images/anime/3/72078t.jpg"));
    list.add(gintama);

    final WatchListEntry steinsGate = new WatchListEntry("Steins;Gate",
        new InfoLink("http://myanimelist.net/anime/9253"),
        new URL("http://cdn.myanimelist.net/images/anime/5/73199t.jpg"));
    list.add(steinsGate);

    // when
    persistenceFacade.addWatchList(list);

    // then
    verify(eventBusMock, times(1)).post(any(AnimeListChangedEvent.class));
    assertThat(persistenceFacade.fetchWatchList().size()).isEqualTo(list.size());
  }


  //@Test(groups = UNIT_TEST_GROUP)
  public void testThatAddAnimeListWorks() throws MalformedURLException {
    // given
    final List<Anime> list = newArrayList();

    final Anime entry = new Anime("Death Note", new InfoLink("http://myanimelist.net/anime/1535"));
    entry.setEpisodes(37);
    entry.setLocation("/death_note");
    entry.setPicture(new URL("http://cdn.myanimelist.net/images/anime/9/9453.jpg"));
    entry.setThumbnail(new URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg"));
    entry.setType(AnimeType.TV);
    list.add(entry);

    final Anime steinsGate = new Anime("Steins;Gate",
        new InfoLink("http://myanimelist.net/anime/9253"));
    steinsGate.setEpisodes(24);
    steinsGate.setLocation("/steins_gate");
    steinsGate.setPicture(new URL("http://cdn.myanimelist.net/images/anime/5/73199.jpg"));
    steinsGate.setThumbnail(new URL("http://cdn.myanimelist.net/images/anime/5/73199t.jpg"));
    steinsGate.setType(AnimeType.TV);
    list.add(steinsGate);

    // when
    persistenceFacade.addAnimeList(list);

    // then
    verify(eventBusMock, times(1)).post(any(AnimeListChangedEvent.class));
    assertThat(persistenceFacade.fetchAnimeList().size()).isEqualTo(list.size());
  }


  //@Test(groups = UNIT_TEST_GROUP)
  public void testUpdateOrCreateForNewAnimeEntry() throws MalformedURLException {
    // given
    final Anime entry = new Anime("Death Note", new InfoLink("http://myanimelist.net/anime/1535"));
    entry.setEpisodes(37);
    entry.setLocation("/death_note");
    entry.setPicture(new URL("http://cdn.myanimelist.net/images/anime/9/9453.jpg"));
    entry.setThumbnail(new URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg"));
    entry.setType(AnimeType.TV);

    // when
    persistenceFacade.updateOrCreate(entry);

    // then
    verify(eventBusMock, times(1)).post(any(AnimeListChangedEvent.class));
    assertThat(persistenceFacade.fetchAnimeList().isEmpty()).isFalse();
    assertThat(persistenceFacade.fetchAnimeList().get(0)).isEqualTo(entry);
  }


  //@Test(groups = UNIT_TEST_GROUP)
  public void testUpdateOrCreateForModifiedAnimeEntry() throws MalformedURLException {
    // given
    final Anime entry = new Anime("Death Note", new InfoLink("http://myanimelist.net/anime/1535"));
    entry.setEpisodes(35);
    entry.setLocation("/death_note");
    entry.setPicture(new URL("http://cdn.myanimelist.net/images/anime/9/9453.jpg"));
    entry.setThumbnail(new URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg"));
    entry.setType(AnimeType.TV);

    persistenceFacade.addAnime(entry);

    final int episodes = 37;
    entry.setEpisodes(episodes);

    // when
    persistenceFacade.updateOrCreate(entry);

    // then
    verify(eventBusMock, times(2)).post(any(AnimeListChangedEvent.class));
    assertThat(persistenceFacade.fetchAnimeList().isEmpty()).isFalse();
    assertThat(persistenceFacade.fetchAnimeList().get(0).getEpisodes()).isEqualTo(episodes);
  }


  //@Test(groups = UNIT_TEST_GROUP)
  public void testUpdateOrCreateForNewFilterEntry() throws MalformedURLException {
    // given
    final FilterListEntry entry = new FilterListEntry("Death Note",
        new InfoLink("http://myanimelist.net/anime/1535"),
        new URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg"));

    // when
    persistenceFacade.updateOrCreate(entry);

    // then
    verify(eventBusMock, times(1)).post(any(AnimeListChangedEvent.class));
    assertThat(persistenceFacade.fetchFilterList().isEmpty()).isFalse();
    assertThat(persistenceFacade.fetchFilterList().get(0)).isEqualTo(entry);
  }


  //@Test(groups = UNIT_TEST_GROUP)
  public void testUpdateOrCreateForModifiedFilterEntry() throws MalformedURLException {
    // given
    final FilterListEntry entry = new FilterListEntry("Death Note",
        new InfoLink("http://myanimelist.net/anime/1535"),
        MinimalEntry.Companion.getNO_IMG_THUMB());

    persistenceFacade.filterAnime(entry);

    final URL thumbnail = new URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg");
    entry.setThumbnail(thumbnail);

    // when
    persistenceFacade.updateOrCreate(entry);

    // then
    verify(eventBusMock, times(2)).post(any(AnimeListChangedEvent.class));
    assertThat(persistenceFacade.fetchFilterList().isEmpty()).isFalse();
    assertThat(persistenceFacade.fetchFilterList().get(0).getThumbnail()).isEqualTo(thumbnail);
  }


  //@Test(groups = UNIT_TEST_GROUP)
  public void testUpdateOrCreateForNewWatchListEntry() throws MalformedURLException {
    // given
    final WatchListEntry entry = new WatchListEntry("Death Note",
        new InfoLink("http://myanimelist.net/anime/1535"),
        new URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg"));

    // when
    persistenceFacade.updateOrCreate(entry);

    // then
    verify(eventBusMock, times(1)).post(any(AnimeListChangedEvent.class));
    assertThat(persistenceFacade.fetchWatchList().isEmpty()).isFalse();
    assertThat(persistenceFacade.fetchWatchList().get(0)).isEqualTo(entry);
  }


  //@Test(groups = UNIT_TEST_GROUP)
  public void testUpdateOrCreateForModifiedWatchListEntry() throws MalformedURLException {
    // given
    final WatchListEntry entry = new WatchListEntry("Death Note",
        new InfoLink("http://myanimelist.net/anime/1535"),
        MinimalEntry.Companion.getNO_IMG_THUMB());

    persistenceFacade.watchAnime(entry);

    final URL thumbnail = new URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg");
    entry.setThumbnail(thumbnail);

    // when
    persistenceFacade.updateOrCreate(entry);

    // then
    verify(eventBusMock, times(2)).post(any(AnimeListChangedEvent.class));
    assertThat(persistenceFacade.fetchWatchList().isEmpty()).isFalse();
    assertThat(persistenceFacade.fetchWatchList().get(0).getThumbnail()).isEqualTo(thumbnail);
  }*/
}