package io.github.manamiproject.manami.cache.remoteretrieval.headlessbrowser

import io.github.manamiproject.manami.cache.remoteretrieval.headlessbrowser.strategies.SimpleUrlConnectionStrategy
import io.github.manamiproject.manami.entities.InfoLink
import io.github.manamiproject.manami.entities.NormalizedAnimeBaseUrls

object HeadlessBrowserStrategies {

    fun getHeadlessBrowserFor(infoLink: InfoLink): HeadlessBrowser? {
        return when {
            infoLink.toString().startsWith(NormalizedAnimeBaseUrls.MAL.value) -> SimpleUrlConnectionStrategy()
            infoLink.toString().startsWith(NormalizedAnimeBaseUrls.ANIDB.value) -> SimpleUrlConnectionStrategy()
            else -> null
        }
    }
}