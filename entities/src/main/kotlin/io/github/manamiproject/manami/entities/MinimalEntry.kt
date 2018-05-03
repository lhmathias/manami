package io.github.manamiproject.manami.entities

import java.net.URL


typealias Title = String

interface MinimalEntry {
    var title: Title

    var thumbnail: URL

    var infoLink: InfoLink

    fun isValid() = title.isNotBlank() && infoLink.isValid()

    companion object {
        /** Placeholder image in case no image is available. */
        val NO_IMG = URL("https://myanimelist.cdn-dena.com/images/na_series.gif")

        /** Placeholder image in case no image is available, thumbnail size. */
        val NO_IMG_THUMB = URL("https://myanimelist.cdn-dena.com/images/qm_50.gif")
    }
}
