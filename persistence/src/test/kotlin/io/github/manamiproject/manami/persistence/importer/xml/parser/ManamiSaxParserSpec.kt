package io.github.manamiproject.manami.persistence.importer.xml.parser

import io.github.manamiproject.manami.dto.AnimeType
import io.github.manamiproject.manami.dto.entities.*
import io.github.manamiproject.manami.persistence.InternalPersistence
import io.github.manamiproject.manami.persistence.importer.xml.XmlImporter
import io.github.manamiproject.manami.persistence.inmemory.InMemoryPersistence
import io.github.manamiproject.manami.persistence.inmemory.animelist.InMemoryAnimeList
import io.github.manamiproject.manami.persistence.inmemory.filterlist.InMemoryFilterList
import io.github.manamiproject.manami.persistence.inmemory.watchlist.InMemoryWatchList
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.net.URL
import java.nio.file.Path
import java.nio.file.Paths


private const val TEST_ANIME_LIST_FILE = "test_anime_list.xml"


@RunWith(JUnitPlatform::class)
class ManamiSaxParserSpec : Spek({

    val file: Path = Paths.get(this::class.java.classLoader.getResource(TEST_ANIME_LIST_FILE).toURI())

    val persistenceFacade: InternalPersistence = InMemoryPersistence(
            InMemoryAnimeList(),
            InMemoryFilterList(),
            InMemoryWatchList()
    )


    given("Importer instance for Manami") {

        val importer = XmlImporter(
                ManamiSaxParser(persistenceFacade),
                MalSaxParser(persistenceFacade)
        ).using(XmlImporter.XmlStrategy.MANAMI)

        on("importing file") {
            importer.importFile(file)

            it("must contain the same entries in the animelist as in the file") {
                val fetchAnimeList: MutableList<Anime> = persistenceFacade.fetchAnimeList()
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

            it("must contain the same entries in the filterlist as in the file") {
                val fetchFilterList: MutableList<FilterListEntry> = persistenceFacade.fetchFilterList()
                assertThat(fetchFilterList).isNotNull()
                assertThat(fetchFilterList).isNotEmpty()
                assertThat(fetchFilterList).hasSize(1)

                val gintama: FilterListEntry = fetchFilterList[0]
                assertThat(gintama).isNotNull()
                assertThat(gintama.infoLink).isEqualTo(InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}918"))
                assertThat(gintama.title).isEqualTo("Gintama")
            }

            it("must contain the same entries in the watchlist as in the file") {
                val fetchWatchList: MutableList<WatchListEntry> = persistenceFacade.fetchWatchList()
                assertThat(fetchWatchList).isNotNull()
                assertThat(fetchWatchList).isNotEmpty()
                assertThat(fetchWatchList).hasSize(1)

                val deathNoteRewrite: WatchListEntry = fetchWatchList[0]
                assertThat(deathNoteRewrite).isNotNull()
                assertThat(deathNoteRewrite.infoLink).isEqualTo(InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}2994"))
                assertThat(deathNoteRewrite.thumbnail).isEqualTo(URL("https://myanimelist.cdn-dena.com/images/anime/13/8518t.jpg"))
                assertThat(deathNoteRewrite.title).isEqualTo("Death Note Rewrite")
            }
        }
    }
})