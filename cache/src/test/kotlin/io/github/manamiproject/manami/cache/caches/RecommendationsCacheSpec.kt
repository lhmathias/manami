package io.github.manamiproject.manami.cache.caches

import com.nhaarman.mockito_kotlin.*
import io.github.manamiproject.manami.cache.remoteretrieval.RemoteRetrieval
import io.github.manamiproject.manami.dto.entities.Anime
import io.github.manamiproject.manami.dto.entities.InfoLink
import io.github.manamiproject.manami.dto.entities.Recommendation
import io.github.manamiproject.manami.dto.entities.RecommendationList
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.mockito.Mockito
import org.mockito.Mockito.mock

class RecommendationsCacheSpec : Spek({

    given("an empty cache") {
        val remoteRetrievalMock = mock<RemoteRetrieval> {
            on {
                fetchRecommendations(isA())
            } doReturn RecommendationList().apply {
                addRecommendation(Recommendation(InfoLink("https://myanimelist.net/anime/1575"), 435))
                addRecommendation(Recommendation(InfoLink("https://myanimelist.net/anime/19"), 63))
                addRecommendation(Recommendation(InfoLink("https://myanimelist.net/anime/10620"), 58))
            }
        }

        val cache = RecommendationsCache(remoteRetrievalMock)

        on("fetching an anime from this infolink") {
            val infoLink = InfoLink("http://myanimelist.net/anime/1535")
            cache.get(infoLink)

            it("must call the remote retrieval strategy to fetch the entry, because it does not exist in the cache") {
                Mockito.verify(remoteRetrievalMock, Mockito.times(1)).fetchRecommendations(infoLink)
            }
        }
    }

    given("a cache populated with two entries") {
        val remoteRetrievalMock = mock(RemoteRetrieval::class.java)
        val cache = RecommendationsCache(remoteRetrievalMock)

        val deathNoteInfoLink = InfoLink("http://myanimelist.net/anime/1535")
        val deathNoteRecommendations = RecommendationList().apply {
            addRecommendation(Recommendation(InfoLink("https://myanimelist.net/anime/1575"), 435))
            addRecommendation(Recommendation(InfoLink("https://myanimelist.net/anime/19"), 63))
            addRecommendation(Recommendation(InfoLink("https://myanimelist.net/anime/10620"), 58))
        }
        cache.populate(deathNoteInfoLink, deathNoteRecommendations)

        val madeInAbyssInfoLink = InfoLink("http://myanimelist.net/anime/34599")
        cache.populate(madeInAbyssInfoLink, RecommendationList().apply {
                addRecommendation(Recommendation(InfoLink("https://myanimelist.net/anime/11061"), 20))
                addRecommendation(Recommendation(InfoLink("https://myanimelist.net/anime/13125"), 14))
            }
        )

        on("fetching an anime from this infolink") {
            val result: RecommendationList = cache.get(deathNoteInfoLink)

            it("must not call the remote retrieval strategy, because it the entry already resides in the cache") {
                verify(remoteRetrievalMock, never()).fetchAnime(deathNoteInfoLink)
            }

            it("result != null") {
                assertThat(result).isNotNull()
            }

            it("returns the correct entry") {
                assertThat(result).isEqualTo(deathNoteRecommendations)
            }
        }
    }
})