package io.github.manamiproject.manami.cache.caches

import com.nhaarman.mockito_kotlin.*
import io.github.manamiproject.manami.cache.remoteretrieval.RemoteRetrieval
import io.github.manamiproject.manami.entities.InfoLink
import io.github.manamiproject.manami.entities.NormalizedAnimeBaseUrls
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.mockito.Mockito.mock

object RelatedAnimeCacheSpec : Spek({

    given("an empty cache") {
        val remoteRetrievalMock = mock<RemoteRetrieval> {
            on {
                fetchRelatedAnime(isA())
            } doReturn setOf(InfoLink("${NormalizedAnimeBaseUrls.MAL.value}2994"))
        }

        val cache = RelatedAnimeCache(remoteRetrievalMock)

        on("fetching related anime for this infolink") {
            val infoLink = InfoLink("${NormalizedAnimeBaseUrls.MAL.value}1535")
            cache.get(infoLink)

            it("must call the remote retrieval strategy to fetch the entry, because it does not exist in the cache") {
                verify(remoteRetrievalMock, times(1)).fetchRelatedAnime(infoLink)
            }
        }
    }

    given("a cache populated with two entries") {
        val remoteRetrievalMock = mock(RemoteRetrieval::class.java)
        val cache = RelatedAnimeCache(remoteRetrievalMock)

        val deathNoteInfoLink = InfoLink("${NormalizedAnimeBaseUrls.MAL.value}1535")
        val deathNoteRelatedAnime = mutableSetOf(InfoLink("${NormalizedAnimeBaseUrls.MAL.value}2994"))
        cache.populate(deathNoteInfoLink, deathNoteRelatedAnime)

        val madeInAbyssInfoLink = InfoLink("${NormalizedAnimeBaseUrls.MAL.value}34599")
        cache.populate(madeInAbyssInfoLink, mutableSetOf(InfoLink("${NormalizedAnimeBaseUrls.MAL.value}36862")))

        on("fetching related anime for this infolink") {
            val result: Set<InfoLink>? = cache.get(deathNoteInfoLink)

            it("must not call the remote retrieval strategy, because it the entry already resides in the cache") {
                verify(remoteRetrievalMock, never()).fetchAnime(deathNoteInfoLink)
            }

            it("set of infolinks is not empty") {
                assertThat(result).isNotEmpty()
            }

            it("returns the correct entry") {
                assertThat(result).isEqualTo(deathNoteRelatedAnime)
            }
        }
    }
})