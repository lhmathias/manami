package io.github.manamiproject.manami.persistence.exporter.json

import io.github.manamiproject.manami.common.deleteIfExists
import io.github.manamiproject.manami.common.exists
import io.github.manamiproject.manami.common.readAllLines
import io.github.manamiproject.manami.common.walk
import io.github.manamiproject.manami.entities.*
import io.github.manamiproject.manami.persistence.InternalPersistence
import io.github.manamiproject.manami.persistence.importer.xml.parser.MalSaxParserSpec
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


private const val EXPECTED_ANIME_LIST_FILE = "test_anime_list.json"
private const val EXPECTED_RECOMMENDATIONS_FILE = "test_recommendations_list.json"
private const val ANIME_LIST_EXPORT_FILE = "test_anime_list_export.json"


class JsonExporterSpec : Spek({

    var tempFolder: Path = Paths.get(".")
    var file: Path = Paths.get(".")

    val persistenceFacade: InternalPersistence = InMemoryPersistence(
            InMemoryAnimeList(),
            InMemoryFilterList(),
            InMemoryWatchList()
    )

    beforeEachTest {
        tempFolder = createTempDir().toPath()
        file = createTempFile(suffix = ANIME_LIST_EXPORT_FILE, directory = tempFolder.toFile()).toPath()
    }


    afterEachTest {
        if(tempFolder.exists()) {
            tempFolder.walk()
                    .sorted(Comparator.reverseOrder())
                    .forEach { it.deleteIfExists() }
        }
    }


    given("a JsonExporter filled with a pre-filled persistence facade") {
        val jsonExporter = JsonExporter(persistenceFacade)

        context("an animelist, a filterlist and a watchlist") {
            val bokuDake = Anime(
                    "Boku dake ga Inai Machi",
                    InfoLink("${NormalizedAnimeBaseUrls.MAL.value}31043"),
                    12,
                    AnimeType.TV,
                    "/anime/series/boku_dake_ga_inai_machi"
            )

            persistenceFacade.addAnime(bokuDake)

            val rurouniKenshin = Anime(
                    "Rurouni Kenshin: Meiji Kenkaku Romantan - Tsuiokuhen",
                    InfoLink("${NormalizedAnimeBaseUrls.MAL.value}44"),
                    4,
                    AnimeType.OVA,
                    "/anime/series/rurouni_kenshin"
            )

            persistenceFacade.addAnime(rurouniKenshin)

            val deathNoteRewrite = WatchListEntry(
                    "Death Note Rewrite",
                    InfoLink("${NormalizedAnimeBaseUrls.MAL.value}2994"),
                    URL("https://cdn.myanimelist.net/images/anime/13/8518t.jpg")
            )

            persistenceFacade.watchAnime(deathNoteRewrite)

            val gintama = FilterListEntry(
                    "Gintama",
                    InfoLink("${NormalizedAnimeBaseUrls.MAL.value}918"),
                    URL("https://cdn.myanimelist.net/images/anime/2/10038t.jpg")
            )

            persistenceFacade.filterAnime(gintama)
        }

        on("exporting the list to a file") {
            jsonExporter.save(file)

            it("must contain the same list within the file as in the persistence facade") {
                val expectedFileBuilder = StringBuilder()
                Paths.get(MalSaxParserSpec::class.java.classLoader.getResource(EXPECTED_ANIME_LIST_FILE).toURI())
                .readAllLines().map(expectedFileBuilder::append)

                val exportedFileBuilder = StringBuilder()
                file.readAllLines().map(exportedFileBuilder::append)

                assertThat(exportedFileBuilder.toString()).isEqualTo(expectedFileBuilder.toString())
            }
        }
    }


    given("a MutableList which is to be exported") {
        val jsonExporter = JsonExporter(persistenceFacade)
        val list: MutableList<Anime> = mutableListOf()

        context("a pre-defined list") {
            val bokuDake = Anime(
                    "Boku dake ga Inai Machi",
                    InfoLink("${NormalizedAnimeBaseUrls.MAL.value}31043"),
                    12,
                    AnimeType.TV,
                    "/anime/series/boku_dake_ga_inai_machi"
            )

            list.add(bokuDake)

            val rurouniKenshin = Anime(
                    "Rurouni Kenshin: Meiji Kenkaku Romantan - Tsuiokuhen",
                    InfoLink("${NormalizedAnimeBaseUrls.MAL.value}44"),
                    4,
                    AnimeType.OVA,
                    "/anime/series/rurouni_kenshin"
            )

            list.add(rurouniKenshin)
        }

        on("exporting the list to a file") {
            jsonExporter.exportList(list, file)

            it("must contain the same list within the file as in the persistence facade") {
                val expectedFileBuilder = StringBuilder()
                Paths.get(JsonExporterSpec::class.java.classLoader.getResource(EXPECTED_RECOMMENDATIONS_FILE).toURI())
                .readAllLines().map(expectedFileBuilder::append)

                val exportedFileBuilder = StringBuilder()
                file.readAllLines().map(exportedFileBuilder::append)

                assertThat(exportedFileBuilder.toString()).isEqualTo(expectedFileBuilder.toString())
            }
        }
    }
})