package io.github.manamiproject.manami.cache.remoteretrieval.extractor.anime

import io.github.manamiproject.manami.cache.remoteretrieval.extractor.Extractor
import io.github.manamiproject.manami.entities.Anime

/**
 * Extracts anime data from a source. A raw HTML page for example.
 */
internal interface AnimeExtractor : Extractor {

    /**
     * Extracts an Anime from a source String.
     * @param source Any given source. A raw HTML page for example.
     * @return Either an Anime or null if extraction was not possible.
     */
    fun extractAnime(source: String): Anime?
}