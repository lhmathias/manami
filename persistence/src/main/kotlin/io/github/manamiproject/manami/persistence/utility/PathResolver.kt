package io.github.manamiproject.manami.persistence.utility

import io.github.manamiproject.manami.common.isNotDirectory
import io.github.manamiproject.manami.common.notExists
import java.lang.IllegalArgumentException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * TODO: Check this object
 * Class to create, check and resolve an absolute path to a relative path if necessary.
 */
object PathResolver {

    fun buildPath(path: String, currentWorkingDir: Path): Path? {
        var dir: Path = Paths.get(path)

        if (dir.notExists() || dir.isNotDirectory()) { // absolute
            dir = createRelativePath(dir, currentWorkingDir)

            if (dir.notExists() || dir.isNotDirectory()) { // relative
                return null
            }
        }

        return dir
    }

    fun buildRelativizedPath(path: String, currentWorkingDir: Path): String {
        val optDir: Path? = buildPath(path, currentWorkingDir)

        if (optDir != null) {
            return try {
                currentWorkingDir.relativize(optDir).toString().replace("\\", "/")
            } catch (e: IllegalArgumentException) {
                path
            }
        }

        return path //TODO: this has been changed from null to path. Is everything still working as excpected?
    }


    /**
     * Creates a relative path.
     */
    private fun createRelativePath(dir: Path, currentWorkingDir: Path) = currentWorkingDir.resolve(dir)
}
