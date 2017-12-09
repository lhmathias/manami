package io.github.manami.dto.comparator;

import static io.github.manami.dto.TestConst.UNIT_TEST_GROUP;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.assertj.core.api.Assertions.assertThat;

import io.github.manami.dto.entities.InfoLink;
import io.github.manami.dto.entities.WatchListEntry;
import java.net.MalformedURLException;
import java.net.URL;
import org.testng.annotations.Test;

public class MinimalEntryComByTitleAscTest {

  @Test(groups = UNIT_TEST_GROUP)
  public void testFirstOneGreater() throws MalformedURLException {
    // given
    final MinimalEntryComByTitleAsc comparator = new MinimalEntryComByTitleAsc();

    final WatchListEntry steinsGate = new WatchListEntry(
        "Steins;Gate",
        new InfoLink("http://myanimelist.net/anime/9253"),
        new URL("https://myanimelist.cdn-dena.com/images/anime/5/73199t.jpg"));

    final WatchListEntry gintama = new WatchListEntry("Gintama",
        new InfoLink("http://myanimelist.net/anime/28977"),
        new URL("https://myanimelist.cdn-dena.com/images/anime/3/72078t.jpg"));

    // when
    final int result = comparator.compare(steinsGate, gintama);

    // then
    assertThat(result > 0).isTrue();
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testFirstOneLesser() throws MalformedURLException {
    // given
    final MinimalEntryComByTitleAsc comparator = new MinimalEntryComByTitleAsc();

    final WatchListEntry steinsGate = new WatchListEntry("Steins;Gate",
        new InfoLink("http://myanimelist.net/anime/9253"),
        new URL("https://myanimelist.cdn-dena.com/images/anime/5/73199t.jpg"));

    final WatchListEntry gintama = new WatchListEntry("Gintama",
        new InfoLink("http://myanimelist.net/anime/28977"),
        new URL("https://myanimelist.cdn-dena.com/images/anime/3/72078t.jpg"));

    // when
    final int result = comparator.compare(gintama, steinsGate);

    // then
    assertThat(result < 0).isTrue();
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testBothEqual() throws MalformedURLException {
    // given
    final MinimalEntryComByTitleAsc comparator = new MinimalEntryComByTitleAsc();

    final WatchListEntry steinsGate = new WatchListEntry("Steins;Gate",
        new InfoLink("http://myanimelist.net/anime/9253"),
        new URL("https://myanimelist.cdn-dena.com/images/anime/5/73199t.jpg"));

    // when
    final int result = comparator.compare(steinsGate, steinsGate);

    // then
    assertThat(result).isEqualTo(0);
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testFirstParameterNull() throws MalformedURLException {
    // given
    final MinimalEntryComByTitleAsc comparator = new MinimalEntryComByTitleAsc();

    final WatchListEntry steinsGate = new WatchListEntry("Steins;Gate",
        new InfoLink("http://myanimelist.net/anime/9253"),
        new URL("https://myanimelist.cdn-dena.com/images/anime/5/73199t.jpg"));

    // when
    final int result = comparator.compare(null, steinsGate);

    // then
    assertThat(result).isEqualTo(0);
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testSecondParameterNull() throws MalformedURLException {
    // given
    final MinimalEntryComByTitleAsc comparator = new MinimalEntryComByTitleAsc();

    final WatchListEntry steinsGate = new WatchListEntry("Steins;Gate",
        new InfoLink("http://myanimelist.net/anime/9253"),
        new URL("https://myanimelist.cdn-dena.com/images/anime/5/73199t.jpg"));

    // when
    final int result = comparator.compare(steinsGate, null);

    // then
    assertThat(result).isEqualTo(0);
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testFirstParameterTitleNullOrEmpty() throws MalformedURLException {
    // given
    final MinimalEntryComByTitleAsc comparator = new MinimalEntryComByTitleAsc();

    final WatchListEntry steinsGate = new WatchListEntry("Steins;Gate",
        new InfoLink("http://myanimelist.net/anime/9253"),
        new URL("https://myanimelist.cdn-dena.com/images/anime/5/73199t.jpg"));

    final WatchListEntry gintama = new WatchListEntry("Gintama",
        new InfoLink("http://myanimelist.net/anime/28977"),
        new URL("https://myanimelist.cdn-dena.com/images/anime/3/72078t.jpg"));
    gintama.setTitle(EMPTY);

    // when
    final int result = comparator.compare(gintama, steinsGate);

    // then
    assertThat(result).isEqualTo(0);
  }


  @Test(groups = UNIT_TEST_GROUP)
  public void testSecondParameterTitleNullOrEmpty() throws MalformedURLException {
    // given
    final MinimalEntryComByTitleAsc comparator = new MinimalEntryComByTitleAsc();

    final WatchListEntry steinsGate = new WatchListEntry(
        "Steins;Gate",
        new InfoLink("http://myanimelist.net/anime/9253"),
        new URL("https://myanimelist.cdn-dena.com/images/anime/5/73199t.jpg"));

    final WatchListEntry gintama = new WatchListEntry("Gintama",
        new InfoLink("http://myanimelist.net/anime/28977"),
        new URL("https://myanimelist.cdn-dena.com/images/anime/3/72078t.jpg"));
    steinsGate.setTitle(EMPTY);

    // when
    final int result = comparator.compare(gintama, steinsGate);

    // then
    assertThat(result).isEqualTo(0);
  }
}