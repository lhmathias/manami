package io.github.manami.persistence.importer.csv

import com.google.common.eventbus.EventBus
import io.github.manami.dto.AnimeType
import io.github.manami.dto.entities.Anime
import io.github.manami.dto.entities.FilterListEntry
import io.github.manami.dto.entities.WatchListEntry
import io.github.manami.persistence.PersistenceFacade
import io.github.manami.persistence.inmemory.InMemoryPersistenceHandler
import io.github.manami.persistence.inmemory.animelist.InMemoryAnimeListHandler
import io.github.manami.persistence.inmemory.filterlist.InMemoryFilterListHandler
import io.github.manami.persistence.inmemory.watchlist.InMemoryWatchListHandler
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.mockito.Mockito
import org.springframework.core.io.ClassPathResource
import java.net.URL

private const val TEST_ANIME_LIST_FILE = "test_anime_list.csv"

class CsvImporterSpec : Spek({
    
    val persistenceFacade = PersistenceFacade(
            InMemoryPersistenceHandler(
                    InMemoryAnimeListHandler(),
                    InMemoryFilterListHandler(),
                    InMemoryWatchListHandler()
            ),
            Mockito.mock(EventBus::class.java)
    )
    
    
    given("csv importer instance") {
        val csvImporter = CsvImporter(persistenceFacade)
        
        on("importing a file") {
            csvImporter.importFile(ClassPathResource(TEST_ANIME_LIST_FILE).file.toPath())
            
            it("must result in the animelist containing all necessary entries") {
                val fetchAnimeList: List<Anime> = persistenceFacade.fetchAnimeList()
                assertThat(fetchAnimeList).isNotNull()
                assertThat(fetchAnimeList.isEmpty()).isFalse()
                assertThat(fetchAnimeList.size).isEqualTo(2)

                val bokuDake: Anime = fetchAnimeList[0]
                assertThat(bokuDake).isNotNull()
                assertThat(bokuDake.episodes).isEqualTo(12)
                assertThat(bokuDake.infoLink.url).isEqualTo(URL("https://myanimelist.net/anime/31043"))
                assertThat(bokuDake.location).isEqualTo("/anime/series/boku_dake_ga_inai_machi")
                assertThat(bokuDake.title).isEqualTo("Boku dake ga Inai Machi")
                assertThat(bokuDake.type).isEqualTo(AnimeType.TV)

                val rurouniKenshin: Anime = fetchAnimeList[1]
                assertThat(rurouniKenshin).isNotNull()
                assertThat(rurouniKenshin.episodes).isEqualTo(4)
                assertThat(rurouniKenshin.infoLink.url).isEqualTo(URL("https://myanimelist.net/anime/44"))
                assertThat(rurouniKenshin.location).isEqualTo("/anime/series/rurouni_kenshin")
                assertThat(rurouniKenshin.title).isEqualTo("Rurouni Kenshin: Meiji Kenkaku Romantan - Tsuiokuhen")
                assertThat(rurouniKenshin.type).isEqualTo(AnimeType.OVA)
            }
            
            it("must result in the watchlist containing all necessary entries") {
                val fetchWatchList: List<WatchListEntry> = persistenceFacade.fetchWatchList()
                assertThat(fetchWatchList).isNotNull()
                assertThat(fetchWatchList.isEmpty()).isFalse()
                assertThat(fetchWatchList.size).isEqualTo(1)

                val deathNoteRewrite: WatchListEntry = fetchWatchList[0]
                assertThat(deathNoteRewrite).isNotNull()
                assertThat(deathNoteRewrite.infoLink.url).isEqualTo(URL("https://myanimelist.net/anime/2994"))
                assertThat(deathNoteRewrite.thumbnail).isEqualTo(URL("https://myanimelist.cdn-dena.com/images/qm_50.gif"))
                assertThat(deathNoteRewrite.title).isEqualTo("Death Note Rewrite")
            }
            
            it("must result in the filterlist containing all necessary entries") {
                val fetchFilterList: List<FilterListEntry> = persistenceFacade.fetchFilterList()
                assertThat(fetchFilterList).isNotNull()
                assertThat(fetchFilterList.isEmpty()).isFalse()
                assertThat(fetchFilterList.size).isEqualTo(1)

                val gintama: FilterListEntry = fetchFilterList[0]
                assertThat(gintama).isNotNull()
                assertThat(gintama.infoLink.url).isEqualTo(URL("https://myanimelist.net/anime/918"))
                assertThat(gintama.thumbnail).isEqualTo(URL("https://myanimelist.cdn-dena.com/images/qm_50.gif"))
                assertThat(gintama.title).isEqualTo("Gintama")
            }
        }
    }
})