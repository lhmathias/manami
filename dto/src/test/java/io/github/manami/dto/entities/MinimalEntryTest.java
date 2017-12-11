package io.github.manami.dto.entities;

import static io.github.manami.dto.TestConst.UNIT_TEST_GROUP;
import static io.github.manami.dto.entities.MinimalEntryKt.isValidMinimalEntry;
import static org.assertj.core.api.Assertions.assertThat;

import io.github.manami.dto.AnimeType;
import java.net.MalformedURLException;
import java.net.URL;
import org.testng.annotations.Test;

public class MinimalEntryTest {

  @Test(groups = UNIT_TEST_GROUP)
  public void testFilterEntryIsValid() throws MalformedURLException {
    // given
    final FilterEntry entry = new FilterEntry("Death Note",
        new InfoLink("http://myanimelist.net/anime/1535"),
        new URL("https://myanimelist.cdn-dena.com/images/anime/9/9453t.jpg"));

    // when
    final boolean result = isValidMinimalEntry(entry);

    // then
    assertThat(result).isTrue();
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testWatchListEntryIsValid() throws MalformedURLException {
    // given
    final WatchListEntry entry = new WatchListEntry("Death Note",
        new InfoLink("http://myanimelist.net/anime/1535"),
        new URL("https://myanimelist.cdn-dena.com/images/anime/9/9453t.jpg"));

    // when
    final boolean result = isValidMinimalEntry(entry);

    // then
    assertThat(result).isTrue();
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testAnimeIsValid() throws MalformedURLException {
    // given
    final Anime entry = new Anime("Death Note", new InfoLink("http://myanimelist.net/anime/1535"));
    entry.setEpisodes(37);
    entry.setLocation("/death_note");
    entry.setPicture(new URL("https://myanimelist.cdn-dena.com/images/anime/9/9453.jpg"));
    entry.setThumbnail(new URL("https://myanimelist.cdn-dena.com/images/anime/9/9453t.jpg"));
    entry.setType(AnimeType.TV);

    // when
    final boolean result = isValidMinimalEntry(entry);

    // then
    assertThat(result).isTrue();
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testAnimeValidMissingThumbnail() throws MalformedURLException {
    // given
    final Anime entry = new Anime("Death Note", new InfoLink("http://myanimelist.net/anime/1535"));
    entry.setEpisodes(37);
    entry.setLocation("/death_note");
    entry.setPicture(new URL("https://myanimelist.cdn-dena.com/images/anime/9/9453.jpg"));
    entry.setType(AnimeType.TV);

    // when
    final boolean result = isValidMinimalEntry(entry);

    // then
    assertThat(result).isTrue();
  }
}
