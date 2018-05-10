package io.github.manamiproject.manami.persistence.importer.json

import io.github.manamiproject.manami.entities.*
import io.github.manamiproject.manami.persistence.InternalPersistence
import io.github.manamiproject.manami.persistence.inmemory.InMemoryPersistence
import io.github.manamiproject.manami.persistence.inmemory.animelist.InMemoryAnimeList
import io.github.manamiproject.manami.persistence.inmemory.filterlist.InMemoryFilterList
import io.github.manamiproject.manami.persistence.inmemory.watchlist.InMemoryWatchList
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.net.URL
import java.nio.file.Path
import java.nio.file.Paths


private const val TEST_ANIME_LIST_FILE = "test_anime_list.json"


class JsonImporterSpec : Spek({

    val file: Path = Paths.get(JsonImporterSpec::class.java.classLoader.getResource(TEST_ANIME_LIST_FILE).toURI())
    val persistenceFacade: InternalPersistence = InMemoryPersistence(
            InMemoryAnimeList(),
            InMemoryFilterList(),
            InMemoryWatchList()
    )


    given("a json importer instance and a file") {
        val jsonImporter = JsonImporter(persistenceFacade)
        
        on("importing the file") {
            jsonImporter.importFile(file)

            it("must contain the same entries in the animelist") {
                val fetchAnimeList: List<Anime> = persistenceFacade.fetchAnimeList()
                assertThat(fetchAnimeList).isNotNull()
                assertThat(fetchAnimeList).isNotEmpty()
                assertThat(fetchAnimeList).hasSize(2)

                val bokuDake: Anime = fetchAnimeList[0]
                assertThat(bokuDake).isNotNull()
                assertThat(bokuDake.episodes).isEqualTo(12)
                assertThat(bokuDake.infoLink).isEqualTo(InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}31043"))
                assertThat(bokuDake.location).isEqualTo("/anime/series/boku_dake_ga_inai_machi")
                assertThat(bokuDake.title).isEqualTo("Boku dake ga Inai Machi")
                assertThat(bokuDake.type).isEqualTo(AnimeType.TV)

                val rurouniKenshin: Anime = fetchAnimeList[1]
                assertThat(rurouniKenshin).isNotNull()
                assertThat(rurouniKenshin.episodes).isEqualTo(4)
                assertThat(rurouniKenshin.infoLink).isEqualTo(InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}44"))
                assertThat(rurouniKenshin.location).isEqualTo("/anime/series/rurouni_kenshin")
                assertThat(rurouniKenshin.title).isEqualTo("Rurouni Kenshin: Meiji Kenkaku Romantan - Tsuiokuhen")
                assertThat(rurouniKenshin.type).isEqualTo(AnimeType.OVA)
            }
        
            it("must contain the same entries in the watchlist") {
                val fetchWatchList: List<WatchListEntry>  = persistenceFacade.fetchWatchList()
                assertThat(fetchWatchList).isNotNull()
                assertThat(fetchWatchList).isNotEmpty()
                assertThat(fetchWatchList).hasSize(1)

                val deathNoteRewrite: WatchListEntry = fetchWatchList[0]
                assertThat(deathNoteRewrite).isNotNull()
                assertThat(deathNoteRewrite.infoLink).isEqualTo(InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}2994"))
                assertThat(deathNoteRewrite.thumbnail).isEqualTo(URL("https://cdn.myanimelist.net/images/anime/13/8518t.jpg"))
                assertThat(deathNoteRewrite.title).isEqualTo("Death Note Rewrite")
                
            }
        
            it("must contain the same entries in the filterlist") {
                val fetchFilterList: List<FilterListEntry> = persistenceFacade.fetchFilterList()
                assertThat(fetchFilterList).isNotNull()
                assertThat(fetchFilterList).isNotEmpty()
                assertThat(fetchFilterList).hasSize(1)

                val gintama: FilterListEntry = fetchFilterList[0]
                assertThat(gintama).isNotNull()
                assertThat(gintama.infoLink).isEqualTo(InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}918"))
                assertThat(gintama.thumbnail).isEqualTo(URL("https://cdn.myanimelist.net/images/anime/2/10038t.jpg"))
                assertThat(gintama.title).isEqualTo("Gintama")
            }
        }
    }
})