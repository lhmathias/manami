package io.github.manami.core.services.events;

import io.github.manami.core.commands.ReversibleCommand;

public interface ReversibleCommandEvent {

  ReversibleCommand getCommand();
}
