package io.github.manami.core.tasks.events

import io.github.manami.core.ManamiImpl
import io.github.manami.core.commands.CmdChangeLocation
import io.github.manami.dto.entities.Anime

class RelativizeLocationEvent(
        private val animeEntry: Anime,
        private val newValue: String,
        private val app: ManamiImpl
) : AbstractEvent(animeEntry), ReversibleCommandEvent {

    private val command: CmdChangeLocation = CmdChangeLocation(animeEntry, newValue, app)


    override fun getCommand(): CmdChangeLocation {
        return command
    }
}
