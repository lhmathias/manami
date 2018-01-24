package io.github.manamiproject.manami.dto.entities

import org.apache.commons.validator.routines.UrlValidator
import java.net.MalformedURLException
import java.net.URL

data class InfoLink private constructor (val url: URL?) {

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


     companion object  {
        private val VALID_SCHEMES = arrayOf("HTTP", "HTTPS")

        fun getUrlValidator() = UrlValidator(VALID_SCHEMES)

        operator fun invoke(url: String): InfoLink {
            return InfoLink(createUrl(url.trim()))
        }

        private fun createUrl(url: String): URL? {
            val normalizedUrl: String = normalizeUrl(url)

            return try {
                URL(normalizedUrl)
            } catch (e: MalformedURLException) {
                null
            }
        }

        private fun normalizeUrl(url: String): String {
            return when {
                url.contains(DOMAINS.MAL.value) -> MyAnimeListNormalizer.normalize(url)
                else -> url
            }
        }
    }
}


enum class DOMAINS(val value: String) {
    MAL("myanimelist.net"),
    ANIDB("anidb.net");
}


private object MyAnimeListNormalizer {

    fun normalize(url: String): String {
        val prefix = "https://${DOMAINS.MAL.value}/anime"
        var normalizedUrl = url

        //remove everything after the ID and noramlize prefix if necesary
        Regex(".*?/[0-9]+").find(normalizedUrl)?.let { matchResult ->
            normalizedUrl = matchResult.value

            if(!normalizedUrl.startsWith(prefix)) {
                Regex("[0-9]+").find(normalizedUrl)?.let { idMatcherResult ->
                    normalizedUrl = "$prefix/${idMatcherResult.value}"
                }
            }
        }

        // correct deeplinks using .php=id=
        if (normalizedUrl.contains(".php?id=")) {
            normalizedUrl = normalizedUrl.replace(".php?id=", "/")
        }

        return normalizedUrl
    }
}