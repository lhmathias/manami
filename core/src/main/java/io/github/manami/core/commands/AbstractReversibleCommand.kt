package io.github.manami.core.commands

import io.github.manami.core.Manami
import io.github.manami.dto.entities.Anime

/**
 * Abstract reversible command.
 */
internal abstract class AbstractReversibleCommand(
        protected var app: Manami
) : ReversibleCommand {

    /**
     * Flag indicating whether this command is the last which was executed before saving.
     */
    private var lastSaved = false

    /**
     * Instance of the anime before it was edited.
     */
    protected var oldAnime: Anime? = null

    /**
     * Instance containing the edited episodes.
     */
    protected var newAnime: Anime? = null


    override fun execute(): Boolean {
        oldAnime?.id?.let { id ->
            app.removeAnime(id)
        }

        newAnime?.let { anime ->
            return app.addAnime(anime)
        }

        return false
    }


    override fun undo() {
        newAnime?.id?.let { id ->
            app.removeAnime(id)
        }

        oldAnime?.let { anime ->
            app.addAnime(anime)
        }
    }


    override fun redo() {
        execute()
    }

    override fun isLastSaved(): Boolean {
        return lastSaved
    }


    override fun setLastSaved(value: Boolean) {
        lastSaved = value
    }
}
