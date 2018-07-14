package io.github.manamiproject.manami.cache

import io.github.manamiproject.manami.common.deleteIfExists
import io.github.manamiproject.manami.common.exists
import io.github.manamiproject.manami.common.walk
import io.github.manamiproject.manami.entities.AnimeType
import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.entities.InfoLink
import io.github.manamiproject.manami.entities.NormalizedAnimeBaseUrls
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.jupiter.api.Tag
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

@Tag("integrationTest")
class CacheFacadeIntegrationSpec : Spek({

    beforeEachTest {
        removeOfflineDatabase()
    }


    afterEachTest {
        removeOfflineDatabase()
    }


    given("an invalid infolink") {
        val infoLink = InfoLink("abcdefgh")

        on("fetching an anime from cache using this infolink") {
            val result = CacheFacade.fetchAnime(infoLink)

            it("must be null") {
                assertThat(result).isNull()
            }
        }

        on("fetching related anime from cache using this infolink") {
            val result = CacheFacade.fetchRelatedAnime(infoLink)

            it("must be null") {
                assertThat(result).isEmpty()
            }
        }

        on("fetching recommendations from cache using this infolink") {
            val result = CacheFacade.fetchRecommendations(infoLink)

            it("must be empty") {
                assertThat(result).isEmpty()
            }
        }
    }


    given("an invalidated cache and a valid MAL infolink") {
        CacheFacade.invalidate()
        val infoLink = InfoLink("${NormalizedAnimeBaseUrls.MAL.value}1535")

        on("fetching the anime via cache") {
            val result: Anime? = CacheFacade.fetchAnime(infoLink)

            it("must not return null") {
                assertThat(result).isNotNull()
            }

            it("must return the correct title") {
                assertThat(result?.title).isEqualTo("Death Note")
            }

            it("must return the correct infolink") {
                assertThat(result?.infoLink).isEqualTo(infoLink)
            }

            it("must return the correct number of episodes") {
                assertThat(result?.episodes).isEqualTo(37)
            }

            it("must return the correct type") {
                assertThat(result?.type).isEqualTo(AnimeType.TV)
            }

            it("must return the default value for a location") {
                assertThat(result?.location).isEqualTo("/")
            }

            it("must return a newly generated id") {
                assertThat(result?.id).isNotEqualTo(UUID.fromString("2d88de4c-9dbd-4837-b3ab-66c597c379ce"))
            }
        }

        on("fetching the related anime via cache") {
            val result: Set<InfoLink> = CacheFacade.fetchRelatedAnime(infoLink)

            it("must not be empty") {
                assertThat(result).isNotEmpty()
            }

            it("must contain one entry") {
                assertThat(result).hasSize(1)
            }

            it("must contain the correct related anime") {
                assertThat(result.contains(InfoLink("${NormalizedAnimeBaseUrls.MAL.value}2994"))).isTrue()
            }
        }
    }


    given("an invalidated cache and a valid ANIDB infolink") {
        val infoLink = InfoLink("${NormalizedAnimeBaseUrls.ANIDB.value}4563")

        on("fetching the anime via cache") {
            val result: Anime? = CacheFacade.fetchAnime(infoLink)

            it("must not return null") {
                assertThat(result).isNotNull()
            }

            it("must return the correct title") {
                assertThat(result?.title).isEqualTo("Death Note")
            }

            it("must return the correct infolink") {
                assertThat(result?.infoLink).isEqualTo(infoLink)
            }

            it("must return the correct number of episodes") {
                assertThat(result?.episodes).isEqualTo(37)
            }

            it("must return the correct type") {
                assertThat(result?.type).isEqualTo(AnimeType.TV)
            }

            it("must return the default value for a location") {
                assertThat(result?.location).isEqualTo("/")
            }

            it("must return a newly generated id") {
                assertThat(result?.id).isNotEqualTo(UUID.fromString("2d88de4c-9dbd-4837-b3ab-66c597c379ce"))
            }
        }

        on("fetching the related anime via cache") {
            val result: Set<InfoLink> = CacheFacade.fetchRelatedAnime(infoLink)

            it("must not be empty") {
                assertThat(result).isNotEmpty()
            }

            it("must contain two entries") {
                assertThat(result).hasSize(2)
            }

            it("must contain the correct related anime") {
                assertThat(result.contains(InfoLink("${NormalizedAnimeBaseUrls.ANIDB.value}8147"))).isTrue()
            }

            it("must contain the correct related anime") {
                assertThat(result.contains(InfoLink("${NormalizedAnimeBaseUrls.ANIDB.value}8146"))).isTrue()
            }
        }
    }
}) {
    companion object {
        fun removeOfflineDatabase() {
            val databaseFolder: Path = Paths.get("database")

            if(databaseFolder.exists()) {
                databaseFolder.walk()
                        .sorted(Comparator.reverseOrder())
                        .forEach { it.deleteIfExists() }
            }
        }
    }
}