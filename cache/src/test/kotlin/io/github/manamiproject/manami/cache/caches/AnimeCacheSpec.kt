package io.github.manamiproject.manami.cache.caches

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.isA
import com.nhaarman.mockito_kotlin.mock
import io.github.manamiproject.manami.cache.remoteretrieval.RemoteRetrieval
import io.github.manamiproject.manami.dto.entities.Anime
import io.github.manamiproject.manami.dto.entities.InfoLink
import io.github.manamiproject.manami.dto.entities.NORMALIZED_ANIME_DOMAIN
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.mockito.Mockito.*


@RunWith(JUnitPlatform::class)
class AnimeCacheSpec : Spek({

    given("an empty cache") {
        val remoteRetrievalMock = mock<RemoteRetrieval> {
            on {
                fetchAnime(isA())
            } doReturn Anime("Death Note", InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}1535"))
        }

        val cache = AnimeCache(remoteRetrievalMock)

        on("fetching an anime from this infolink") {
            val infoLink = InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}1535")
            cache.get(infoLink)

            it("must call the remote retrieval strategy to fetch the entry, because it does not exist in the cache") {
                verify(remoteRetrievalMock, times(1)).fetchAnime(infoLink)
            }
        }
    }

    given("a cache populated with two entries") {
        val remoteRetrievalMock = mock(RemoteRetrieval::class.java)
        val cache = AnimeCache(remoteRetrievalMock)

        val deathNoteInfoLink = InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}1535")
        val deathNote = Anime("Death Note", deathNoteInfoLink)
        cache.populate(deathNoteInfoLink, deathNote)

        val madeInAbyssInfoLink = InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}34599")
        cache.populate(madeInAbyssInfoLink, Anime("Made in Abyss", madeInAbyssInfoLink))

        on("fetching an anime from this infolink") {
            val result: Anime? = cache.get(deathNoteInfoLink)

            it("must not call the remote retrieval strategy, because it the entry already resides in the cache") {
                verify(remoteRetrievalMock, never()).fetchAnime(deathNoteInfoLink)
            }

            it("must not return null") {
                assertThat(result).isNotNull()
            }

            it("returns the correct entry") {
                assertThat(result).isEqualTo(deathNote)
            }
        }
    }
})