package io.github.manami.persistence.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Class to create, check and resolve an absolute path to a relative path if
 * necessary.
 *
 * @author manami-project
 * @since 2.10.0
 */
public final class PathResolver {

  private static final Logger log = LoggerFactory.getLogger(PathResolver.class);

  private PathResolver() {
  }


  /**
   * @since 2.10.0
   */
  public static Optional<Path> buildPath(final String path, final Path currentWorkingDir) {
    Path dir = Paths.get(path);

    if (!Files.exists(dir) || !Files.isDirectory(dir)) { // absolute
      dir = createRelativePath(dir, currentWorkingDir);
      if (!Files.exists(dir) || !Files.isDirectory(dir)) { // relative
        return Optional.empty();
      }
    }

    return Optional.ofNullable(dir);
  }


  /**
   * @since 2.10.0
   */
  public static String buildRelativizedPath(final String path, final Path currentWorkingDir) {
    final Optional<Path> optDir = buildPath(path, currentWorkingDir);

    if (optDir.isPresent()) {
      try {
        return currentWorkingDir.relativize(optDir.get()).toString().replace("\\", "/");
      } catch (final IllegalArgumentException e) {
        return path;
      }
    }

    return null;
  }


  /**
   * Creates a relative path.
   *
   * @since 2.6.0
   */
  private static Path createRelativePath(final Path dir, final Path currentWorkingDir) {
    Path ret = null;
    try {
      ret = currentWorkingDir.resolve(dir);
    } catch (final Exception e) {
      log.error("An error occurred trying to create a relative Path: ", e);
    }

    return ret;
  }
}
