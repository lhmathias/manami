package io.github.manamiproject.manami.core.tasks.events

import io.github.manamiproject.manami.core.Manami
import io.github.manamiproject.manami.core.commands.CmdChangeTitle
import io.github.manamiproject.manami.dto.entities.Anime

class TitleDifferEvent(
        private var animeEntry: Anime,
        private var newValue: String,
        private var app: Manami
) : AbstractEvent(animeEntry), ReversibleCommandEvent {

    private val command: CmdChangeTitle = CmdChangeTitle(animeEntry, newValue, app)


    override fun getCommand(): CmdChangeTitle {
        return command
    }
}
