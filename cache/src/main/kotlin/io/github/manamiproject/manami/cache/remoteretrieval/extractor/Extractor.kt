package io.github.manamiproject.manami.cache.remoteretrieval.extractor

import io.github.manamiproject.manami.entities.InfoLink

internal interface Extractor {

    /**
     * Checks whether the current plugin can process the given link.
     *
     * @param infoLink URL
     * @return True if the extractor is responsible.
     */
    fun isResponsible(infoLink: InfoLink): Boolean
}