package io.github.manamiproject.manami.cache.remoteretrieval.extractor.recommendations.mal

import io.github.manamiproject.manami.dto.entities.InfoLink
import io.github.manamiproject.manami.dto.entities.RecommendationList
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@RunWith(JUnitPlatform::class)
class MalRecommendationsExtractorSpec : Spek({

    val malRecommendationsExtractor = MalRecommendationsExtractor()

    given("raw html with recommendations") {
        val html = StringBuilder()
        val htmlFile: Path = Paths.get(MalRecommendationsExtractorSpec::class.java.classLoader.getResource("mal/mal_recommendations.html").toURI())
        Files.readAllLines(htmlFile, StandardCharsets.UTF_8).map(html::append)

        on("extracting recommendations") {
            val recommendationList: RecommendationList = malRecommendationsExtractor.extractRecommendations(html.toString())

            it("contains 11 titles") {
                assertThat(recommendationList).hasSize(11)
            }

            it("Contains 2 for Toki wo Kakeru Shoujo") {
                assertThat(recommendationList.get(InfoLink("https://myanimelist.net/anime/2236"))?.amount).isEqualTo(2)
            }

            it("Contains 1 for Tsuki wa Higashi ni Hi wa Nishi ni: Operation Sanctuary") {
                assertThat(recommendationList.get(InfoLink("https://myanimelist.net/anime/648"))?.amount).isOne()
            }

            it("Contains 1 for Suzumiya Haruhi no Yuuutsu (2009)") {
                assertThat(recommendationList.get(InfoLink("https://myanimelist.net/anime/4382"))?.amount).isOne()
            }

            it("Contains 1 for Ushinawareta Mirai wo Motomete") {
                assertThat(recommendationList.get(InfoLink("https://myanimelist.net/anime/21845"))?.amount).isOne()
            }

            it("Contains 1 for Boku dake ga Inai Machi") {
                assertThat(recommendationList.get(InfoLink("https://myanimelist.net/anime/31043"))?.amount).isOne()
            }

            it("Contains 1 for _Summer") {
                assertThat(recommendationList.get(InfoLink("https://myanimelist.net/anime/1692"))?.amount).isOne()
            }

            it("Contains 1 for Fire Tripper") {
                assertThat(recommendationList.get(InfoLink("https://myanimelist.net/anime/2973"))?.amount).isOne()
            }

            it("Contains 1 for Elf-ban Kakyuusei") {
                assertThat(recommendationList.get(InfoLink("https://myanimelist.net/anime/661"))?.amount).isOne()
            }

            it("Contains 1 for Kimi ga Nozomu Eien") {
                assertThat(recommendationList.get(InfoLink("https://myanimelist.net/anime/147"))?.amount).isOne()
            }

            it("Contains 1 for Kakyuusei 2: Hitomi no Naka no Shoujo-tachi") {
                assertThat(recommendationList.get(InfoLink("https://myanimelist.net/anime/510"))?.amount).isOne()
            }

            it("Contains 1 for School Days") {
                assertThat(recommendationList.get(InfoLink("https://myanimelist.net/anime/2476"))?.amount).isOne()
            }
        }
    }


    given("raw html of an entry without recommendations") {
        val html = StringBuilder()
        val htmlFile: Path = Paths.get(MalRecommendationsExtractorSpec::class.java.classLoader.getResource("mal/mal_no_recommendations.html").toURI())
        Files.readAllLines(htmlFile, StandardCharsets.UTF_8).map(html::append)

        on("extracting recommendations") {
            val recommendationList: RecommendationList = malRecommendationsExtractor.extractRecommendations(html.toString())

            it("must return an empty list") {
                assertThat(recommendationList).isEmpty()
            }
        }
    }
})