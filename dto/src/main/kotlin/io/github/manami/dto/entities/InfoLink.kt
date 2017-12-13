package io.github.manami.dto.entities

import org.apache.commons.validator.routines.UrlValidator
import java.net.MalformedURLException
import java.net.URL

data class InfoLink(private val infoLinkUrl: String) {

    val url: URL?

    init {
        url = createUrl(infoLinkUrl)
    }

    private fun createUrl(url: String): URL? {
        return try {
            URL(url)
        } catch (e: MalformedURLException) {
            null
        }
    }

    /**
     * First checks a value is present and then checks if the given value is
     * valid.
     *
     * @return
     */
    fun isValid(): Boolean {
        if (!isPresent()) {
            return false
        }

        return getUrlValidator().isValid(url.toString())
    }


    /**
     * Checks if a value is actually present
     *
     * @return
     */
    fun isPresent(): Boolean {
        return url != null && url.toString().isNotBlank()
    }

    override fun toString() = url?.toString() ?: ""

    companion object {
        private val VALID_SCHEMES = arrayOf("HTTP", "HTTPS")

        fun getUrlValidator() = UrlValidator(VALID_SCHEMES)
    }
}
