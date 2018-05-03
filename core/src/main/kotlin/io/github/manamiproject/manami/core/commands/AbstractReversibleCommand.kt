package io.github.manamiproject.manami.core.commands

import io.github.manamiproject.manami.dto.entities.Anime
import io.github.manamiproject.manami.persistence.Persistence

/**
 * Abstract reversible command.
 */
internal abstract class AbstractReversibleCommand(
        private var persistenceHandler: Persistence
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
        if(oldAnime == null || newAnime == null) {
            return false
        }

        oldAnime?.let { anime ->
            if (anime.isValid()) {
                persistenceHandler.removeAnime(anime)
            }
        }

        newAnime?.let { anime ->
            return if(anime.isValid()) {
                persistenceHandler.addAnime(anime)
            } else {
                oldAnime?.let { previousEntry ->
                    if(previousEntry.isValid() && !persistenceHandler.animeEntryExists(previousEntry.infoLink)) {
                        persistenceHandler.addAnime(previousEntry)
                    }
                }

                false
            }
        }

        return false
    }


    override fun undo() {
        newAnime?.let { anime ->
            persistenceHandler.removeAnime(anime)
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
