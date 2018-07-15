package io.github.manamiproject.manami.cache.remoteretrieval.headlessbrowser

import io.github.manamiproject.manami.entities.InfoLink

/**
 * Headless browser which downloads raw HTML.
 */
interface HeadlessBrowser {

    /**
     * Downloads raw HTML based on the given InfoLink.
     * @param infoLink Website which contains anime meta data.
     * @return Raw HTML.
     */
    fun downloadSite(infoLink: InfoLink): String
}