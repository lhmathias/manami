package io.github.manami.persistence.utility

import io.github.manami.dto.LoggerDelegate
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import org.slf4j.Logger

/**
 * Class to create, check and resolve an absolute path to a relative path if necessary.
 */
class PathResolver {

  companion object {
    private val log: Logger by LoggerDelegate()

    fun buildPath(path: String, currentWorkingDir: Path): Path? {
      var dir: Path = Paths.get(path)

      if (!Files.exists(dir) || !Files.isDirectory(dir)) { // absolute
        dir = createRelativePath(dir, currentWorkingDir)

        if (!Files.exists(dir) || !Files.isDirectory(dir)) { // relative
          return null
        }
      }

      return dir
    }

    fun buildRelativizedPath(path: String, currentWorkingDir: Path): String {
      val optDir: Path? = buildPath(path, currentWorkingDir)

      if (optDir != null) {
        try {
          return currentWorkingDir.relativize(optDir).toString().replace("\\", "/")
        } catch (e: IllegalArgumentException) {
          return path
        }
      }

      return path //TODO: this has been changed from null to path. Is everything still working as excpected?
    }


    /**
     * Creates a relative path.
     */
    private fun createRelativePath(dir: Path, currentWorkingDir: Path) = currentWorkingDir.resolve(dir)
  }
}
