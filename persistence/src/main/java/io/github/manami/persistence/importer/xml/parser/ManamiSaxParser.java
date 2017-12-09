package io.github.manami.persistence.importer.xml.parser;

import static com.google.common.collect.Lists.newArrayList;

import io.github.manami.dto.AnimeType;
import io.github.manami.dto.ToolVersion;
import io.github.manami.dto.entities.Anime;
import io.github.manami.dto.entities.FilterEntry;
import io.github.manami.dto.entities.InfoLink;
import io.github.manami.dto.entities.WatchListEntry;
import io.github.manami.persistence.PersistenceFacade;
import io.github.manami.persistence.importer.xml.postprocessor.ImportMigrationPostProcessor;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author manami-project
 * @version 2.0.0
 */
public class ManamiSaxParser extends DefaultHandler {

  private static final Logger log = LoggerFactory.getLogger(ManamiSaxParser.class);
  private static final String THUMBNAIL_PARSING_EXCEPTION = "Unable to parse thumbnail URL from [{}]";

  /**
   * This is the builder for the text within the elements.
   */
  private StringBuilder strBuilder;

  private final PersistenceFacade persistence;

  private final List<Anime> animeListEntries;
  private final List<FilterEntry> filterListEntries;
  private final List<WatchListEntry> watchListEntries;

  private ImportMigrationPostProcessor importMigrationPostProcessor;


  /**
   * Constructor awaiting a list.
   *
   * @version 2.0.0
   */
  public ManamiSaxParser(final PersistenceFacade persistence) {
    this.persistence = persistence;
    animeListEntries = newArrayList();
    filterListEntries = newArrayList();
    watchListEntries = newArrayList();
  }


  @Override
  public void startElement(final String namespaceURI, final String localName, final String qName, final Attributes attributes) {
    strBuilder = new StringBuilder();

    switch (qName) {
      case "manami":
        importMigrationPostProcessor = new ImportMigrationPostProcessor(ToolVersion.getToolVersion(),
            attributes.getValue("version"),
            animeListEntries,
            filterListEntries,
            watchListEntries);
        break;
      case "anime":
        createAnimeEntry(attributes);
        break;
      case "filterEntry":
        createFilterEntry(attributes);
        break;
      case "watchListEntry":
        createWatchListEntry(attributes);
        break;
    }
  }


  @Override
  public void endDocument() throws SAXException {
    persistence.addAnimeList(animeListEntries);
    persistence.addFilterList(filterListEntries);
    persistence.addWatchList(watchListEntries);
    importMigrationPostProcessor.execute();
  }


  /**
   * @since 2.7.0
   */
  private void createAnimeEntry(final Attributes attributes) {
    final String title = attributes.getValue("title").trim();
    final InfoLink infoLink = new InfoLink(attributes.getValue("infoLink").trim());

    final Anime actAnime = new Anime(title, infoLink);
    actAnime.setType(AnimeType.findByName(attributes.getValue("type").trim()));
    actAnime.setEpisodes(Integer.valueOf(attributes.getValue("episodes").trim()));
    actAnime.setLocation(attributes.getValue("location").trim());

    animeListEntries.add(actAnime);
  }


  /**
   * @since 2.7.0
   */
  private void createFilterEntry(final Attributes attributes) {
    try {
      final FilterEntry entry = new FilterEntry(attributes.getValue("title").trim(),
          new InfoLink(attributes.getValue("infoLink").trim()),
          new URL(attributes.getValue("thumbnail").trim()));

      filterListEntries.add(entry);
    } catch (MalformedURLException e) {
      log.error(THUMBNAIL_PARSING_EXCEPTION, attributes.getValue("title"));
    }
  }


  /**
   * @since 2.8.0
   */
  private void createWatchListEntry(final Attributes attributes) {
    try {
      final WatchListEntry entry = new WatchListEntry(attributes.getValue("title").trim(),
          new InfoLink(attributes.getValue("infoLink").trim()),
          new URL(attributes.getValue("thumbnail").trim()));

      watchListEntries.add(entry);
    } catch (MalformedURLException e) {
      log.error(THUMBNAIL_PARSING_EXCEPTION, attributes.getValue("title"));
    }
  }


  @Override
  public void characters(final char ch[], final int start, final int length) {
    strBuilder.append(new String(ch, start, length));
  }
}
