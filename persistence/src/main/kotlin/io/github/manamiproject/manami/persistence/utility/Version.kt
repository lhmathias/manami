package io.github.manamiproject.manami.persistence.utility

import java.lang.IllegalArgumentException


internal class Version(version: String) {

    private var internalVersion: SplitVersion = extractVersionParts(version)

    fun isOlderThan(otherVersion: String): Boolean {
        val otherSplitVersion: SplitVersion = extractVersionParts(otherVersion)

        return when {
            internalVersion.major > otherSplitVersion.major -> false
            internalVersion.major < otherSplitVersion.major -> true
            internalVersion.minor > otherSplitVersion.minor -> false
            internalVersion.minor < otherSplitVersion.minor -> true
            internalVersion.bugfix > otherSplitVersion.bugfix -> false
            internalVersion.bugfix < otherSplitVersion.bugfix -> true
            else -> false
        }
    }


    fun isNewerThan(otherVersion: String): Boolean {
        val otherSplitVersion: SplitVersion = extractVersionParts(otherVersion)

        return when {
            internalVersion.major > otherSplitVersion.major -> true
            internalVersion.major < otherSplitVersion.major -> false
            internalVersion.minor > otherSplitVersion.minor -> true
            internalVersion.minor < otherSplitVersion.minor -> false
            internalVersion.bugfix > otherSplitVersion.bugfix -> true
            internalVersion.bugfix < otherSplitVersion.bugfix -> false
            else -> false
        }
    }


    private fun extractVersionParts(version: String): SplitVersion {
        if (!isValid(version)) {
            throw IllegalArgumentException("Version is not valid.")
        }

        val splitParts = version.split('.')
        val filterNotNull: List<Int> = splitParts.mapNotNull { it.toIntOrNull() }

        return SplitVersion(filterNotNull[0], filterNotNull[1], filterNotNull[2])
    }


    private class SplitVersion(val major: Int, val minor: Int, val bugfix: Int)


    companion object {
        fun isValid(version: String) = version.matches(Regex("\\d+\\.\\d+\\.\\d+"))
    }
}