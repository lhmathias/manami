package io.github.manamiproject.manami.cache.caches

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.isA
import com.nhaarman.mockito_kotlin.mock
import io.github.manamiproject.manami.cache.remoteretrieval.RemoteRetrieval
import io.github.manamiproject.manami.dto.entities.InfoLink
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.mockito.Mockito

@RunWith(JUnitPlatform::class)
class RelatedAnimeCacheSpec : Spek({

    given("an empty cache") {
        val remoteRetrievalMock = mock<RemoteRetrieval> {
            on {
                fetchRelatedAnime(isA())
            } doReturn mutableSetOf(InfoLink("https://myanimelist.net/anime/2994"))
        }

        val cache = RelatedAnimeCache(remoteRetrievalMock)

        on("fetching related anime for this infolink") {
            val infoLink = InfoLink("http://myanimelist.net/anime/1535")
            cache.get(infoLink)

            it("must call the remote retrieval strategy to fetch the entry, because it does not exist in the cache") {
                Mockito.verify(remoteRetrievalMock, Mockito.times(1)).fetchRelatedAnime(infoLink)
            }
        }
    }

    given("a cache populated with two entries") {
        val remoteRetrievalMock = Mockito.mock(RemoteRetrieval::class.java)
        val cache = RelatedAnimeCache(remoteRetrievalMock)

        val deathNoteInfoLink = InfoLink("http://myanimelist.net/anime/1535")
        val deathNoteRelatedAnime = mutableSetOf(InfoLink("https://myanimelist.net/anime/2994"))
        cache.populate(deathNoteInfoLink, deathNoteRelatedAnime)

        val madeInAbyssInfoLink = InfoLink("http://myanimelist.net/anime/34599")
        cache.populate(madeInAbyssInfoLink, mutableSetOf(InfoLink("https://myanimelist.net/anime/36862")))

        on("fetching related anime for this infolink") {
            val result: Set<InfoLink> = cache.get(deathNoteInfoLink)

            it("must not call the remote retrieval strategy, because it the entry already resides in the cache") {
                Mockito.verify(remoteRetrievalMock, Mockito.never()).fetchAnime(deathNoteInfoLink)
            }

            it("set of infolinks is not empty") {
                assertThat(result).isNotEmpty
            }

            it("returns the correct entry") {
                assertThat(result).isEqualTo(deathNoteRelatedAnime)
            }
        }
    }
})