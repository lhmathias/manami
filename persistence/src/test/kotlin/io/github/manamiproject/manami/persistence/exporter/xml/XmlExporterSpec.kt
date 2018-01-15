package io.github.manamiproject.manami.persistence.exporter.xml

import io.github.manamiproject.manami.dto.AnimeType
import io.github.manamiproject.manami.dto.entities.Anime
import io.github.manamiproject.manami.dto.entities.FilterListEntry
import io.github.manamiproject.manami.dto.entities.InfoLink
import io.github.manamiproject.manami.dto.entities.WatchListEntry
import io.github.manamiproject.manami.persistence.InternalPersistence
import io.github.manamiproject.manami.persistence.exporter.json.JsonExporterSpec
import io.github.manamiproject.manami.persistence.inmemory.InMemoryPersistence
import io.github.manamiproject.manami.persistence.inmemory.animelist.InMemoryAnimeList
import io.github.manamiproject.manami.persistence.inmemory.filterlist.InMemoryFilterList
import io.github.manamiproject.manami.persistence.inmemory.watchlist.InMemoryWatchList
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.net.URL
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

private const val TEST_ANIME_LIST_FILE = "test_anime_list.xml"
private const val ANIME_LIST_EXPORT_FILE = "test_anime_list_export.xml"

@RunWith(JUnitPlatform::class)
class XmlExporterSpec : Spek({

    var tempFolder: Path = Paths.get(".")
    var file: Path = Paths.get(".")
    val persistence: InternalPersistence = InMemoryPersistence(
            InMemoryAnimeList(),
            InMemoryFilterList(),
            InMemoryWatchList()
    )


    given("a XmlExporter filled with a pre-filled persistence facade") {
        val xmlExporter = XmlExporter(persistence)

        beforeEachTest {
            tempFolder = createTempDir().toPath()
            file = createTempFile(suffix = ANIME_LIST_EXPORT_FILE, directory = tempFolder.toFile()).toPath()
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
                    InfoLink("https://myanimelist.net/anime/31043"),
                    12,
                    AnimeType.TV,
                    "/anime/series/boku_dake_ga_inai_machi"
            )

            persistence.addAnime(bokuDake)

            val rurouniKenshin = Anime(
                    "Rurouni Kenshin: Meiji Kenkaku Romantan - Tsuiokuhen",
                    InfoLink("https://myanimelist.net/anime/44"),
                    4,
                    AnimeType.OVA,
                    "/anime/series/rurouni_kenshin"
            )

            persistence.addAnime(rurouniKenshin)

            val deathNoteRewrite = WatchListEntry(
                    "Death Note Rewrite",
                    InfoLink("https://myanimelist.net/anime/2994"),
                    URL("https://myanimelist.cdn-dena.com/images/anime/13/8518t.jpg")
            )

            persistence.watchAnime(deathNoteRewrite)

            val gintama = FilterListEntry(
                    "Gintama",
                    InfoLink("https://myanimelist.net/anime/918"),
                    URL("https://myanimelist.cdn-dena.com/images/anime/2/10038t.jpg")
            )

            persistence.filterAnime(gintama)
        }

        on("exporting the list to a file") {
            xmlExporter.save(file)
            
            it("must contain the same list within the file as in the persistence facade") {
                val exportedFileBuilder = StringBuilder()
                Files.readAllLines(file, StandardCharsets.UTF_8).map(exportedFileBuilder::append)
                val actual: String = normalizeXml(exportedFileBuilder.toString())

                val resource: Path =  Paths.get(JsonExporterSpec::class.java.classLoader.getResource(TEST_ANIME_LIST_FILE).toURI())
                val expectedFileBuilder = StringBuilder()
                Files.readAllLines(resource, StandardCharsets.UTF_8).map(expectedFileBuilder::append)
                val expected: String = normalizeXml(expectedFileBuilder.toString())

                assertThat(expected).isEqualTo(actual)
            }
        }
    }
})

private fun normalizeXml(xmlString: String): String {
    return xmlString
        .replace(Regex("href=\\\"(.)*?animelist_transform.xsl\\\""), "href=\"\"")
        .replace(Regex("SYSTEM \\\"(.)*?animelist.dtd\\\""), "SYSTEM \"animelist.dtd\"")
        .replace(Regex("version=\\\"(.)*?\\\""), "version=\\\"\\\"")
}