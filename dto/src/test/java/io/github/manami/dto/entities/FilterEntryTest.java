package io.github.manami.dto.entities;

import static io.github.manami.dto.TestConst.UNIT_TEST_GROUP;
import static org.assertj.core.api.Assertions.assertThat;

import io.github.manami.dto.AnimeType;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import org.testng.annotations.Test;

public class FilterEntryTest {

  @Test(groups = UNIT_TEST_GROUP)
  public void testValueOfFromNull() {
    // given

    // when
    final FilterEntry result = FilterEntry.valueOf(null);

    // then
    assertThat(result).isNull();
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testValueOfFromAnime() throws MalformedURLException {
    // given
    final Anime anime = new Anime("Death Note", new InfoLink("http://myanimelist.net/anime/1535"));
    anime.setEpisodes(37);
    anime.setLocation("/anime/series/death_note");
    anime.setPicture(new URL("https://myanimelist.cdn-dena.com/images/anime/9/9453.jpg"));
    anime.setThumbnail(new URL("https://myanimelist.cdn-dena.com/images/anime/9/9453t.jpg"));
    anime.setType(AnimeType.TV);

    // when
    final FilterEntry result = FilterEntry.valueOf(anime);

    // then
    assertThat(result).isNotNull();
    assertThat(result.getTitle()).isEqualTo(anime.getTitle());
    assertThat(result.getThumbnail()).isEqualTo(anime.getThumbnail());
    assertThat(result.getInfoLink()).isEqualTo(anime.getInfoLink());
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testValueOfFromWatchListEntry() throws MalformedURLException {
    // given
    final WatchListEntry entry = new WatchListEntry("Death Note",
        new InfoLink("http://myanimelist.net/anime/1535"),
        new URL("https://myanimelist.cdn-dena.com/images/anime/9/9453t.jpg"));

    // when
    final FilterEntry result = FilterEntry.valueOf(entry);

    // then
    assertThat(result).isNotNull();
    assertThat(result.getTitle()).isEqualTo(entry.getTitle());
    assertThat(result.getThumbnail()).isEqualTo(entry.getThumbnail());
    assertThat(result.getInfoLink()).isEqualTo(entry.getInfoLink());
  }
}
