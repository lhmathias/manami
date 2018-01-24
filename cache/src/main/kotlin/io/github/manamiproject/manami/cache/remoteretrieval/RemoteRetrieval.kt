package io.github.manamiproject.manami.cache.remoteretrieval

import io.github.manamiproject.manami.cache.remoteretrieval.extractor.Extractors
import io.github.manamiproject.manami.cache.Cache
import io.github.manamiproject.manami.cache.remoteretrieval.headlessbrowser.HeadlessBrowserStrategies
import io.github.manamiproject.manami.dto.entities.Anime
import io.github.manamiproject.manami.dto.entities.InfoLink
import io.github.manamiproject.manami.dto.entities.RecommendationList

internal class RemoteRetrieval(
        private val extractors: Extractors
) : Cache {

    override fun fetchAnime(infoLink: InfoLink): Anime? {
        extractors.getAnimeExtractorFor(infoLink)?.let { extractor ->
            HeadlessBrowserStrategies.getHeadlessBrowserFor(infoLink)?.let {headlessBrowser ->
                return extractor.extractAnime(headlessBrowser.downloadSite(infoLink))
            }
        }

        return null
    }

    override fun fetchRelatedAnime(infoLink: InfoLink): Set<InfoLink> {
        extractors.getRelatedAnimeExtractorFor(infoLink)?.let { extractor ->
            HeadlessBrowserStrategies.getHeadlessBrowserFor(infoLink)?.let {headlessBrowser ->
                return extractor.extractRelatedAnime(headlessBrowser.downloadSite(infoLink))
            }
        }

        return mutableSetOf()
    }

    override fun fetchRecommendations(infoLink: InfoLink): RecommendationList {
        extractors.getRecommendationsExtractorFor(infoLink)?.let { extractor ->
            HeadlessBrowserStrategies.getHeadlessBrowserFor(infoLink)?.let {headlessBrowser ->
                extractor.extractRecommendations(headlessBrowser.downloadSite(infoLink))
            }
        }

        return RecommendationList()
    }
}