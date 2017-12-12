package io.github.manami.dto.entities

import org.apache.commons.validator.routines.UrlValidator

data class InfoLink(val url: String?) {

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

        return getUrlValidator().isValid(url)
    }


    /**
     * Checks if a value is actually present
     *
     * @return
     */
    fun isPresent(): Boolean {
        return url != null && url.isNotBlank()
    }

    override fun toString() = if (url != null) url else ""

    companion object {
        private val VALID_SCHEMES = arrayOf("HTTP", "HTTPS")

        fun getUrlValidator() = UrlValidator(VALID_SCHEMES)
    }
}
