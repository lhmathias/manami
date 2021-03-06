package io.github.manami.persistence.importer.xml.parser;

import static io.github.manami.dto.TestConst.UNIT_TEST_GROUP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.core.io.ClassPathResource;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import com.google.common.eventbus.EventBus;

import io.github.manami.dto.AnimeType;
import io.github.manami.dto.entities.Anime;
import io.github.manami.dto.entities.FilterEntry;
import io.github.manami.dto.entities.WatchListEntry;
import io.github.manami.persistence.PersistenceFacade;
import io.github.manami.persistence.importer.xml.XmlImporter;
import io.github.manami.persistence.importer.xml.XmlImporter.XmlStrategy;
import io.github.manami.persistence.inmemory.InMemoryPersistenceHandler;
import io.github.manami.persistence.inmemory.animelist.InMemoryAnimeListHandler;
import io.github.manami.persistence.inmemory.filterlist.InMemoryFilterListHandler;
import io.github.manami.persistence.inmemory.watchlist.InMemoryWatchListHandler;

public class ManamiSaxParserTest {

    private static final String TEST_ANIME_LIST_FILE = "test_anime_list.xml";
    private XmlImporter xmlImporter;
    private Path file;
    private InMemoryPersistenceHandler inMemoryPersistenceHandler;


    @BeforeMethod
    public void setUp() throws IOException {
        inMemoryPersistenceHandler = new InMemoryPersistenceHandler(new InMemoryAnimeListHandler(), new InMemoryFilterListHandler(), new InMemoryWatchListHandler());
        final PersistenceFacade persistenceFacade = new PersistenceFacade(inMemoryPersistenceHandler, mock(EventBus.class));
        xmlImporter = new XmlImporter(XmlStrategy.MANAMI, persistenceFacade);
        final ClassPathResource resource = new ClassPathResource(TEST_ANIME_LIST_FILE);
        file = resource.getFile().toPath();
    }


    @Test(groups = UNIT_TEST_GROUP)
    public void testThatAnimeListIsParsedCorrectly() throws SAXException, ParserConfigurationException, IOException {
        // given

        // when
        xmlImporter.importFile(file);

        // then
        final List<Anime> fetchAnimeList = inMemoryPersistenceHandler.fetchAnimeList();
        assertThat(fetchAnimeList).isNotNull();
        assertThat(fetchAnimeList.isEmpty()).isFalse();
        assertThat(fetchAnimeList.size()).isEqualTo(2);

        final Anime bokuDake = fetchAnimeList.get(0);
        assertThat(bokuDake).isNotNull();
        assertThat(bokuDake.getEpisodes()).isEqualTo(12);
        assertThat(bokuDake.getInfoLink().getUrl()).isEqualTo("https://myanimelist.net/anime/31043");
        assertThat(bokuDake.getLocation()).isEqualTo("/anime/series/boku_dake_ga_inai_machi");
        assertThat(bokuDake.getTitle()).isEqualTo("Boku dake ga Inai Machi");
        assertThat(bokuDake.getType()).isEqualTo(AnimeType.TV);

        final Anime rurouniKenshin = fetchAnimeList.get(1);
        assertThat(rurouniKenshin).isNotNull();
        assertThat(rurouniKenshin.getEpisodes()).isEqualTo(4);
        assertThat(rurouniKenshin.getInfoLink().getUrl()).isEqualTo("https://myanimelist.net/anime/44");
        assertThat(rurouniKenshin.getLocation()).isEqualTo("/anime/series/rurouni_kenshin");
        assertThat(rurouniKenshin.getTitle()).isEqualTo("Rurouni Kenshin: Meiji Kenkaku Romantan - Tsuiokuhen");
        assertThat(rurouniKenshin.getType()).isEqualTo(AnimeType.OVA);
    }


    @Test(groups = UNIT_TEST_GROUP)
    public void testThatWatchListIsParsedCorrectly() throws SAXException, ParserConfigurationException, IOException {
        // given

        // when
        xmlImporter.importFile(file);

        // then
        final List<WatchListEntry> fetchWatchList = inMemoryPersistenceHandler.fetchWatchList();
        assertThat(fetchWatchList).isNotNull();
        assertThat(fetchWatchList.isEmpty()).isFalse();
        assertThat(fetchWatchList.size()).isEqualTo(1);

        final WatchListEntry deathNoteRewrite = fetchWatchList.get(0);
        assertThat(deathNoteRewrite).isNotNull();
        assertThat(deathNoteRewrite.getInfoLink().getUrl()).isEqualTo("https://myanimelist.net/anime/2994");
        assertThat(deathNoteRewrite.getThumbnail()).isEqualTo("https://myanimelist.cdn-dena.com/images/anime/13/8518t.jpg");
        assertThat(deathNoteRewrite.getTitle()).isEqualTo("Death Note Rewrite");
    }


    @Test(groups = UNIT_TEST_GROUP)
    public void testThatFilterListIsParsedCorrectly() throws SAXException, ParserConfigurationException, IOException {
        // given

        // when
        xmlImporter.importFile(file);

        // then
        final List<FilterEntry> fetchFilterList = inMemoryPersistenceHandler.fetchFilterList();
        assertThat(fetchFilterList).isNotNull();
        assertThat(fetchFilterList.isEmpty()).isFalse();
        assertThat(fetchFilterList.size()).isEqualTo(1);

        final FilterEntry gintama = fetchFilterList.get(0);
        assertThat(gintama).isNotNull();
        assertThat(gintama.getInfoLink().getUrl()).isEqualTo("https://myanimelist.net/anime/918");
        assertThat(gintama.getTitle()).isEqualTo("Gintama");
    }
}