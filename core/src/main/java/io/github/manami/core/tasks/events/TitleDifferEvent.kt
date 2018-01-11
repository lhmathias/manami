package io.github.manami.core.tasks.events

import io.github.manami.core.ManamiImpl
import io.github.manami.core.commands.CmdChangeTitle
import io.github.manami.dto.entities.Anime

class TitleDifferEvent(
        private var animeEntry: Anime,
        private var newValue: String,
        private var app: ManamiImpl
) : AbstractEvent(animeEntry), ReversibleCommandEvent {

    private val command: CmdChangeTitle = CmdChangeTitle(animeEntry, newValue, app)


    override fun getCommand(): CmdChangeTitle {
        return command
    }
}
