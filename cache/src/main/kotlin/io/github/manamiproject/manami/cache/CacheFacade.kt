package io.github.manamiproject.manami.cache

import io.github.manami.cache.strategies.headlessbrowser.HeadlessBrowserRetrievalStrategy
import io.github.manamiproject.manami.dto.entities.Anime
import io.github.manamiproject.manami.dto.entities.InfoLink
import io.github.manamiproject.manami.dto.entities.RecommendationList
import java.util.*
import io.github.manamiproject.manami.cache.offlinedatabase.OfflineDatabaseGitRepository
import io.github.manamiproject.manami.cache.caches.RecommendationsCache
import io.github.manamiproject.manami.cache.caches.RelatedAnimeCache
import io.github.manamiproject.manami.cache.caches.AnimeCache


object CacheFacade : Cache {

    private val headlessBrowserStrategy: HeadlessBrowserRetrievalStrategy = HeadlessBrowserRetrievalStrategy()
    private val animeEntryCache: AnimeCache = AnimeCache(headlessBrowserStrategy)
    private val relatedAnimeCache: RelatedAnimeCache = RelatedAnimeCache(headlessBrowserStrategy)
    private val recommendationsCache: RecommendationsCache = RecommendationsCache(headlessBrowserStrategy)
    private val offlineDatabaseGitRepository: OfflineDatabaseGitRepository = OfflineDatabaseGitRepository()

    init {
        offlineDatabaseGitRepository.database.animeMetaData.forEach { entry ->
            entry.sources.forEach { infoLinkUrl ->
                val infoLink = InfoLink(infoLinkUrl.toString())

                val anime = Anime(
                        entry.title,
                        infoLink,
                        entry.episodes,
                        entry.type,
                        "/",
                        entry.thumbnail,
                        entry.picture
                )

                animeEntryCache.populate(infoLink, Optional.of(anime))

                val relatedAnime: MutableSet<InfoLink> = mutableSetOf()

                entry.relations
                        .filter { url -> url.host == infoLink.url?.host }
                        .forEach { relation -> relatedAnime.add(InfoLink(relation.toString())) }

                relatedAnimeCache.populate(infoLink, relatedAnime)
            }

            offlineDatabaseGitRepository.database.deadEntries.forEach { infoLink ->
                animeEntryCache.populate(infoLink, Optional.empty())
            }
        }
    }

    override fun fetchAnime(infoLink: InfoLink): Optional<Anime> {
        if (!infoLink.isValid()) {
            return Optional.empty()
        }

        return animeEntryCache.get(infoLink)
    }

    override fun fetchRelatedAnime(infoLink: InfoLink): Set<InfoLink> {
        if (!infoLink.isValid()) {
            return mutableSetOf()
        }

        return relatedAnimeCache.get(infoLink)
    }

    override fun fetchRecommendations(infoLink: InfoLink): RecommendationList {
        if (!infoLink.isValid()) {
            return RecommendationList()
        }

        return recommendationsCache.get(infoLink)
    }
}