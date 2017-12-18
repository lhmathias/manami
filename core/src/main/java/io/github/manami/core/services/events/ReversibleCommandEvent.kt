package io.github.manami.core.services.events

import io.github.manami.core.commands.ReversibleCommand


interface ReversibleCommandEvent {

    fun getCommand(): ReversibleCommand
}
