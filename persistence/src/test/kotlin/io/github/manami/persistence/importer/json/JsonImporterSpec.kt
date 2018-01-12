package io.github.manami.persistence.importer.json

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
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.core.io.ClassPathResource
import java.net.URL


private const val TEST_ANIME_LIST_FILE = "test_anime_list.json"


@RunWith(JUnitPlatform::class)
class JsonImporterSpec : Spek({

    val file = ClassPathResource(TEST_ANIME_LIST_FILE).file.toPath()
    val persistenceFacade = PersistenceFacade(
            InMemoryPersistenceHandler(
                    InMemoryAnimeListHandler(),
                    InMemoryFilterListHandler(),
                    InMemoryWatchListHandler()
            ),
            Mockito.mock(EventBus::class.java)
    )
    
    given("a json importer instance and a file") {
        val jsonImporter = JsonImporter(persistenceFacade)
        
        on("importing the file") {
            jsonImporter.importFile(file)

            it("must contain the same entries in the animelist") {
                val fetchAnimeList: MutableList<Anime> = persistenceFacade.fetchAnimeList()
                assertThat(fetchAnimeList).isNotNull()
                assertThat(fetchAnimeList).isNotEmpty()
                assertThat(fetchAnimeList.size).isEqualTo(2)

                val bokuDake: Anime = fetchAnimeList[0]
                assertThat(bokuDake).isNotNull()
                assertThat(bokuDake.episodes).isEqualTo(12)
                assertThat(bokuDake.infoLink).isEqualTo(InfoLink("https://myanimelist.net/anime/31043"))
                assertThat(bokuDake.location).isEqualTo("/anime/series/boku_dake_ga_inai_machi")
                assertThat(bokuDake.title).isEqualTo("Boku dake ga Inai Machi")
                assertThat(bokuDake.type).isEqualTo(AnimeType.TV)

                val rurouniKenshin: Anime = fetchAnimeList[1]
                assertThat(rurouniKenshin).isNotNull()
                assertThat(rurouniKenshin.episodes).isEqualTo(4)
                assertThat(rurouniKenshin.infoLink).isEqualTo(InfoLink ("https://myanimelist.net/anime/44"))
                assertThat(rurouniKenshin.location).isEqualTo("/anime/series/rurouni_kenshin")
                assertThat(rurouniKenshin.title).isEqualTo("Rurouni Kenshin: Meiji Kenkaku Romantan - Tsuiokuhen")
                assertThat(rurouniKenshin.type).isEqualTo(AnimeType.OVA)
            }
        
            it("must contain the same entries in the watchlist") {
                val fetchWatchList: MutableList<WatchListEntry>  = persistenceFacade.fetchWatchList()
                assertThat(fetchWatchList).isNotNull()
                assertThat(fetchWatchList).isNotEmpty()
                assertThat(fetchWatchList.size).isOne()

                val deathNoteRewrite: WatchListEntry = fetchWatchList[0]
                assertThat(deathNoteRewrite).isNotNull()
                assertThat(deathNoteRewrite.infoLink).isEqualTo(InfoLink("https://myanimelist.net/anime/2994"))
                assertThat(deathNoteRewrite.thumbnail).isEqualTo(URL("https://cdn.myanimelist.net/images/anime/13/8518t.jpg"))
                assertThat(deathNoteRewrite.title).isEqualTo("Death Note Rewrite")
                
            }
        
            it("must contain the same entries in the filterlist") {
                val fetchFilterList: MutableList<FilterListEntry> = persistenceFacade.fetchFilterList()
                assertThat(fetchFilterList).isNotNull()
                assertThat(fetchFilterList).isNotEmpty()
                assertThat(fetchFilterList.size).isOne()

                val gintama: FilterListEntry = fetchFilterList[0]
                assertThat(gintama).isNotNull()
                assertThat(gintama.infoLink).isEqualTo(InfoLink("https://myanimelist.net/anime/918"))
                assertThat(gintama.thumbnail).isEqualTo(URL("https://cdn.myanimelist.net/images/anime/2/10038t.jpg"))
                assertThat(gintama.title).isEqualTo("Gintama")
            }
        }
    }
})