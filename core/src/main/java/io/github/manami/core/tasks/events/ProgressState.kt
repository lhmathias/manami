package io.github.manami.core.tasks.events


/**
 * Used by services to notify observers of the progress.
 * @param done Number of processed items.
 * @param todo Number of items which still need to be processed.
 */
open class ProgressState(
        /**
         * Number of items which have already been processed.
         */
        val done: Int,
        /**
         * Number of items which still need to be processed.
         */
        val todo: Int
)