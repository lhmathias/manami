package io.github.manamiproject.manami.cache


/**
 * The cache holds various anime data.
 */
interface Cache : AnimeFetcher {

  /**
   * Invalidates all caches.
   */
  fun invalidate()
}
