package io.github.manamiproject.manami.cache.remoteretrieval.headlessbrowser

import io.github.manamiproject.manami.dto.entities.InfoLink

interface HeadlessBrowser {

    fun downloadSite(infoLink: InfoLink): String
}