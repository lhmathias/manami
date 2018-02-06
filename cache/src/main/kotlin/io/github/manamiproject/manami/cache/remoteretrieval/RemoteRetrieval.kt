package io.github.manamiproject.manami.cache.remoteretrieval

import io.github.manamiproject.manami.cache.AnimeRetrieval
import io.github.manamiproject.manami.cache.remoteretrieval.extractor.Extractors
import io.github.manamiproject.manami.cache.Cache
import io.github.manamiproject.manami.cache.remoteretrieval.headlessbrowser.HeadlessBrowserStrategies
import io.github.manamiproject.manami.common.LoggerDelegate
import io.github.manamiproject.manami.dto.entities.Anime
import io.github.manamiproject.manami.dto.entities.InfoLink
import io.github.manamiproject.manami.dto.entities.RecommendationList
import org.slf4j.Logger

internal class RemoteRetrieval(
        private val extractors: Extractors
) : AnimeRetrieval {

    private val log: Logger by LoggerDelegate()

    override fun fetchAnime(infoLink: InfoLink): Anime? {
        log.debug("Starting remote retrieval of anime data for [{}]", infoLink)

        extractors.getAnimeExtractorFor(infoLink)?.let { extractor ->
            HeadlessBrowserStrategies.getHeadlessBrowserFor(infoLink)?.let {headlessBrowser ->
                return extractor.extractAnime(headlessBrowser.downloadSite(infoLink))
            }
        }

        return null
    }

    override fun fetchRelatedAnime(infoLink: InfoLink): Set<InfoLink> {
        log.debug("Starting remote retrieval of related anime for [{}]", infoLink)

        extractors.getRelatedAnimeExtractorFor(infoLink)?.let { extractor ->
            HeadlessBrowserStrategies.getHeadlessBrowserFor(infoLink)?.let {headlessBrowser ->
                return extractor.extractRelatedAnime(headlessBrowser.downloadSite(infoLink))
            }
        }

        return mutableSetOf()
    }

    override fun fetchRecommendations(infoLink: InfoLink): RecommendationList {
        log.debug("Starting remote retrieval of recommendations for [{}]", infoLink)

        extractors.getRecommendationsExtractorFor(infoLink)?.let { extractor ->
            HeadlessBrowserStrategies.getHeadlessBrowserFor(infoLink)?.let {headlessBrowser ->
                extractor.extractRecommendations(headlessBrowser.downloadSite(infoLink))
            }
        }

        return RecommendationList()
    }
}