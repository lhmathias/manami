package io.github.manami.persistence.importer.xml.postprocessor

import com.google.common.eventbus.Subscribe
import io.github.manami.common.EventBus
import io.github.manami.dto.entities.Anime
import io.github.manami.dto.entities.FilterListEntry
import io.github.manami.dto.entities.InfoLink
import io.github.manami.dto.entities.WatchListEntry
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.net.URL


private class EventBusListener {

    var receivedFileImportExceptionEvent = 0

    @Subscribe
    fun listen(obj: FileImportExceptionEvent) {
        receivedFileImportExceptionEvent++
    }
}


@RunWith(JUnitPlatform::class)
class ImportMigrationPostProcessorSpec : Spek({

    val eventBusListener = EventBusListener()
    EventBus.register(eventBusListener)

    beforeEachTest {
        eventBusListener.receivedFileImportExceptionEvent = 0
    }

    given("an invalid document version") {
        val processor = ImportMigrationPostProcessor

        on("start processing") {
            processor.process(
                ImportDocument(
                    "2.?.2",
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf()
                )
            )

            it("must fire a FileImportExceptionEvent") {
                assertThat(eventBusListener.receivedFileImportExceptionEvent).isOne()
            }
        }
    }


    given("a document version newer than 2.10.3 containing an entry from before 2.10.3") {
        val thumbnail = URL("http://cdn.myanimelist.net/images/anime/2/10038t.jpg")
        val filterList: MutableList<FilterListEntry> = mutableListOf(
            FilterListEntry(
                "Gintama",
                InfoLink("https://myanimelist.net/anime/918"),
                thumbnail
            )
        )
        val processor = ImportMigrationPostProcessor

        on("processing the document") {
            processor.process(
                ImportDocument(
                    "2.10.4",
                    mutableListOf(),
                    filterList,
                    mutableListOf()
                )
            )

            it("must not change the value of the thumbnail. The document version is newer so the process will be skipped.") {
                assertThat(filterList[0].thumbnail).isEqualTo(thumbnail)
            }
        }
    }


    given("a document version less than 2.10.3") {
        val filterList: MutableList<FilterListEntry> = mutableListOf()
        val watchList: MutableList<WatchListEntry> = mutableListOf()
        val processor = ImportMigrationPostProcessor

        context("filter and watch list entries with old cdn picture urls, other picture urls and the new cdn url") {
            filterList.apply {
                add(
                    FilterListEntry(
                        "Gintama",
                        InfoLink("https://myanimelist.net/anime/918"),
                        URL("http://cdn.myanimelist.net/images/anime/2/10038t.jpg")
                    )
                )

                add(
                    FilterListEntry(
                        "Haikyuu!! Second Season",
                        InfoLink("http://anidb.net/a10981"),
                        URL("http://img7.anidb.net/pics/anime/177031.jpg")
                    )
                )

                add(
                    FilterListEntry(
                        "Hajime no Ippo",
                        InfoLink("https://myanimelist.net/anime/263")
                    )
                )

                add(
                    FilterListEntry(
                        "Code Geass: Hangyaku no Lelouch R2",
                        InfoLink("https://myanimelist.net/anime/33486"),
                        URL("https://myanimelist.cdn-dena.com/images/anime/12/85221t.jpg")
                    )
                )

                watchList.apply {
                    add(
                        WatchListEntry(
                            "Kimi no Na wa.",
                            InfoLink("https://myanimelist.net/anime/32281"),
                            URL("http://cdn.myanimelist.net/images/anime/5/87048t.jpg")
                        )
                    )

                    add(
                        WatchListEntry(
                            "Eiga Koe no Katachi",
                            InfoLink("http://anidb.net/a10937"),
                            URL("http://img7.anidb.net/pics/anime/188217.jpg")
                        )
                    )

                    add(
                        WatchListEntry(
                            "Clannad: After Story",
                            InfoLink("https://myanimelist.net/anime/4181")
                        )
                    )

                    add(
                        WatchListEntry(
                            "Made in Abyss",
                            InfoLink("https://myanimelist.net/anime/34599"),
                            URL("https://myanimelist.cdn-dena.com/images/anime/6/86733.jpg")
                        )
                    )
                }
            }
        }

        on("processing the document") {
            processor.process(
                ImportDocument(
                    "2.10.2",
                    mutableListOf(),
                    filterList,
                    watchList
                )
            )

            it("must change only change the old MAL picture links in filterlist") {
                assertThat(filterList[0].thumbnail).isEqualTo(URL("https://myanimelist.cdn-dena.com/images/anime/2/10038t.jpg"))
                assertThat(filterList[1].thumbnail).isEqualTo(URL("http://img7.anidb.net/pics/anime/177031.jpg"))
                assertThat(filterList[2].thumbnail).isEqualTo(URL("https://myanimelist.cdn-dena.com/images/qm_50.gif"))
                assertThat(filterList[3].thumbnail).isEqualTo(URL("https://myanimelist.cdn-dena.com/images/anime/12/85221t.jpg"))
            }

            it("must change only change the old MAL picture links in watchlist") {
                assertThat(watchList[0].thumbnail).isEqualTo(URL("https://myanimelist.cdn-dena.com/images/anime/5/87048t.jpg"))
                assertThat(watchList[1].thumbnail).isEqualTo(URL("http://img7.anidb.net/pics/anime/188217.jpg"))
                assertThat(watchList[2].thumbnail).isEqualTo(URL("https://myanimelist.cdn-dena.com/images/qm_50.gif"))
                assertThat(watchList[3].thumbnail).isEqualTo(URL("https://myanimelist.cdn-dena.com/images/anime/6/86733.jpg"))
            }
        }
    }


    given("a document version newer than 2.14.2 and entries older than 2.14.2") {
        val animeList: MutableList<Anime> = mutableListOf()
        val filterList: MutableList<FilterListEntry> = mutableListOf()
        val watchList: MutableList<WatchListEntry> = mutableListOf()
        val processor = ImportMigrationPostProcessor

        context("MAL entries with and without http and www and anidb link") {

            animeList.apply {
                add(
                        Anime(
                                "Suzumiya Haruhi no Shoushitsu",
                                InfoLink("http://myanimelist.net/anime/7311")
                        )
                )
            }

            filterList.apply {
                add(
                        FilterListEntry(
                                "Gintama",
                                InfoLink("http://myanimelist.net/anime/918")
                        )
                )
            }

            watchList.apply {
                add(
                        WatchListEntry(
                                "Kimi no Na wa.",
                                InfoLink("http://myanimelist.net/anime/32281")
                        )
                )
            }
        }

        on("processing the document") {
            processor.process(
                    ImportDocument(
                            "2.14.3",
                            animeList,
                            filterList,
                            watchList
                    )
            )

            it("must not change the value in animelist, because the processor has to be skipped due to the newer version.") {
                assertThat(animeList[0].infoLink).isEqualTo(InfoLink("http://myanimelist.net/anime/7311"))
            }

            it("must not change the value in filterlist, because the processor has to be skipped due to the newer version.") {
                assertThat(filterList[0].infoLink).isEqualTo(InfoLink("http://myanimelist.net/anime/918"))
            }

            it("must not change the value in watchlist, because the processor has to be skipped due to the newer version.") {
                assertThat(watchList[0].infoLink).isEqualTo(InfoLink("http://myanimelist.net/anime/32281"))
            }
        }
    }
    

    given("a document version less than 2.14.2") {
        val animeList: MutableList<Anime> = mutableListOf()
        val filterList: MutableList<FilterListEntry> = mutableListOf()
        val watchList: MutableList<WatchListEntry> = mutableListOf()
        val processor = ImportMigrationPostProcessor

        context("MAL entries with and without http and www and anidb link") {

            animeList.apply {
                add(
                        Anime(
                                "Suzumiya Haruhi no Shoushitsu",
                                InfoLink("http://myanimelist.net/anime/7311")
                        )
                )

                add(
                        Anime(
                                "Natsume Yuujinchou Shi",
                                InfoLink("http://anidb.net/a8667")
                        )
                )

                add(
                        Anime(
                                "Death Note",
                                InfoLink("https://www.myanimelist.net/anime/1535")
                        )
                )

                add(
                        Anime(
                                "Boku dake ga Inai Machi",
                                InfoLink("https://myanimelist.net/anime/31043")
                        )
                )
            }

            filterList.apply {
                add(
                        FilterListEntry(
                                "Gintama",
                                InfoLink("http://myanimelist.net/anime/918")
                        )
                )

                add(
                        FilterListEntry(
                                "Haikyuu!! Second Season",
                                InfoLink("http://anidb.net/a10981")
                        )
                )

                add(
                        FilterListEntry(
                                "Hajime no Ippo",
                                InfoLink("http://www.myanimelist.net/anime/263")
                        )
                )

                add(
                        FilterListEntry(
                                "Code Geass: Hangyaku no Lelouch R2",
                                InfoLink("https://myanimelist.net/anime/33486")
                        )
                )

                watchList.apply {
                    add(
                            WatchListEntry(
                                    "Kimi no Na wa.",
                                    InfoLink("http://myanimelist.net/anime/32281")
                            )
                    )

                    add(
                            WatchListEntry(
                                    "Eiga Koe no Katachi",
                                    InfoLink("http://anidb.net/a10937")
                            )
                    )

                    add(
                            WatchListEntry(
                                    "Clannad: After Story",
                                    InfoLink("http://www.myanimelist.net/anime/4181")
                            )
                    )

                    add(
                            WatchListEntry(
                                    "Made in Abyss",
                                    InfoLink("https://myanimelist.net/anime/34599")
                            )
                    )
                }
            }
        }

        on("processing the document") {
            processor.process(
                    ImportDocument(
                            "2.14.1",
                            animeList,
                            filterList,
                            watchList
                    )
            )

            it("must change only change the old MAL picture links in animelist") {
                assertThat(animeList[0].infoLink).isEqualTo(InfoLink("https://myanimelist.net/anime/7311"))
                assertThat(animeList[1].infoLink).isEqualTo(InfoLink("http://anidb.net/a8667"))
                assertThat(animeList[2].infoLink).isEqualTo(InfoLink("https://www.myanimelist.net/anime/1535"))
                assertThat(animeList[3].infoLink).isEqualTo(InfoLink("https://myanimelist.net/anime/31043"))
            }

            it("must change only change the old MAL picture links in filterlist") {
                assertThat(filterList[0].infoLink).isEqualTo(InfoLink("https://myanimelist.net/anime/918"))
                assertThat(filterList[1].infoLink).isEqualTo(InfoLink("http://anidb.net/a10981"))
                assertThat(filterList[2].infoLink).isEqualTo(InfoLink("https://www.myanimelist.net/anime/263"))
                assertThat(filterList[3].infoLink).isEqualTo(InfoLink("https://myanimelist.net/anime/33486"))
            }

            it("must change only change the old MAL picture links in watchlist") {
                assertThat(watchList[0].infoLink).isEqualTo(InfoLink("https://myanimelist.net/anime/32281"))
                assertThat(watchList[1].infoLink).isEqualTo(InfoLink("http://anidb.net/a10937"))
                assertThat(watchList[2].infoLink).isEqualTo(InfoLink("https://www.myanimelist.net/anime/4181"))
                assertThat(watchList[3].infoLink).isEqualTo(InfoLink("https://myanimelist.net/anime/34599"))
            }
        }
    }
})