package io.github.manami.persistence.importer.xml.postprocessor;

import static com.google.common.collect.Lists.newArrayList;
import static io.github.manami.dto.TestConst.UNIT_TEST_GROUP;
import static org.assertj.core.api.Assertions.assertThat;

import io.github.manami.dto.entities.Anime;
import io.github.manami.dto.entities.FilterListEntry;
import io.github.manami.dto.entities.InfoLink;
import io.github.manami.dto.entities.WatchListEntry;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.testng.annotations.Test;

// TODO: Additional tests for null check etc.
public class ImportMigrationPostProcessorTest {

  @Test(
      groups = UNIT_TEST_GROUP,
      description = "Test execution stops, because the document version is not valid. Result: no changes to the entries.",
      expectedExceptions = IllegalArgumentException.class
  )
  public void testMigrationStopsInvalidDocumentVersion() throws MalformedURLException {
    // given
    final URL entry1Thumb = new URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg");
    final URL entry2Thumb = new URL("http://img7.anidb.net/pics/anime/thumbs/50x65/129527.jpg-thumb.jpg");
    final FilterListEntry entry1 = new FilterListEntry(
        "Death Note",
        new InfoLink("http://myanimelist.net/anime/1535"),
        entry1Thumb
    );
    final FilterListEntry entry2 = new FilterListEntry(
        "Fullmetal Panic!",
        new InfoLink("http://anidb.net/perl-bin/animedb.pl?show=anime&aid=17"),
        entry2Thumb
    );
    final FilterListEntry entry3 = new FilterListEntry(
        "Code Geass: Hangyaku no Lelouch R2",
        new InfoLink("https://myanimelist.net/anime/2904/Code_Geass__Hangyaku_no_Lelouch_R2")
    );

    final List<FilterListEntry> filterListEntries = newArrayList(entry1, entry2, entry3);

    // when
    new ImportMigrationPostProcessor(
        "2.?.2",
        newArrayList(),
        filterListEntries,
        newArrayList()
    );
  }


  @Test(
      groups = UNIT_TEST_GROUP,
      description = "Test migration to version 2.10.3 throws an exception, because the document version is not valid.",
      expectedExceptions = IllegalArgumentException.class
  )
  public void testMigration2103IsSkippedBecauseTheCurrentDocumentVersionIsMoreRecent() throws MalformedURLException {
    // given
    final URL entry1Thumb = new URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg");
    final URL entry2Thumb = new URL("http://img7.anidb.net/pics/anime/thumbs/50x65/129527.jpg-thumb.jpg");
    final FilterListEntry entry1 = new FilterListEntry(
        "Death Note",
        new InfoLink("http://myanimelist.net/anime/1535"),
        entry1Thumb
    );
    final FilterListEntry entry2 = new FilterListEntry(
        "Fullmetal Panic!",
        new InfoLink("http://anidb.net/perl-bin/animedb.pl?show=anime&aid=17"),
        entry2Thumb
    );
    final FilterListEntry entry3 = new FilterListEntry("Code Geass: Hangyaku no Lelouch R2",
        new InfoLink("https://myanimelist.net/anime/2904/Code_Geass__Hangyaku_no_Lelouch_R2"));

    final List<FilterListEntry> filterListEntries = newArrayList(entry1, entry2, entry3);

    // when
    new ImportMigrationPostProcessor(
        "2.?.2",
        newArrayList(),
        filterListEntries,
        newArrayList()
    );
  }


  @Test(groups = UNIT_TEST_GROUP, description = "Test migration to version 2.10.3 for filter list")
  public void testMigration2103WorksForFilterList() throws MalformedURLException {
    // given
    final URL fmpThumb = new URL("http://img7.anidb.net/pics/anime/thumbs/50x65/129527.jpg-thumb.jpg");
    final FilterListEntry entry1 = new FilterListEntry(
        "Death Note",
        new InfoLink("http://myanimelist.net/anime/1535"),
        new URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg")
    );
    final FilterListEntry entry2 = new FilterListEntry(
        "Fullmetal Panic!",
        new InfoLink("http://anidb.net/perl-bin/animedb.pl?show=anime&aid=17"),
        fmpThumb
    );
    final FilterListEntry entry3 = new FilterListEntry(
        "Code Geass: Hangyaku no Lelouch R2",
        new InfoLink("https://myanimelist.net/anime/2904/Code_Geass__Hangyaku_no_Lelouch_R2")
    );

    final List<FilterListEntry> filterListEntries = newArrayList(entry1, entry2, entry3);

    final ImportMigrationPostProcessor processor = new ImportMigrationPostProcessor(
        "2.10.2",
        newArrayList(),
        filterListEntries,
        newArrayList()
    );

    // when
    processor.execute();

    // then
    assertThat(entry1.getThumbnail()).isEqualTo(new URL("https://myanimelist.cdn-dena.com/images/anime/9/9453t.jpg"));
    assertThat(entry2.getThumbnail()).isEqualTo(fmpThumb);
    assertThat(entry3.getThumbnail()).isEqualTo(new URL("https://myanimelist.cdn-dena.com/images/qm_50.gif"));
  }


  @Test(groups = UNIT_TEST_GROUP, description = "Test migration to version 2.10.3 for watch list")
  public void testMigration2103WorksForWatchList() throws MalformedURLException {
    // given
    final URL fmpThumb = new URL("http://img7.anidb.net/pics/anime/thumbs/50x65/129527.jpg-thumb.jpg");
    final WatchListEntry entry1 = new WatchListEntry(
        "Death Note",
        new InfoLink("http://myanimelist.net/anime/1535"),
        new URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg")
    );
    final WatchListEntry entry2 = new WatchListEntry(
        "Fullmetal Panic!",
        new InfoLink("http://anidb.net/perl-bin/animedb.pl?show=anime&aid=17"),
        fmpThumb
    );
    final WatchListEntry entry3 = new WatchListEntry(
        "Code Geass: Hangyaku no Lelouch R2", new InfoLink("https://myanimelist.net/anime/2904/Code_Geass__Hangyaku_no_Lelouch_R2"));

    final List<WatchListEntry> watchListEntries = newArrayList(entry1, entry2, entry3);

    final ImportMigrationPostProcessor processor = new ImportMigrationPostProcessor(
        "2.10.2",
        newArrayList(),
        newArrayList(),
        watchListEntries
    );

    // when
    processor.execute();

    // then
    assertThat(entry1.getThumbnail()).isEqualTo(new URL("https://myanimelist.cdn-dena.com/images/anime/9/9453t.jpg"));
    assertThat(entry2.getThumbnail()).isEqualTo(fmpThumb);
    assertThat(entry3.getThumbnail()).isEqualTo(new URL("https://myanimelist.cdn-dena.com/images/qm_50.gif"));
  }


  @Test(groups = UNIT_TEST_GROUP, description = "Test migration to version 2.14.2 for anime list")
  public void testMigration2142WorksForAnimeList() throws MalformedURLException {
    // given
    final Anime entry1 = new Anime("Death Note", new InfoLink("http://myanimelist.net/anime/1535"));

    final List<Anime> animeListEntries = newArrayList(entry1);

    final ImportMigrationPostProcessor processor = new ImportMigrationPostProcessor("2.14.2", animeListEntries, newArrayList(),
        newArrayList());

    // when
    processor.execute();

    // then
    assertThat(entry1.getInfoLink().getUrl()).isEqualTo(new URL("https://myanimelist.net/anime/1535"));
  }


  @Test(groups = UNIT_TEST_GROUP, description = "Test migration to version 2.14.2 for filter list")
  public void testMigration2142WorksForFilterList() throws MalformedURLException {
    // given
    final FilterListEntry entry1 = new FilterListEntry(
        "Death Note",
        new InfoLink("http://myanimelist.net/anime/1535"),
        new URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg")
    );

    final List<FilterListEntry> filterListEntries = newArrayList(entry1);

    final ImportMigrationPostProcessor processor = new ImportMigrationPostProcessor("2.14.2", newArrayList(), filterListEntries,
        newArrayList());

    // when
    processor.execute();

    // then
    assertThat(entry1.getInfoLink().getUrl()).isEqualTo(new URL("https://myanimelist.net/anime/1535"));
  }


  @Test(groups = UNIT_TEST_GROUP, description = "Test migration to version 2.14.2 for watch list")
  public void testMigration2142WorksForWatchList() throws MalformedURLException {
    // given
    final WatchListEntry entry1 = new WatchListEntry(
        "Death Note",
        new InfoLink("http://myanimelist.net/anime/1535"),
        new URL("http://cdn.myanimelist.net/images/anime/9/9453t.jpg")
    );

    final List<WatchListEntry> watchListEntries = newArrayList(entry1);

    final ImportMigrationPostProcessor processor = new ImportMigrationPostProcessor("2.14.2", newArrayList(), newArrayList(),
        watchListEntries);

    // when
    processor.execute();

    // then
    assertThat(entry1.getInfoLink().getUrl()).isEqualTo(new URL("https://myanimelist.net/anime/1535"));
  }
}
