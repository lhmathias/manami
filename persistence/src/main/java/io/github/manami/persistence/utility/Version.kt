package io.github.manami.persistence.utility


class Version(version: String) {

    private var internalVersion: SplitVersion

    init {
        if (!isValid(version)) {
            throw IllegalArgumentException(MSG_INVALID_VERSION)
        }

        internalVersion = extractVersionParts(version) ?: SplitVersion(0, 0, 0)
    }


    fun isOlderThan(otherVersion: String) = !isNewerThan(otherVersion)


    fun isNewerThan(otherVersion: String): Boolean {
        if (!isValid(otherVersion)) {
            throw IllegalArgumentException(MSG_INVALID_VERSION)
        }

        val otherSplitVersion: SplitVersion? = extractVersionParts(otherVersion)

        return when {
            otherSplitVersion == null -> false
            internalVersion.major > otherSplitVersion.major -> true
            internalVersion.major < otherSplitVersion.major -> false
            internalVersion.minor > otherSplitVersion.minor -> true
            internalVersion.minor < otherSplitVersion.minor -> false
            internalVersion.bugfix > otherSplitVersion.bugfix -> false
            else -> false
        }
    }


    class SplitVersion(val major: Int, val minor: Int, val bugfix: Int)

    companion object {
        private const val MSG_INVALID_VERSION: String = "Version is not valid."

        /**
         * A Version consists of three parts: major, minor, bugfix.
         */
        private const val VERSION_PARTS: Int = 3

        @JvmStatic
        fun isValid(version: String) = extractVersionParts(version) != null

        private fun extractVersionParts(version: String): SplitVersion? {
            val splitParts = version.split(".")

            if (splitParts.size != VERSION_PARTS) {
                return null
            }

            val filterNotNull: List<Int> = splitParts.mapNotNull { it.toIntOrNull() }

            if (filterNotNull.size != VERSION_PARTS) {
                return null
            }

            return SplitVersion(filterNotNull[0], filterNotNull[1], filterNotNull[2])
        }
    }
}