package io.github.manamiproject.manami.cache.remoteretrieval.extractor.anime.mal

import io.github.manamiproject.manami.dto.AnimeType
import io.github.manamiproject.manami.dto.entities.Anime
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class MalAnimeExtractorSpec : Spek({

    val malAnimeExtractor = MalAnimeExtractor()

    given("raw html from mal") {
        val html = StringBuilder()
        val htmlFile: Path = Paths.get(MalAnimeExtractorSpec::class.java.classLoader.getResource("mal/mal_anime_data.html").toURI())
        Files.readAllLines(htmlFile, StandardCharsets.UTF_8).map(html::append)

        on("extracting information") {
            val extractedAnime: Anime? = malAnimeExtractor.extractAnime(html.toString())

            it("must not be null") {
                assertThat(extractedAnime).isNotNull()
            }

            it("must extract the correct title") {
                assertThat(extractedAnime?.title).isEqualTo("Death Note")
            }

            it("must contain the correct infolink") {
                assertThat(extractedAnime?.infoLink).isEqualTo("http://myanimelist.net/anime/1535")
            }

            it("must extract the correct type") {
                assertThat(extractedAnime?.type).isEqualTo(AnimeType.TV)
            }

            it("must extract the correct number of episodes") {
                assertThat(extractedAnime?.episodes).isEqualTo(37)
            }

            it("must extract the correct picture") {
                assertThat(extractedAnime?.picture).isEqualTo("https://myanimelist.cdn-dena.com/images/anime/9/9453.jpg")
            }

            it("must extract the correct thumbnail") {
                assertThat(extractedAnime?.thumbnail).isEqualTo("https://myanimelist.cdn-dena.com/images/anime/9/9453t.jpg")
            }
        }
    }
})