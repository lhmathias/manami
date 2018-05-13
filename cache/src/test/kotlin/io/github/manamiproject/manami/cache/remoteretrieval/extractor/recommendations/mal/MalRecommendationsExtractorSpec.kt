package io.github.manamiproject.manami.cache.remoteretrieval.extractor.recommendations.mal

import io.github.manamiproject.manami.common.readAllLines
import io.github.manamiproject.manami.entities.InfoLink
import io.github.manamiproject.manami.entities.NORMALIZED_ANIME_DOMAIN
import io.github.manamiproject.manami.entities.RecommendationList
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

object MalRecommendationsExtractorSpec : Spek({

    val malRecommendationsExtractor = MalRecommendationsExtractor()

    given("raw html with recommendations") {
        val html = StringBuilder()
        Paths.get(MalRecommendationsExtractorSpec::class.java.classLoader.getResource("extractor/mal/mal_recommendations.html").toURI())
        .readAllLines().map(html::append)

        on("extracting recommendations") {
            val recommendationList: RecommendationList = malRecommendationsExtractor.extractRecommendations(html.toString())

            it("contains 11 titles") {
                assertThat(recommendationList).hasSize(11)
            }

            it("Contains 2 for Toki wo Kakeru Shoujo") {
                assertThat(recommendationList.get(InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}2236"))?.amount).isEqualTo(2)
            }

            it("Contains 1 for Tsuki wa Higashi ni Hi wa Nishi ni: Operation Sanctuary") {
                assertThat(recommendationList.get(InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}648"))?.amount).isOne()
            }

            it("Contains 1 for Suzumiya Haruhi no Yuuutsu (2009)") {
                assertThat(recommendationList.get(InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}4382"))?.amount).isOne()
            }

            it("Contains 1 for Ushinawareta Mirai wo Motomete") {
                assertThat(recommendationList.get(InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}21845"))?.amount).isOne()
            }

            it("Contains 1 for Boku dake ga Inai Machi") {
                assertThat(recommendationList.get(InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}31043"))?.amount).isOne()
            }

            it("Contains 1 for _Summer") {
                assertThat(recommendationList.get(InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}1692"))?.amount).isOne()
            }

            it("Contains 1 for Fire Tripper") {
                assertThat(recommendationList.get(InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}2973"))?.amount).isOne()
            }

            it("Contains 1 for Elf-ban Kakyuusei") {
                assertThat(recommendationList.get(InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}661"))?.amount).isOne()
            }

            it("Contains 1 for Kimi ga Nozomu Eien") {
                assertThat(recommendationList.get(InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}147"))?.amount).isOne()
            }

            it("Contains 1 for Kakyuusei 2: Hitomi no Naka no Shoujo-tachi") {
                assertThat(recommendationList.get(InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}510"))?.amount).isOne()
            }

            it("Contains 1 for School Days") {
                assertThat(recommendationList.get(InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}2476"))?.amount).isOne()
            }
        }
    }


    given("raw html of an entry without recommendations") {
        val html = StringBuilder()
        Paths.get(MalRecommendationsExtractorSpec::class.java.classLoader.getResource("extractor/mal/mal_no_recommendations.html").toURI())
        .readAllLines().map(html::append)

        on("extracting recommendations") {
            val recommendationList: RecommendationList = malRecommendationsExtractor.extractRecommendations(html.toString())

            it("must return an empty list") {
                assertThat(recommendationList).isEmpty()
            }
        }
    }


    given("a valid MAL infolink") {
        val infoLink = InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}1535")

        on("checking responsibility") {
            val result: Boolean = malRecommendationsExtractor.isResponsible(infoLink)

            it("must return true") {
                assertThat(result).isTrue()
            }
        }
    }


    given("a valid ANIDB infolink") {
        val infoLink = InfoLink("${NORMALIZED_ANIME_DOMAIN.ANIDB.value}4563")

        on("checking responsibility") {
            val result: Boolean = malRecommendationsExtractor.isResponsible(infoLink)

            it("must return true") {
                assertThat(result).isFalse()
            }
        }
    }
})