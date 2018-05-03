package io.github.manamiproject.manami.cache.remoteretrieval.extractor

import io.github.manamiproject.manami.cache.remoteretrieval.extractor.anime.AnimeExtractor
import io.github.manamiproject.manami.cache.remoteretrieval.extractor.recommendations.RecommendationsExtractor
import io.github.manamiproject.manami.cache.remoteretrieval.extractor.relatedanime.RelatedAnimeExtractor
import io.github.manamiproject.manami.entities.InfoLink

internal class Extractors(
        vararg extractor: Extractor
) {

    private val animeExtractorList: MutableList<AnimeExtractor> = mutableListOf()
    private val relatedAnimeExtractorList: MutableList<RelatedAnimeExtractor> = mutableListOf()
    private val recommendationsExtractorList: MutableList<RecommendationsExtractor> = mutableListOf()

    init {
        extractor.forEach {
            when(it) {
                is AnimeExtractor -> animeExtractorList.add(it)
                is RelatedAnimeExtractor -> relatedAnimeExtractorList.add(it)
                is RecommendationsExtractor -> recommendationsExtractorList.add(it)
            }
        }
    }

    fun getAnimeExtractorFor(infoLink: InfoLink): AnimeExtractor? {
        animeExtractorList.forEach {
            if(it.isResponsible(infoLink)) {
                return it
            }
        }

        return null
    }

    fun getRelatedAnimeExtractorFor(infoLink: InfoLink): RelatedAnimeExtractor? {
        relatedAnimeExtractorList.forEach {
            if(it.isResponsible(infoLink)) {
                return it
            }
        }

        return null
    }

    fun getRecommendationsExtractorFor(infoLink: InfoLink): RecommendationsExtractor? {
        recommendationsExtractorList.forEach {
            if(it.isResponsible(infoLink)) {
                return it
            }
        }

        return null
    }
}