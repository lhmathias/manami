package io.github.manami.persistence.utility;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to create, check and resolve an absolute path to a relative path if necessary.
 */
class PathResolver {

  private static final Logger log = LoggerFactory.getLogger(PathResolver.class);

  companion object {
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
      final Optional<Path> optDir = buildPath(path, currentWorkingDir);

      if (optDir.isPresent()) {
        try {
          return currentWorkingDir.relativize(optDir.get()).toString().replace("\\", "/");
        } catch (e: IllegalArgumentException) {
          return path
        }
      }

      return null
    }


    /**
     * Creates a relative path.
     */
    fun createRelativePath(dir: Path, currentWorkingDir: Path): Path {
      var ret: Path = null

      try {
        ret = currentWorkingDir.resolve(dir)
      } catch (e: Exception) {
        log.error("An error occurred trying to create a relative Path: ", e)
      }

      return ret;
    }
  }
}
