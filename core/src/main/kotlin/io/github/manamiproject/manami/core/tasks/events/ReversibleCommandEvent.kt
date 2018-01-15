package io.github.manamiproject.manami.core.tasks.events

import io.github.manamiproject.manami.core.commands.ReversibleCommand


interface ReversibleCommandEvent {

    fun getCommand(): ReversibleCommand
}
