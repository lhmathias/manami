package io.github.manami.persistence.exporter.json

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
import org.mockito.Mockito
import org.springframework.core.io.ClassPathResource
import java.net.URL
import java.nio.charset.StandardCharsets
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


private const val EXPECTED_ANIME_LIST_FILE = "test_anime_list.json"
private const val EXPECTED_RECOMMENDATIONS_FILE = "test_recommendations_list.json"
private const val ANIME_LIST_EXPORT_FILE = "test_anime_list_export.json"


class JsonExporterSpec : Spek({

    val separator: String = FileSystems.getDefault().separator
    var tempFolder: Path = Paths.get(".")
    var file: Path = Paths.get(".")

    val persistenceFacade = PersistenceFacade(
            InMemoryPersistenceHandler(
                    InMemoryAnimeListHandler(),
                    InMemoryFilterListHandler(),
                    InMemoryWatchListHandler()
            ),
            Mockito.mock(EventBus::class.java)
    )


    beforeEachTest {
        tempFolder = Files.createTempDirectory(System.currentTimeMillis().toString())
        file = Files.createFile(Paths.get("$tempFolder$separator$ANIME_LIST_EXPORT_FILE"))
    }


    afterEachTest {
        if (Files.isDirectory(tempFolder)) {
            Files.list(tempFolder).forEach(Files::delete)
            Files.delete(tempFolder)
        }
    }


    given("a JsonExporter filled with a pre-filled persistence facade") {
        val jsonExporter = JsonExporter(persistenceFacade)

        context("an animelist, a filterlist and a watchlist") {
            val bokuDake = Anime(
                    "Boku dake ga Inai Machi",
                    InfoLink("https://myanimelist.net/anime/31043"),
                    12,
                    AnimeType.TV,
                    "/anime/series/boku_dake_ga_inai_machi"
            )

            persistenceFacade.addAnime(bokuDake)

            val rurouniKenshin = Anime(
                    "Rurouni Kenshin: Meiji Kenkaku Romantan - Tsuiokuhen",
                    InfoLink("https://myanimelist.net/anime/44"),
                    4,
                    AnimeType.OVA,
                    "/anime/series/rurouni_kenshin"
            )

            persistenceFacade.addAnime(rurouniKenshin)

            val deathNoteRewrite = WatchListEntry(
                    "Death Note Rewrite",
                    InfoLink("https://myanimelist.net/anime/2994"),
                    URL("https://cdn.myanimelist.net/images/anime/13/8518t.jpg")
            )

            persistenceFacade.watchAnime(deathNoteRewrite)

            val gintama = FilterListEntry(
                    "Gintama",
                    InfoLink("https://myanimelist.net/anime/918"),
                    URL("https://cdn.myanimelist.net/images/anime/2/10038t.jpg")
            )

            persistenceFacade.filterAnime(gintama)
        }

        on("exporting the list to a file") {
            jsonExporter.exportAll(file)

            it("must contain the same list within the file as in the persistence facade") {
                var expectedFileBuilder = StringBuilder()
                val expectedFile = ClassPathResource(EXPECTED_ANIME_LIST_FILE)
                Files.readAllLines(expectedFile.file.toPath(), StandardCharsets.UTF_8).map(expectedFileBuilder::append)

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
                InfoLink("https://myanimelist.net/anime/31043"),
                12,
                AnimeType.TV,
            "/anime/series/boku_dake_ga_inai_machi"
            )

            list.add(bokuDake)

            val rurouniKenshin = Anime(
                "Rurouni Kenshin: Meiji Kenkaku Romantan - Tsuiokuhen",
                InfoLink("https://myanimelist.net/anime/44"),
                4,
                AnimeType.OVA,
                "/anime/series/rurouni_kenshin"
            )

            list.add(rurouniKenshin)
        }

        on("exporting the list to a file") {
            jsonExporter.exportList(list, file)

            it("must contain the same list within the file as in the persistence facade") {
                var expectedFileBuilder = StringBuilder()
                val expectedFile = ClassPathResource(EXPECTED_RECOMMENDATIONS_FILE)
                Files.readAllLines(expectedFile.file.toPath(), StandardCharsets.UTF_8).map(expectedFileBuilder::append)

                val exportedFileBuilder = StringBuilder()
                Files.readAllLines(file, StandardCharsets.UTF_8).map(exportedFileBuilder::append)

                assertThat(exportedFileBuilder.toString()).isEqualTo(expectedFileBuilder.toString())
            }
        }
    }
})