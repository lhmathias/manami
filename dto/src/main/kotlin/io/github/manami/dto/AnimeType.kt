package io.github.manami.dto

/**
 * Types of anime which are known.
 */
enum class AnimeType(val value: String) {
    /**
     * TV
     */
    TV("TV"),
    /**
     * Movie
     */
    MOVIE("Movie"),
    /**
     * OVA
     */
    OVA("OVA"),
    /**
     * Special
     */
    SPECIAL("Special"),
    /**
     * ONA
     */
    ONA("ONA"),
    /**
     * Music
     */
    MUSIC("Music");

    companion object {
        /**
         * Returns a type by a String comparison. It's not case sensitive.
         *
         * @param name Type as String value.
         * @return The corresponding AnimeType or null if no type matches.
         */
        fun findByName(name: String): AnimeType? {
            return values().firstOrNull { it.value.equals(name, ignoreCase = true) }
        }
    }
}
