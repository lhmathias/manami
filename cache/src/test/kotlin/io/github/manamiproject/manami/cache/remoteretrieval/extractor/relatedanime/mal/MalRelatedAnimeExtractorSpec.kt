package io.github.manamiproject.manami.cache.remoteretrieval.extractor.relatedanime.mal

import io.github.manamiproject.manami.cache.remoteretrieval.extractor.recommendations.mal.MalRecommendationsExtractorSpec
import io.github.manamiproject.manami.dto.entities.InfoLink
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class MalRelatedAnimeExtractorSpec : Spek({

    val malRelatedAnimeExtractor = MalRelatedAnimeExtractor()

    given("raw html with related anime") {
        val html = StringBuilder()
        val htmlFile: Path = Paths.get(MalRecommendationsExtractorSpec::class.java.classLoader.getResource("mal/mal_related_anime.html").toURI())
        Files.readAllLines(htmlFile, StandardCharsets.UTF_8).map(html::append)

        on("extracting related anime") {
            val relatedAnime: MutableSet<InfoLink> = malRelatedAnimeExtractor.extractRelatedAnime(html.toString())

            it("must return a list with three titles") {
                assertThat(relatedAnime).hasSize(3)
            }

            it("must contain parent story") {
                assertThat(relatedAnime).contains(InfoLink("https://myanimelist.net/anime/849"))
            }

            it("must contain sequel") {
                assertThat(relatedAnime).contains(InfoLink("https://myanimelist.net/anime/7311"))
            }

            it("must contain spin-off") {
                assertThat(relatedAnime).contains(InfoLink("https://myanimelist.net/anime/26351"))
            }
        }
    }

    given("raw html without any relations") {
        val html = StringBuilder()
        val htmlFile: Path = Paths.get(MalRecommendationsExtractorSpec::class.java.classLoader.getResource("mal/mal_no_relations.html").toURI())
        Files.readAllLines(htmlFile, StandardCharsets.UTF_8).map(html::append)

        on("extracting related anime") {
            val relatedAnime: MutableSet<InfoLink> = malRelatedAnimeExtractor.extractRelatedAnime(html.toString())

            it("must return an empty list") {
                assertThat(relatedAnime).isEmpty()
            }
        }
    }

    given("raw html without any related anime, but one adaption entry") {
        val html = StringBuilder()
        val htmlFile: Path = Paths.get(MalRecommendationsExtractorSpec::class.java.classLoader.getResource("mal/mal_no_related_anime_but_one_adaption.html").toURI())
        Files.readAllLines(htmlFile, StandardCharsets.UTF_8).map(html::append)

        on("extracting related anime") {
            val relatedAnime: MutableSet<InfoLink> = malRelatedAnimeExtractor.extractRelatedAnime(html.toString())

            it("must return an empty list") {
                assertThat(relatedAnime).isEmpty()
            }
        }
    }
})