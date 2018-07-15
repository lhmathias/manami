package io.github.manamiproject.manami.cache.remoteretrieval.extractor

import io.github.manamiproject.manami.entities.InfoLink

/**
 * Extracts meta data of anime.
 */
internal interface Extractor {

    /**
     * Checks whether the current plugin can process the given InfoLink.
     * @param infoLink Website containing meta data about the anime.
     * @return True if the extractor is responsible.
     */
    fun isResponsible(infoLink: InfoLink): Boolean
}