package io.github.manami.persistence;

import static com.google.common.collect.Lists.newArrayList;
import static io.github.manami.dto.TestConst.UNIT_TEST_GROUP;
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
import io.github.manami.dto.events.AnimeListChangedEvent;
import io.github.manami.persistence.inmemory.InMemoryPersistenceHandler;
import io.github.manami.persistence.inmemory.animelist.InMemoryAnimeListHandler;
import io.github.manami.persistence.inmemory.filterlist.InMemoryFilterListHandler;
import io.github.manami.persistence.inmemory.watchlist.InMemoryWatchListHandler;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class PersistenceFacadeTest {

  private PersistenceFacade persistenceFacade;
  private EventBus eventBusMock;
  private InMemoryPersistenceHandler inMemoryPersistenceHandler;


  @BeforeMethod
  public void setUp() throws IOException {
    inMemoryPersistenceHandler = new InMemoryPersistenceHandler(new InMemoryAnimeListHandler(),
        new InMemoryFilterListHandler(), new InMemoryWatchListHandler());
    eventBusMock = mock(EventBus.class);
    persistenceFacade = new PersistenceFacade(inMemoryPersistenceHandler, eventBusMock);
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testFilterAnimeIsEntryWithoutTitle() {
    // given
    final FilterListEntry entry = new FilterListEntry(EMPTY,
        new InfoLink("http://myanimelist.net/anime/1535"));

    // when
    final boolean result = persistenceFacade.filterAnime(entry);

    // then
    verify(eventBusMock, times(0)).post(any(AnimeListChangedEvent.class));
    assertThat(result).isFalse();
    assertThat(persistenceFacade.fetchFilterList().size()).isEqualTo(0);
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testFilterAnimeIsEntryWithoutInfoLink() {
    // given
    final FilterListEntry entry = new FilterListEntry("Death Note", new InfoLink(""));

    // when
    final boolean result = persistenceFacade.filterAnime(entry);

    // then
    verify(eventBusMock, times(0)).post(any(AnimeListChangedEvent.class));
    assertThat(result).isFalse();
    assertThat(persistenceFacade.fetchFilterList().size()).isEqualTo(0);
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testFilterAnimeIsEntryWithoutThumbnail() {
    // given
    final FilterListEntry entry = new FilterListEntry("Death Note",
        new InfoLink("http://myanimelist.net/anime/1535"));

    // when
    final boolean result = persistenceFacade.filterAnime(entry);

    // then
    verify(eventBusMock, times(1)).post(any(AnimeListChangedEvent.class));
    assertThat(result).isTrue();
    assertThat(persistenceFacade.fetchFilterList().size()).isEqualTo(1);
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testFilterAnimeIsFullEntry() throws MalformedURLException {
    // given
    final FilterListEntry entry = new FilterListEntry("Death Note",
        new InfoLink("http://myanimelist.net/anime/1535"),
        new URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg"));

    // when
    final boolean result = persistenceFacade.filterAnime(entry);

    // then
    verify(eventBusMock, times(1)).post(any(AnimeListChangedEvent.class));
    assertThat(result).isTrue();
    assertThat(persistenceFacade.fetchFilterList().size()).isEqualTo(1);
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testFilterEntryExists() {
    // given
    final InfoLink infoLink = new InfoLink("http://myanimelist.net/anime/1535");
    final FilterListEntry entry = new FilterListEntry("Death Note", infoLink);
    persistenceFacade.filterAnime(entry);

    // when
    final boolean result = persistenceFacade.filterEntryExists(infoLink);

    // then
    assertThat(result).isTrue();
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testFilterEntryNotExists() {
    // given

    // when
    final boolean result = persistenceFacade
        .filterEntryExists(new InfoLink("http://myanimelist.net/anime/1535"));

    // then
    verify(eventBusMock, times(0)).post(any(AnimeListChangedEvent.class));
    assertThat(result).isFalse();
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testFilterAnimeList() {
    // given
    final FilterListEntry entry = new FilterListEntry("Death Note",
        new InfoLink("http://myanimelist.net/anime/1535"));
    persistenceFacade.filterAnime(entry);

    // when
    final List<FilterListEntry> fetchFilterList = persistenceFacade.fetchFilterList();

    // then
    assertThat(fetchFilterList.size()).isEqualTo(1);
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testFetchWatchList() {
    // given
    final WatchListEntry entry = new WatchListEntry("Death Note",
        new InfoLink("http://myanimelist.net/anime/1535"));
    persistenceFacade.watchAnime(entry);

    // when
    final List<WatchListEntry> fetchWatchList = persistenceFacade.fetchWatchList();

    // then
    assertThat(fetchWatchList.size()).isEqualTo(1);
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testRemoveFromFilterListWorks() {
    // given
    final InfoLink infoLink = new InfoLink("http://myanimelist.net/anime/1535");
    final FilterListEntry entry = new FilterListEntry("Death Note", infoLink);
    persistenceFacade.filterAnime(entry);

    // when
    final boolean result = persistenceFacade.removeFromFilterList(infoLink);

    // then
    verify(eventBusMock, times(2)).post(any(AnimeListChangedEvent.class));
    assertThat(result).isTrue();
    assertThat(persistenceFacade.fetchFilterList().isEmpty()).isTrue();
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testWatchListEntryExists() {
    // given
    final InfoLink infoLink = new InfoLink("http://myanimelist.net/anime/1535");
    final WatchListEntry entry = new WatchListEntry("Death Note", infoLink);
    persistenceFacade.watchAnime(entry);

    // when
    final boolean result = persistenceFacade.watchListEntryExists(infoLink);

    // then
    assertThat(result).isTrue();
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testWatchListEntryNotExists() {
    // given

    // when
    final boolean result = persistenceFacade
        .watchListEntryExists(new InfoLink("http://myanimelist.net/anime/1535"));

    // then
    verify(eventBusMock, times(0)).post(any(AnimeListChangedEvent.class));
    assertThat(result).isFalse();
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testWatchAnimeIsEntryWithoutTitle() {
    // given
    final WatchListEntry entry = new WatchListEntry(EMPTY,
        new InfoLink("http://myanimelist.net/anime/1535"));

    // when
    final boolean result = persistenceFacade.watchAnime(entry);

    // then
    verify(eventBusMock, times(0)).post(any(AnimeListChangedEvent.class));
    assertThat(result).isFalse();
    assertThat(persistenceFacade.fetchWatchList().size()).isEqualTo(0);
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testWatchAnimeIsEntryWithoutInfoLink() {
    // given
    final WatchListEntry entry = new WatchListEntry("Death Note", new InfoLink(""));

    // when
    final boolean result = persistenceFacade.watchAnime(entry);

    // then
    verify(eventBusMock, times(0)).post(any(AnimeListChangedEvent.class));
    assertThat(result).isFalse();
    assertThat(persistenceFacade.fetchWatchList().size()).isEqualTo(0);
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testWatchAnimeIsEntryWithoutThumbnail() {
    // given
    final WatchListEntry entry = new WatchListEntry("Death Note",
        new InfoLink("http://myanimelist.net/anime/1535"));

    // when
    final boolean result = persistenceFacade.watchAnime(entry);

    // then
    verify(eventBusMock, times(1)).post(any(AnimeListChangedEvent.class));
    assertThat(result).isTrue();
    assertThat(persistenceFacade.fetchWatchList().size()).isEqualTo(1);
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testWatchAnimeIsFullEntry() throws MalformedURLException {
    // given
    final WatchListEntry entry = new WatchListEntry("Death Note",
        new InfoLink("http://myanimelist.net/anime/1535"),
        new URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg"));

    // when
    final boolean result = persistenceFacade.watchAnime(entry);

    // then
    verify(eventBusMock, times(1)).post(any(AnimeListChangedEvent.class));
    assertThat(result).isTrue();
    assertThat(persistenceFacade.fetchWatchList().size()).isEqualTo(1);
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testRemoveFromWatchListWorks() {
    // given
    final InfoLink infoLink = new InfoLink("http://myanimelist.net/anime/1535");
    final WatchListEntry entry = new WatchListEntry("Death Note", infoLink);
    persistenceFacade.watchAnime(entry);

    // when
    final boolean result = persistenceFacade.removeFromWatchList(infoLink);

    // then
    verify(eventBusMock, times(2)).post(any(AnimeListChangedEvent.class));
    assertThat(result).isTrue();
    assertThat(persistenceFacade.fetchWatchList().isEmpty()).isTrue();
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testaddAnimeIsFullEntry() throws MalformedURLException {
    // given
    final Anime entry = new Anime("Death Note", new InfoLink("http://myanimelist.net/anime/1535"));
    entry.setEpisodes(37);
    entry.setLocation("/death_note");
    entry.setPicture(new URL("http://cdn.myanimelist.net/images/anime/9/9453.jpg"));
    entry.setThumbnail(new URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg"));
    entry.setType(AnimeType.TV);

    // when
    final boolean result = persistenceFacade.addAnime(entry);

    // then
    verify(eventBusMock, times(1)).post(any(AnimeListChangedEvent.class));
    assertThat(result).isTrue();
    assertThat(persistenceFacade.fetchAnimeList().size()).isEqualTo(1);
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testaddAnimeIsEntryWithoutEpisodes() throws MalformedURLException {
    // given
    final Anime entry = new Anime("Death Note", new InfoLink("http://myanimelist.net/anime/1535"));
    entry.setLocation("/death_note");
    entry.setPicture(new URL("http://cdn.myanimelist.net/images/anime/9/9453.jpg"));
    entry.setThumbnail(new URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg"));
    entry.setType(AnimeType.TV);

    // when
    final boolean result = persistenceFacade.addAnime(entry);

    // then
    verify(eventBusMock, times(1)).post(any(AnimeListChangedEvent.class));
    assertThat(result).isTrue();
    assertThat(persistenceFacade.fetchAnimeList().size()).isEqualTo(1);
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testaddAnimeIsEntryWithoutInfoLink() throws MalformedURLException {
    // given
    final Anime entry = new Anime("Death Note", new InfoLink(""));
    entry.setEpisodes(37);
    entry.setLocation("/death_note");
    entry.setPicture(new URL("http://cdn.myanimelist.net/images/anime/9/9453.jpg"));
    entry.setThumbnail(new URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg"));
    entry.setType(AnimeType.TV);

    // when
    final boolean result = persistenceFacade.addAnime(entry);

    // then
    verify(eventBusMock, times(1)).post(any(AnimeListChangedEvent.class));
    assertThat(result).isTrue();
    assertThat(persistenceFacade.fetchAnimeList().size()).isEqualTo(1);
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testaddAnimeIsEntryWithoutLocation() throws MalformedURLException {
    // given
    final Anime entry = new Anime("Death Note", new InfoLink("http://myanimelist.net/anime/1535"));
    entry.setEpisodes(37);
    entry.setPicture(new URL("http://cdn.myanimelist.net/images/anime/9/9453.jpg"));
    entry.setThumbnail(new URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg"));
    entry.setType(AnimeType.TV);

    // when
    final boolean result = persistenceFacade.addAnime(entry);

    // then
    verify(eventBusMock, times(0)).post(any(AnimeListChangedEvent.class));
    assertThat(result).isFalse();
    assertThat(persistenceFacade.fetchAnimeList().size()).isEqualTo(0);
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testaddAnimeIsEntryWithoutPicture() throws MalformedURLException {
    // given
    final Anime entry = new Anime("Death Note", new InfoLink("http://myanimelist.net/anime/1535"));
    entry.setEpisodes(37);
    entry.setLocation("/death_note");
    entry.setThumbnail(new URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg"));
    entry.setType(AnimeType.TV);

    // when
    final boolean result = persistenceFacade.addAnime(entry);

    // then
    verify(eventBusMock, times(1)).post(any(AnimeListChangedEvent.class));
    assertThat(result).isTrue();
    assertThat(persistenceFacade.fetchAnimeList().size()).isEqualTo(1);
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testaddAnimeIsEntryWithoutThumbnail() throws MalformedURLException {
    // given
    final Anime entry = new Anime("Death Note", new InfoLink("http://myanimelist.net/anime/1535"));
    entry.setEpisodes(37);
    entry.setLocation("/death_note");
    entry.setPicture(new URL("http://cdn.myanimelist.net/images/anime/9/9453.jpg"));
    entry.setType(AnimeType.TV);

    // when
    final boolean result = persistenceFacade.addAnime(entry);

    // then
    verify(eventBusMock, times(1)).post(any(AnimeListChangedEvent.class));
    assertThat(result).isTrue();
    assertThat(persistenceFacade.fetchAnimeList().size()).isEqualTo(1);
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testaddAnimeIsEntryWithoutTitle() throws MalformedURLException {
    // given
    final Anime entry = new Anime("", new InfoLink("http://myanimelist.net/anime/1535"));
    entry.setEpisodes(37);
    entry.setLocation("/death_note");
    entry.setPicture(new URL("http://cdn.myanimelist.net/images/anime/9/9453.jpg"));
    entry.setThumbnail(new URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg"));
    entry.setType(AnimeType.TV);

    // when
    final boolean result = persistenceFacade.addAnime(entry);

    // then
    verify(eventBusMock, times(0)).post(any(AnimeListChangedEvent.class));
    assertThat(result).isFalse();
    assertThat(persistenceFacade.fetchAnimeList().size()).isEqualTo(0);
  }


  @Test(
      groups = UNIT_TEST_GROUP,
      description = "Fallback for the type is TV. So in case no AnimeType has been set TV is being used. Therefore the entry will be added."
  )
  public void testaddAnimeIsEntryWithoutType() throws MalformedURLException {
    // given
    final Anime entry = new Anime("Death Note", new InfoLink("http://myanimelist.net/anime/1535"));
    entry.setEpisodes(37);
    entry.setLocation("/death_note");
    entry.setPicture(new URL("http://cdn.myanimelist.net/images/anime/9/9453.jpg"));
    entry.setThumbnail(new URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg"));

    // when
    final boolean result = persistenceFacade.addAnime(entry);

    // then
    verify(eventBusMock, times(1)).post(any(AnimeListChangedEvent.class));
    assertThat(result).isTrue();
    assertThat(persistenceFacade.fetchAnimeList().size()).isEqualTo(1);
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testAnimeEntryExists() throws MalformedURLException {
    // given
    final InfoLink infoLink = new InfoLink("http://myanimelist.net/anime/1535");
    final Anime entry = new Anime("Death Note", infoLink);
    entry.setEpisodes(37);
    entry.setLocation("/death_note");
    entry.setPicture(new URL("http://cdn.myanimelist.net/images/anime/9/9453.jpg"));
    entry.setThumbnail(new URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg"));
    entry.setType(AnimeType.TV);
    persistenceFacade.addAnime(entry);

    // when
    final boolean result = persistenceFacade.animeEntryExists(infoLink);

    // then
    assertThat(result).isTrue();
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testAnimeEntryNotExists() {
    // given

    // when
    final boolean result = persistenceFacade
        .animeEntryExists(new InfoLink("http://myanimelist.net/anime/1535"));

    // then
    verify(eventBusMock, times(0)).post(any(AnimeListChangedEvent.class));
    assertThat(result).isFalse();
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testAnimeList() throws MalformedURLException {
    // given
    final Anime entry = new Anime("Death Note", new InfoLink("http://myanimelist.net/anime/1535"));
    entry.setEpisodes(37);
    entry.setLocation("/death_note");
    entry.setPicture(new URL("http://cdn.myanimelist.net/images/anime/9/9453.jpg"));
    entry.setThumbnail(new URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg"));
    entry.setType(AnimeType.TV);
    persistenceFacade.addAnime(entry);

    // when
    final List<Anime> animeList = persistenceFacade.fetchAnimeList();

    // then
    assertThat(animeList.size()).isEqualTo(1);
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testRemoveFromAnimeListWorks() throws MalformedURLException {
    // given
    final Anime entry = new Anime("Death Note", new InfoLink("http://myanimelist.net/anime/1535"));
    entry.setEpisodes(37);
    entry.setLocation("/death_note");
    entry.setPicture(new URL("http://cdn.myanimelist.net/images/anime/9/9453.jpg"));
    entry.setThumbnail(new URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg"));
    entry.setType(AnimeType.TV);
    persistenceFacade.addAnime(entry);

    // when
    final boolean result = persistenceFacade.removeAnime(entry.getId());

    // then
    verify(eventBusMock, times(2)).post(any(AnimeListChangedEvent.class));
    assertThat(result).isTrue();
    assertThat(persistenceFacade.fetchAnimeList().isEmpty()).isTrue();
  }


  @Test(groups = UNIT_TEST_GROUP)
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


  @Test(groups = UNIT_TEST_GROUP)
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


  @Test(groups = UNIT_TEST_GROUP)
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


  @Test(groups = UNIT_TEST_GROUP)
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


  @Test(groups = UNIT_TEST_GROUP)
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


  @Test(groups = UNIT_TEST_GROUP)
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


  @Test(groups = UNIT_TEST_GROUP)
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


  @Test(groups = UNIT_TEST_GROUP)
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


  @Test(groups = UNIT_TEST_GROUP)
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


  @Test(groups = UNIT_TEST_GROUP)
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
  }
}