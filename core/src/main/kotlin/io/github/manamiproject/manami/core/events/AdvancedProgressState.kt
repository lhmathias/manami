package io.github.manamiproject.manami.core.events

import io.github.manamiproject.manami.entities.Anime

/**
 * Contains the state as well as payload.
 */
data class AdvancedProgressState(
        /**
         * Number of items which have already been processed.
         */
        val progressDone: Int,
        /**
         * Number of items which still need to be processed.
         */
        var progressTodo: Int,
        val anime: Anime
) : ProgressState(progressDone, progressTodo)
