package io.github.manamiproject.manami.cache.remoteretrieval.extractor.anime.mal

import io.github.manamiproject.manami.common.readAllLines
import io.github.manamiproject.manami.entities.AnimeType
import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.entities.InfoLink
import io.github.manamiproject.manami.entities.NormalizedAnimeBaseUrls
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.net.URL
import java.nio.file.Paths


object MalAnimeExtractorSpec : Spek({

    val malAnimeExtractor = MalAnimeExtractor()

    given("raw html from mal") {
        val html = StringBuilder()
        Paths.get(MalAnimeExtractorSpec::class.java.classLoader.getResource("extractor/mal/mal_anime_data.html").toURI())
        .readAllLines().map(html::append)

        on("extracting information") {
            val extractedAnime: Anime? = malAnimeExtractor.extractAnime(html.toString())

            it("must not be null") {
                assertThat(extractedAnime).isNotNull()
            }

            it("must extract the correct title") {
                assertThat(extractedAnime?.title).isEqualTo("Death Note")
            }

            it("must contain the correct infolink") {
                assertThat(extractedAnime?.infoLink).isEqualTo(InfoLink("${NormalizedAnimeBaseUrls.MAL.value}1535"))
            }

            it("must extract the correct type") {
                assertThat(extractedAnime?.type).isEqualTo(AnimeType.TV)
            }

            it("must extract the correct number of episodes") {
                assertThat(extractedAnime?.episodes).isEqualTo(37)
            }

            it("must extract the correct picture") {
                assertThat(extractedAnime?.picture).isEqualTo(URL("https://myanimelist.cdn-dena.com/images/anime/9/9453.jpg"))
            }

            it("must extract the correct thumbnail") {
                assertThat(extractedAnime?.thumbnail).isEqualTo(URL("https://myanimelist.cdn-dena.com/images/anime/9/9453t.jpg"))
            }
        }
    }

    given("a valid MAL infolink") {
        val infoLink = InfoLink("${NormalizedAnimeBaseUrls.MAL.value}1535")

        on("checking responsibility") {
            val result: Boolean = malAnimeExtractor.isResponsible(infoLink)

            it("must return true") {
                assertThat(result).isTrue()
            }
        }
    }

    given("a valid ANIDB infolink") {
        val infoLink = InfoLink("${NormalizedAnimeBaseUrls.ANIDB.value}4563")

        on("checking responsibility") {
            val result: Boolean = malAnimeExtractor.isResponsible(infoLink)

            it("must return true") {
                assertThat(result).isFalse()
            }
        }
    }
})