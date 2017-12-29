package io.github.manami.persistence.exporter.xml

import com.google.common.eventbus.EventBus
import io.github.manami.dto.AnimeType
import io.github.manami.dto.entities.Anime
import io.github.manami.dto.entities.FilterListEntry
import io.github.manami.dto.entities.InfoLink
import io.github.manami.dto.entities.WatchListEntry
import io.github.manami.persistence.PersistenceFacade
import io.github.manami.persistence.inmemory.InMemoryPersistenceHandler
import io.github.manami.persistence.inmemory.animelist.InMemoryAnimeListHandler
import io.github.manami.persistence.inmemory.filterlist.InMemoryFilterListHandler
import io.github.manami.persistence.inmemory.watchlist.InMemoryWatchListHandler
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.mockito.Mockito.mock
import org.springframework.core.io.ClassPathResource
import java.net.URL
import java.nio.charset.StandardCharsets
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

private const val TEST_ANIME_LIST_FILE = "test_anime_list.xml"
private const val ANIME_LIST_EXPORT_FILE = "test_anime_list_export.xml"

class XmlExporterSpec : Spek({

    val separator: String = FileSystems.getDefault().separator
    var tempFolder: Path = Paths.get(".")
    var file: Path = Paths.get(".")
    var resource = ClassPathResource("")
    var expected = ""
    val persistenceFacade = PersistenceFacade(
        InMemoryPersistenceHandler(
                InMemoryAnimeListHandler(),
                InMemoryFilterListHandler(),
                InMemoryWatchListHandler()
        ),
        mock(EventBus::class.java)
    )


    given("a XmlExporter filled with a pre-filled persistence facade") {
        val xmlExporter = XmlExporter(persistenceFacade)

        beforeEachTest {
            tempFolder = Files.createTempDirectory(System.currentTimeMillis().toString())
            file = Files.createFile(Paths.get("$tempFolder$separator$ANIME_LIST_EXPORT_FILE"))

            resource = ClassPathResource(TEST_ANIME_LIST_FILE)
            val expectedFileBuilder = StringBuilder()
            Files.readAllLines(resource.file.toPath(), StandardCharsets.UTF_8).map(expectedFileBuilder::append)
            expected = normalizeXml(expectedFileBuilder.toString())
        }

        afterEachTest {
            if (Files.isDirectory(tempFolder)) {
                Files.list(tempFolder).forEach(Files::delete)
                Files.delete(tempFolder)
            }
        }

        context("an animelist, a filterlist and a watchlist") {
            val bokuDake = Anime(
                "Boku dake ga Inai Machi",
                InfoLink("http://myanimelist.net/anime/31043"),
                12,
                AnimeType.TV,
                "/anime/series/boku_dake_ga_inai_machi"
            )
    
            persistenceFacade.addAnime(bokuDake)
    
            val rurouniKenshin = Anime(
                "Rurouni Kenshin: Meiji Kenkaku Romantan - Tsuiokuhen",
                InfoLink("http://myanimelist.net/anime/44"),
                4,
                AnimeType.OVA,
                "/anime/series/rurouni_kenshin"
            )
    
            persistenceFacade.addAnime(rurouniKenshin)
    
            val deathNoteRewrite = WatchListEntry(
                "Death Note Rewrite",
                InfoLink("http://myanimelist.net/anime/2994"),
                URL("http://cdn.myanimelist.net/images/anime/13/8518t.jpg")
            )
    
            persistenceFacade.watchAnime(deathNoteRewrite)
    
            val gintama = FilterListEntry(
                "Gintama",
                InfoLink("http://myanimelist.net/anime/918"),
                URL("http://cdn.myanimelist.net/images/anime/2/10038t.jpg")
            )
    
            persistenceFacade.filterAnime(gintama)
        }
        
        on("exporting the list to a file") {
            xmlExporter.exportAll(file)
            
            it("must contain the same list within the file as in the persistence facade") {
                val exportedFileBuilder = StringBuilder()
                Files.readAllLines(resource.file.toPath(), StandardCharsets.UTF_8).map(exportedFileBuilder::append)
                val actual: String = normalizeXml(exportedFileBuilder.toString())

                assertThat(expected).isEqualTo(actual)
            }
        }
    }
})

private fun normalizeXml(xmlString: String): String {
    return xmlString
        .replace("href=\\\"(.)*?animelist_transform.xsl\\\"", "href=\"\"")
        .replace("SYSTEM \\\"(.)*?animelist.dtd\\\"", "SYSTEM \"animelist.dtd\"")
        .replace("version=\\\"(.)*?\\\"", "version=\\\"\\\"")
}