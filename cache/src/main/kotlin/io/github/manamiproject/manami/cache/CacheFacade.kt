package io.github.manamiproject.manami.cache

import io.github.manamiproject.manami.dto.entities.Anime
import io.github.manamiproject.manami.dto.entities.InfoLink
import io.github.manamiproject.manami.dto.entities.RecommendationList
import io.github.manamiproject.manami.cache.offlinedatabase.OfflineDatabaseGitRepository
import io.github.manamiproject.manami.cache.caches.RecommendationsCache
import io.github.manamiproject.manami.cache.caches.RelatedAnimeCache
import io.github.manamiproject.manami.cache.caches.AnimeCache
import io.github.manamiproject.manami.cache.remoteretrieval.RemoteRetrieval
import io.github.manamiproject.manami.cache.remoteretrieval.extractor.Extractors
import io.github.manamiproject.manami.cache.remoteretrieval.extractor.anime.mal.MalAnimeExtractor


object CacheFacade : Cache {
    private val remoteRetrieval = RemoteRetrieval(
        Extractors(
            MalAnimeExtractor()
        )
    )
    private val animeEntryCache: AnimeCache = AnimeCache(remoteRetrieval)
    private val relatedAnimeCache: RelatedAnimeCache = RelatedAnimeCache(remoteRetrieval)
    private val recommendationsCache: RecommendationsCache = RecommendationsCache(remoteRetrieval)
    private val offlineDatabaseGitRepository: OfflineDatabaseGitRepository = OfflineDatabaseGitRepository()

    init {
        offlineDatabaseGitRepository.database.animeMetaData.forEach { entry ->
            entry.sources.forEach { infoLinkUrl ->
                val infoLink = InfoLink(infoLinkUrl)

                val anime = Anime(
                        entry.title,
                        infoLink,
                        entry.episodes,
                        entry.type,
                        "/",
                        entry.thumbnail,
                        entry.picture
                )

                animeEntryCache.populate(infoLink, anime)

                val relatedAnime: MutableSet<InfoLink> = mutableSetOf()

                entry?.relations
                        ?.filter { url -> url.host == infoLink.url?.host }
                        ?.forEach { relation -> relatedAnime.add(InfoLink(relation)) }

                relatedAnimeCache.populate(infoLink, relatedAnime)
            }
        }

        offlineDatabaseGitRepository.database.deadEntries.forEach { infoLink ->
            animeEntryCache.populate(infoLink, null)
        }
    }

    override fun fetchAnime(infoLink: InfoLink): Anime? {
        if (!infoLink.isValid()) {
            return null
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