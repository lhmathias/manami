package io.github.manamiproject.manami.cache.remoteretrieval.headlessbrowser

import io.github.manamiproject.manami.cache.remoteretrieval.headlessbrowser.strategies.SimpleUrlConnectionStrategy
import io.github.manamiproject.manami.dto.entities.DOMAINS
import io.github.manamiproject.manami.dto.entities.InfoLink

object HeadlessBrowserStrategies {

    fun getHeadlessBrowserFor(infoLink: InfoLink): HeadlessBrowser? {
        return when {
            infoLink.toString().contains(DOMAINS.MAL.value) -> SimpleUrlConnectionStrategy()
            infoLink.toString().contains(DOMAINS.ANIDB.value) -> SimpleUrlConnectionStrategy()
            else -> null
        }
    }
}