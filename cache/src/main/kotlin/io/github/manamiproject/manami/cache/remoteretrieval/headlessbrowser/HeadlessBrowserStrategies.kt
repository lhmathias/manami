package io.github.manamiproject.manami.cache.remoteretrieval.headlessbrowser

import io.github.manamiproject.manami.cache.remoteretrieval.headlessbrowser.strategies.SimpleUrlConnectionStrategy
import io.github.manamiproject.manami.dto.entities.InfoLink
import io.github.manamiproject.manami.dto.entities.NORMALIZED_ANIME_DOMAIN

object HeadlessBrowserStrategies {

    fun getHeadlessBrowserFor(infoLink: InfoLink): HeadlessBrowser? {
        return when {
            infoLink.toString().startsWith(NORMALIZED_ANIME_DOMAIN.MAL.value) -> SimpleUrlConnectionStrategy()
            infoLink.toString().startsWith(NORMALIZED_ANIME_DOMAIN.ANIDB.value) -> SimpleUrlConnectionStrategy()
            else -> null
        }
    }
}