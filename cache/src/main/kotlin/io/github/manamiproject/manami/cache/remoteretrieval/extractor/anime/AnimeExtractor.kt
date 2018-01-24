package io.github.manamiproject.manami.cache.remoteretrieval.extractor.anime

import io.github.manamiproject.manami.cache.remoteretrieval.extractor.Extractor
import io.github.manamiproject.manami.dto.entities.Anime

internal interface AnimeExtractor : Extractor {

    fun extractAnime(html: String): Anime?
}