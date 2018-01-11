package io.github.manami.core.tasks.events

import io.github.manami.core.ManamiImpl
import io.github.manami.core.commands.CmdChangeEpisodes
import io.github.manami.core.commands.ReversibleCommand
import io.github.manami.dto.entities.Anime

class EpisodesDifferEvent(
        private val animeEntry: Anime,
        private val newValue: Int,
        private val app: ManamiImpl
) : AbstractEvent(animeEntry), ReversibleCommandEvent {

    private val cmd: CmdChangeEpisodes = CmdChangeEpisodes(animeEntry, newValue, app)


    override fun getCommand(): ReversibleCommand {
        return cmd
    }
}
