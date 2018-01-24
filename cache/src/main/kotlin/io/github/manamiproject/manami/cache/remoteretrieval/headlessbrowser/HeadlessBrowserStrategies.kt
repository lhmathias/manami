package io.github.manamiproject.manami.cache.remoteretrieval.headlessbrowser

import io.github.manamiproject.manami.dto.entities.DOMAINS
import io.github.manamiproject.manami.dto.entities.InfoLink

object HeadlessBrowserStrategies {

    fun getHeadlessBrowserFor(infoLink: InfoLink): HeadlessBrowser? {
        return when {
            infoLink.toString().contains(DOMAINS.MAL.value) -> null
            infoLink.toString().contains(DOMAINS.ANIDB.value) -> null
            else -> null
        }
    }
}