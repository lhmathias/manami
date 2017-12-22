package io.github.manami.dto.entities

import java.net.URL


interface MinimalEntry {
    var title: String

    var thumbnail: URL

    var infoLink: InfoLink

    fun isValidMinimalEntry() = title.isNotBlank() && infoLink.isValid()

    companion object {
        /** Placeholder image in case no image is available. */
        val NO_IMG = URL("https://myanimelist.cdn-dena.com/images/na_series.gif")

        /** Placeholder image in case no image is available, thumbnail size. */
        val NO_IMG_THUMB = URL("https://myanimelist.cdn-dena.com/images/qm_50.gif")
    }
}
