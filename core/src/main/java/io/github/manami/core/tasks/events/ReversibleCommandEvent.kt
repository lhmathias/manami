package io.github.manami.core.tasks.events

import io.github.manami.core.commands.ReversibleCommand


interface ReversibleCommandEvent {

    fun getCommand(): ReversibleCommand
}
