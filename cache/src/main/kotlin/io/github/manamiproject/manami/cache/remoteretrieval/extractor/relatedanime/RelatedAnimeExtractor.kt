package io.github.manamiproject.manami.cache.remoteretrieval.extractor.relatedanime

import io.github.manamiproject.manami.cache.remoteretrieval.extractor.Extractor
import io.github.manamiproject.manami.dto.entities.InfoLink

internal interface RelatedAnimeExtractor : Extractor {

    fun extractRelatedAnime(html: String): MutableSet<InfoLink>
}