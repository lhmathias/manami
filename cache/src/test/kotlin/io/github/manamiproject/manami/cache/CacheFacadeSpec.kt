package io.github.manamiproject.manami.cache

import io.github.manamiproject.manami.dto.AnimeType
import io.github.manamiproject.manami.dto.entities.Anime
import io.github.manamiproject.manami.dto.entities.InfoLink
import io.github.manamiproject.manami.dto.entities.NORMALIZED_ANIME_DOMAIN
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.jupiter.api.Tag
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*


@RunWith(JUnitPlatform::class)
@Tag("integrationTest")
class CacheFacadeSpec : Spek({

    afterEachTest {
        val databaseFolder: Path = Paths.get("database")

        Files.walk(databaseFolder)
                .sorted(Comparator.reverseOrder())
                .map(Files::deleteIfExists)
    }

    given("no offline database and a valid MAL infolink") {
        val infoLink = InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}1535")

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

            it("must return the correct id") {
                assertThat(result?.id).isEqualTo(UUID.fromString("2d88de4c-9dbd-4837-b3ab-66c597c379ce"))
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
                assertThat(result.contains(InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}2994"))).isTrue()
            }
        }
    }
})