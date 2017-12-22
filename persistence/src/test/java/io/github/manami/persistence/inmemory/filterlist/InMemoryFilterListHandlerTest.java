package io.github.manami.persistence.inmemory.filterlist;

import static io.github.manami.dto.TestConst.UNIT_TEST_GROUP;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.assertj.core.api.Assertions.assertThat;

import io.github.manami.dto.AnimeType;
import io.github.manami.dto.entities.Anime;
import io.github.manami.dto.entities.FilterListEntry;
import io.github.manami.dto.entities.InfoLink;
import io.github.manami.dto.entities.MinimalEntry;
import io.github.manami.dto.entities.WatchListEntry;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class InMemoryFilterListHandlerTest {

  private InMemoryFilterListHandler inMemoryFilterListHandler;


  @BeforeMethod
  public void setUp() throws IOException {
    inMemoryFilterListHandler = new InMemoryFilterListHandler();
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testFilterAnimeIsEntryWithoutTitle() {
    // given
    final FilterListEntry entry = new FilterListEntry(EMPTY,
        new InfoLink("http://myanimelist.net/anime/1535"));

    // when
    final boolean result = inMemoryFilterListHandler.filterAnime(entry);

    // then
    assertThat(result).isFalse();
    assertThat(inMemoryFilterListHandler.fetchFilterList().size()).isEqualTo(0);
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testFilterAnimeIsEntryWithoutInfoLink() {
    // given
    final FilterListEntry entry = new FilterListEntry("Death Note", new InfoLink(""));

    // when
    final boolean result = inMemoryFilterListHandler.filterAnime(entry);

    // then
    assertThat(result).isFalse();
    assertThat(inMemoryFilterListHandler.fetchFilterList().size()).isEqualTo(0);
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testFilterAnimeIsEntryWithoutThumbnail() {
    // given
    final FilterListEntry entry = new FilterListEntry("Death Note",
        new InfoLink("http://myanimelist.net/anime/1535"));

    // when
    final boolean result = inMemoryFilterListHandler.filterAnime(entry);

    // then
    assertThat(result).isTrue();
    assertThat(inMemoryFilterListHandler.fetchFilterList().size()).isEqualTo(1);
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testFilterAnimeIsFullEntry() throws MalformedURLException {
    // given
    final FilterListEntry entry = new FilterListEntry(
        "Death Note",
        new InfoLink("http://myanimelist.net/anime/1535"),
        new URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg")
    );

    // when
    final boolean result = inMemoryFilterListHandler.filterAnime(entry);

    // then
    assertThat(result).isTrue();
    assertThat(inMemoryFilterListHandler.fetchFilterList().size()).isEqualTo(1);
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testFilterEntryExists() {
    // given
    final InfoLink infoLink = new InfoLink("http://myanimelist.net/anime/1535");
    final FilterListEntry entry = new FilterListEntry("Death Note", infoLink);
    inMemoryFilterListHandler.filterAnime(entry);

    // when
    final boolean result = inMemoryFilterListHandler.filterEntryExists(infoLink);

    // then
    assertThat(result).isTrue();
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testFilterEntryNotExists() {
    // given

    // when
    final boolean result = inMemoryFilterListHandler
        .filterEntryExists(new InfoLink("http://myanimelist.net/anime/1535"));

    // then
    assertThat(result).isFalse();
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testFilterAnimeList() {
    // given
    final FilterListEntry entry = new FilterListEntry("Death Note",
        new InfoLink("http://myanimelist.net/anime/1535"));
    inMemoryFilterListHandler.filterAnime(entry);

    // when
    final List<FilterListEntry> fetchFilterList = inMemoryFilterListHandler.fetchFilterList();

    // then
    assertThat(fetchFilterList.size()).isEqualTo(1);
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testRemoveFromFilterListWorks() {
    // given
    final InfoLink infoLink = new InfoLink("http://myanimelist.net/anime/1535");
    final FilterListEntry entry = new FilterListEntry("Death Note", infoLink);
    inMemoryFilterListHandler.filterAnime(entry);

    // when
    final boolean result = inMemoryFilterListHandler.removeFromFilterList(infoLink);

    // then
    assertThat(result).isTrue();
    assertThat(inMemoryFilterListHandler.fetchFilterList().isEmpty()).isTrue();
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testUpdateOrCreateForNewFilterEntry() throws MalformedURLException {
    // given
    final FilterListEntry entry = new FilterListEntry(
        "Death Note",
        new InfoLink("http://myanimelist.net/anime/1535"),
        new URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg")
    );

    // when
    inMemoryFilterListHandler.updateOrCreate(entry);

    // then
    assertThat(inMemoryFilterListHandler.fetchFilterList().isEmpty()).isFalse();
    assertThat(inMemoryFilterListHandler.fetchFilterList().get(0)).isEqualTo(entry);
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testUpdateOrCreateForModifiedFilterEntry() throws MalformedURLException {
    // given
    final FilterListEntry entry = new FilterListEntry(
        "Death Note",
        new InfoLink("http://myanimelist.net/anime/1535"),
        MinimalEntry.Companion.getNO_IMG_THUMB()
    );

    inMemoryFilterListHandler.filterAnime(entry);

    final URL thumbnail = new URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg");
    entry.setThumbnail(thumbnail);

    // when
    inMemoryFilterListHandler.updateOrCreate(entry);

    // then
    assertThat(inMemoryFilterListHandler.fetchFilterList().isEmpty()).isFalse();
    assertThat(inMemoryFilterListHandler.fetchFilterList().get(0).getThumbnail())
        .isEqualTo(thumbnail);
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testClearing() throws MalformedURLException {
    // given
    final FilterListEntry entry = new FilterListEntry(
        "Death Note",
        new InfoLink("http://myanimelist.net/anime/1535"),
        new URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg")
    );
    inMemoryFilterListHandler.filterAnime(entry);

    // when
    inMemoryFilterListHandler.clear();

    // then
    assertThat(inMemoryFilterListHandler.fetchFilterList().isEmpty()).isTrue();
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testFilterEntryNotAddedBecauseItAlreadyExists() throws MalformedURLException {
    // given
    final FilterListEntry entry = new FilterListEntry(
        "Death Note",
        new InfoLink("http://myanimelist.net/anime/1535"),
        new URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg")
    );
    inMemoryFilterListHandler.filterAnime(entry);

    // when
    final boolean result = inMemoryFilterListHandler.filterAnime(entry);

    // then
    assertThat(result).isFalse();
    assertThat(inMemoryFilterListHandler.fetchFilterList().size()).isEqualTo(1);
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testFilterWatchListEntry() throws MalformedURLException {
    // given
    final WatchListEntry entry = new WatchListEntry(
        "Death Note",
        new InfoLink("http://myanimelist.net/anime/1535"),
        new URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg")
    );

    // when
    final boolean result = inMemoryFilterListHandler.filterAnime(entry);

    // then
    assertThat(result).isTrue();
    assertThat(inMemoryFilterListHandler.fetchFilterList().size()).isEqualTo(1);
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testFilterAnime() throws MalformedURLException {
    // given
    final Anime entry = new Anime("Death Note", new InfoLink("http://myanimelist.net/anime/1535"));
    entry.setEpisodes(37);
    entry.setLocation("/death_note");
    entry.setPicture(new URL("http://cdn.myanimelist.net/images/anime/9/9453.jpg"));
    entry.setThumbnail(new URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg"));
    entry.setType(AnimeType.TV);

    // when
    final boolean result = inMemoryFilterListHandler.filterAnime(entry);

    // then
    assertThat(result).isTrue();
    assertThat(inMemoryFilterListHandler.fetchFilterList().size()).isEqualTo(1);
  }
}
