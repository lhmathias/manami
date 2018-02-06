package io.github.manamiproject.manami.cache


/**
 * The cache is supposed to save raw html files from which the information can be extracted at any time.
 */
interface Cache : AnimeRetrieval {

  /**
   * Invalidates all caches.
   */
  fun invalidate()
}
