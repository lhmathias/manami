package io.github.manami.persistence.importer.xml.parser

import io.github.manami.dto.AnimeType
import io.github.manami.dto.entities.Anime
import io.github.manami.dto.entities.FilterListEntry
import io.github.manami.dto.entities.InfoLink
import io.github.manami.dto.entities.WatchListEntry
import io.github.manami.persistence.InternalPersistence
import io.github.manami.persistence.importer.xml.XmlImporter
import io.github.manami.persistence.inmemory.InMemoryPersistence
import io.github.manami.persistence.inmemory.animelist.InMemoryAnimeList
import io.github.manami.persistence.inmemory.filterlist.InMemoryFilterList
import io.github.manami.persistence.inmemory.watchlist.InMemoryWatchList
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


private const val MAL_EXPORT_FILE = "mal_export.xml"


@RunWith(JUnitPlatform::class)
class MalSaxParserSpec : Spek({

    val file: Path = Paths.get(MalSaxParserSpec::class.java.classLoader.getResource(MAL_EXPORT_FILE).toURI())
    val persistenceFacade: InternalPersistence = InMemoryPersistence(
            InMemoryAnimeList(),
            InMemoryFilterList(),
            InMemoryWatchList()
    )


    given("Importer instance for MAL") {
        val importer = XmlImporter(
                ManamiSaxParser(persistenceFacade),
                MalSaxParser(persistenceFacade)
        ).using(XmlImporter.XmlStrategy.MAL)
        
        on("importing file") {
            importer.importFile(file)
            
            it("must contain the same entries in the animelist as in the file") {
                val fetchAnimeList: MutableList<Anime> = persistenceFacade.fetchAnimeList()
                assertThat(fetchAnimeList).isNotNull()
                assertThat(fetchAnimeList).isNotEmpty()
                assertThat(fetchAnimeList.size).isEqualTo(2)

                val deathNote: Anime = fetchAnimeList[0]
                assertThat(deathNote).isNotNull()
                assertThat(deathNote.episodes).isEqualTo(37)
                assertThat(deathNote.infoLink).isEqualTo(InfoLink("https://myanimelist.net/anime/1535"))
                assertThat(deathNote.location).isEqualTo("/")
                assertThat(deathNote.title).isEqualTo("Death Note")
                assertThat(deathNote.type).isEqualTo(AnimeType.TV)

                val rurouniKenshin: Anime = fetchAnimeList[1]
                assertThat(rurouniKenshin).isNotNull()
                assertThat(rurouniKenshin.episodes).isEqualTo(94)
                assertThat(rurouniKenshin.infoLink).isEqualTo(InfoLink("https://myanimelist.net/anime/45"))
                assertThat(rurouniKenshin.location).isEqualTo("/")
                assertThat(rurouniKenshin.title).isEqualTo("Rurouni Kenshin: Meiji Kenkaku Romantan")
                assertThat(rurouniKenshin.type).isEqualTo(AnimeType.TV) 
            }
            
            it("must contain the same entries in the filterlist as in the file") {
                val fetchFilterList: MutableList<FilterListEntry> = persistenceFacade.fetchFilterList()
                assertThat(fetchFilterList).isNotNull()
                assertThat(fetchFilterList).isNotEmpty()
                assertThat(fetchFilterList.size).isEqualTo(2)

                val matanteiLokiRagnarok: FilterListEntry  = fetchFilterList[0]
                assertThat(matanteiLokiRagnarok).isNotNull()
                assertThat(matanteiLokiRagnarok.infoLink).isEqualTo(InfoLink("https://myanimelist.net/anime/335"))
                assertThat(matanteiLokiRagnarok.thumbnail).isEqualTo(URL("https://myanimelist.cdn-dena.com/images/qm_50.gif"))
                assertThat(matanteiLokiRagnarok.title).isEqualTo("Matantei Loki Ragnarok")

                val saiunkokuMonogatari: FilterListEntry  = fetchFilterList[1]
                assertThat(saiunkokuMonogatari).isNotNull()
                assertThat(saiunkokuMonogatari.infoLink).isEqualTo(InfoLink("https://myanimelist.net/anime/957"))
                assertThat(saiunkokuMonogatari.thumbnail).isEqualTo(URL("https://myanimelist.cdn-dena.com/images/qm_50.gif"))
                assertThat(saiunkokuMonogatari.title).isEqualTo("Saiunkoku Monogatari")
            }
            
            it("must contain the same entries in the watchlist as in the file") {
                val fetchWatchList: MutableList<WatchListEntry> = persistenceFacade.fetchWatchList()
                assertThat(fetchWatchList).isNotNull()
                assertThat(fetchWatchList).isNotEmpty()
                assertThat(fetchWatchList.size).isEqualTo(2)

                val akatsukiNoYona: WatchListEntry = fetchWatchList[0]
                assertThat(akatsukiNoYona).isNotNull()
                assertThat(akatsukiNoYona.infoLink).isEqualTo(InfoLink("https://myanimelist.net/anime/25013"))
                assertThat(akatsukiNoYona.title).isEqualTo("Akatsuki no Yona")
                assertThat(akatsukiNoYona.thumbnail).isEqualTo(URL("https://myanimelist.cdn-dena.com/images/qm_50.gif"))

                val aldnoahZero: WatchListEntry = fetchWatchList[1]
                assertThat(aldnoahZero).isNotNull()
                assertThat(aldnoahZero.infoLink).isEqualTo(InfoLink("https://myanimelist.net/anime/27655"))
                assertThat(aldnoahZero.title).isEqualTo("Aldnoah.Zero 2nd Season")
                assertThat(aldnoahZero.thumbnail).isEqualTo(URL("https://myanimelist.cdn-dena.com/images/qm_50.gif"))
            }
        }
    }
})