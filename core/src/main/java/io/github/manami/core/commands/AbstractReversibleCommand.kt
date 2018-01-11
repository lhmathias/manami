package io.github.manami.core.commands

import io.github.manami.dto.entities.Anime
import io.github.manami.persistence.PersistenceHandler

/**
 * Abstract reversible command.
 */
internal abstract class AbstractReversibleCommand(
        private var persistenceHandler: PersistenceHandler
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
            persistenceHandler.removeAnime(id)
        }

        newAnime?.let { anime ->
            return persistenceHandler.addAnime(anime)
        }

        return false
    }


    override fun undo() {
        newAnime?.id?.let { id ->
            persistenceHandler.removeAnime(id)
        }

        oldAnime?.let { anime ->
            persistenceHandler.addAnime(anime)
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
