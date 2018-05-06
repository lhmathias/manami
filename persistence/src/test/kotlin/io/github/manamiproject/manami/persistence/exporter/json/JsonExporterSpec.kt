package io.github.manamiproject.manami.persistence.exporter.json

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
import java.nio.charset.StandardCharsets
import java.nio.file.Files
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
        if(Files.exists(tempFolder)) {
            Files.walk(tempFolder)
                    .sorted(Comparator.reverseOrder())
                    .forEach(Files::delete)
        }
    }


    given("a JsonExporter filled with a pre-filled persistence facade") {
        val jsonExporter = JsonExporter(persistenceFacade)

        context("an animelist, a filterlist and a watchlist") {
            val bokuDake = Anime(
                    "Boku dake ga Inai Machi",
                    InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}31043"),
                    12,
                    AnimeType.TV,
                    "/anime/series/boku_dake_ga_inai_machi"
            )

            persistenceFacade.addAnime(bokuDake)

            val rurouniKenshin = Anime(
                    "Rurouni Kenshin: Meiji Kenkaku Romantan - Tsuiokuhen",
                    InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}44"),
                    4,
                    AnimeType.OVA,
                    "/anime/series/rurouni_kenshin"
            )

            persistenceFacade.addAnime(rurouniKenshin)

            val deathNoteRewrite = WatchListEntry(
                    "Death Note Rewrite",
                    InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}2994"),
                    URL("https://cdn.myanimelist.net/images/anime/13/8518t.jpg")
            )

            persistenceFacade.watchAnime(deathNoteRewrite)

            val gintama = FilterListEntry(
                    "Gintama",
                    InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}918"),
                    URL("https://cdn.myanimelist.net/images/anime/2/10038t.jpg")
            )

            persistenceFacade.filterAnime(gintama)
        }

        on("exporting the list to a file") {
            jsonExporter.save(file)

            it("must contain the same list within the file as in the persistence facade") {
                val expectedFileBuilder = StringBuilder()
                val expectedFile: Path = Paths.get(MalSaxParserSpec::class.java.classLoader.getResource(EXPECTED_ANIME_LIST_FILE).toURI())

                Files.readAllLines(expectedFile, StandardCharsets.UTF_8).map(expectedFileBuilder::append)

                val exportedFileBuilder = StringBuilder()
                Files.readAllLines(file, StandardCharsets.UTF_8).map(exportedFileBuilder::append)

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
                    InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}31043"),
                    12,
                    AnimeType.TV,
                    "/anime/series/boku_dake_ga_inai_machi"
            )

            list.add(bokuDake)

            val rurouniKenshin = Anime(
                    "Rurouni Kenshin: Meiji Kenkaku Romantan - Tsuiokuhen",
                    InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}44"),
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
                val expectedFile: Path =  Paths.get(JsonExporterSpec::class.java.classLoader.getResource(EXPECTED_RECOMMENDATIONS_FILE).toURI())

                Files.readAllLines(expectedFile, StandardCharsets.UTF_8).map(expectedFileBuilder::append)

                val exportedFileBuilder = StringBuilder()
                Files.readAllLines(file, StandardCharsets.UTF_8).map(exportedFileBuilder::append)

                assertThat(exportedFileBuilder.toString()).isEqualTo(expectedFileBuilder.toString())
            }
        }
    }
})