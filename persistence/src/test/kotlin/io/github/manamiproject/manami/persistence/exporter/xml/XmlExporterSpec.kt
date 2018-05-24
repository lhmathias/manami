package io.github.manamiproject.manami.persistence.exporter.xml

import io.github.manamiproject.manami.common.deleteIfExists
import io.github.manamiproject.manami.common.exists
import io.github.manamiproject.manami.common.readAllLines
import io.github.manamiproject.manami.common.walk
import io.github.manamiproject.manami.entities.*
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
import java.net.URL
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

private const val TEST_ANIME_LIST_FILE = "test_anime_list.xml"
private const val ANIME_LIST_EXPORT_FILE = "test_anime_list_export.xml"

class XmlExporterSpec : Spek({

    var tempFolder: Path = Paths.get(".")
    val persistence: InternalPersistence = InMemoryPersistence(
            InMemoryAnimeList(),
            InMemoryFilterList(),
            InMemoryWatchList()
    )


    given("a XmlExporter filled with a pre-filled persistence facade") {
        val xmlExporter = XmlExporter(persistence)

        beforeEachTest {
            tempFolder = createTempDir().toPath()

        }

        afterEachTest {
            if(tempFolder.exists()) {
                tempFolder.walk()
                        .sorted(Comparator.reverseOrder())
                        .forEach(Path::deleteIfExists)
            }
        }

        context("an animelist, a filterlist and a watchlist") {
            val bokuDake = Anime(
                    "Boku dake ga Inai Machi",
                    InfoLink("${NormalizedAnimeBaseUrls.MAL.value}31043"),
                    12,
                    AnimeType.TV,
                    "/anime/series/boku_dake_ga_inai_machi"
            )

            persistence.addAnime(bokuDake)

            val rurouniKenshin = Anime(
                    "Rurouni Kenshin: Meiji Kenkaku Romantan - Tsuiokuhen",
                    InfoLink("${NormalizedAnimeBaseUrls.MAL.value}44"),
                    4,
                    AnimeType.OVA,
                    "/anime/series/rurouni_kenshin"
            )

            persistence.addAnime(rurouniKenshin)

            val deathNoteRewrite = WatchListEntry(
                    "Death Note Rewrite",
                    InfoLink("${NormalizedAnimeBaseUrls.MAL.value}2994"),
                    URL("https://myanimelist.cdn-dena.com/images/anime/13/8518t.jpg")
            )

            persistence.watchAnime(deathNoteRewrite)

            val gintama = FilterListEntry(
                    "Gintama",
                    InfoLink("${NormalizedAnimeBaseUrls.MAL.value}918"),
                    URL("https://myanimelist.cdn-dena.com/images/anime/2/10038t.jpg")
            )

            persistence.filterAnime(gintama)
        }

        on("exporting the list to an existing file") {
            val file: Path = createTempFile(suffix = ANIME_LIST_EXPORT_FILE, directory = tempFolder.toFile()).toPath()
            xmlExporter.save(file)
            
            it("must contain the same list within the file as in the persistence facade") {
                val exportedFileBuilder = StringBuilder()
                file.readAllLines().map(exportedFileBuilder::append)
                val actual: String = normalizeXml(exportedFileBuilder.toString())

                val expectedFileBuilder = StringBuilder()
                Paths.get(JsonExporterSpec::class.java.classLoader.getResource(TEST_ANIME_LIST_FILE).toURI())
                .readAllLines().map(expectedFileBuilder::append)

                val expected: String = normalizeXml(expectedFileBuilder.toString())

                assertThat(expected).isEqualTo(actual)
            }
        }

        on("exporting the list to a non-existing file") {
            val outputFile = tempFolder.resolve(Paths.get("test-output.xml"))
            xmlExporter.save(outputFile)

            it("must create the file") {
                assertThat(outputFile).exists()
                assertThat(outputFile).isRegularFile()
            }

            it("must contain the same list within the file as in the persistence facade") {
                val exportedFileBuilder = StringBuilder()
                outputFile.readAllLines().map(exportedFileBuilder::append)
                val actual: String = normalizeXml(exportedFileBuilder.toString())

                val expectedFileBuilder = StringBuilder()
                Paths.get(JsonExporterSpec::class.java.classLoader.getResource(TEST_ANIME_LIST_FILE).toURI())
                .readAllLines().map(expectedFileBuilder::append)

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