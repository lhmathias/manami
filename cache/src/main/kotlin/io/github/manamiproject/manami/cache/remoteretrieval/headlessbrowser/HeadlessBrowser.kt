package io.github.manamiproject.manami.cache.remoteretrieval.headlessbrowser

import io.github.manamiproject.manami.entities.InfoLink

interface HeadlessBrowser {

    fun downloadSite(infoLink: InfoLink): String
}