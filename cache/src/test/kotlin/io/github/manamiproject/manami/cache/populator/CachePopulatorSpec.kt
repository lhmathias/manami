package io.github.manamiproject.manami.cache.populator

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import io.github.manamiproject.manami.cache.AnimeRetrieval
import io.github.manamiproject.manami.cache.caches.AnimeCache
import io.github.manamiproject.manami.cache.caches.RelatedAnimeCache
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

@RunWith(JUnitPlatform::class)
class CachePopulatorSpec : Spek({

    val remoteRetrievalMock = mock<AnimeRetrieval> { }
    val offlineDatabaseMock = mock<CacheEntrySource> {
        on { getAnimeCacheEntries() } doReturn mapOf(
                Pair(InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}1535"), Anime("Death note", InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}1535"))),
                Pair(InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}32769"), null)
        )

        on {getRelatedAnimeCacheEntries() } doReturn mapOf(
                Pair(
                        InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}4382"),
                        setOf(
                                InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}849"),
                                InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}7311"),
                                InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}26351")
                        )
                )
        )
    }
    val animeCache = AnimeCache(remoteRetrievalMock)
    val relatedAnimeCache = RelatedAnimeCache(remoteRetrievalMock)

    given("a CachePopulator with a mocked OfflineDatabase") {
        val cachePopulator = CachePopulator(
                animeCache,
                relatedAnimeCache,
                offlineDatabaseMock
        )

        on("populating cache") {
            cachePopulator.populate()

            it("must contain the correct anime") {
                val deathNote = animeCache.get(InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}1535"))
                assertThat(deathNote).isNotNull()
            }

            it("must return null for the unknown entry") {
                val notFoundEntry = animeCache.get(InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}32769"))
                assertThat(notFoundEntry).isNotNull()
            }

            it("must the correct list of related anime") {
                val relatedAnime = relatedAnimeCache.get(InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}4382"))
                assertThat(relatedAnime).contains(InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}849"))
                assertThat(relatedAnime).contains(InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}7311"))
                assertThat(relatedAnime).contains(InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}26351"))
            }
        }
    }
})