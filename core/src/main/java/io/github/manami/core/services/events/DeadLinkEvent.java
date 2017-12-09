package io.github.manami.core.services.events;

import io.github.manami.core.Manami;
import io.github.manami.core.commands.CmdDeleteDeadFilterListEntry;
import io.github.manami.core.commands.CmdDeleteDeadWatchListEntry;
import io.github.manami.core.commands.ReversibleCommand;
import io.github.manami.dto.entities.FilterEntry;
import io.github.manami.dto.entities.MinimalEntry;
import io.github.manami.dto.entities.WatchListEntry;

public class DeadLinkEvent extends AbstractEvent implements ReversibleCommandEvent {

  private ReversibleCommand command = null;


  public DeadLinkEvent(final MinimalEntry entry, final Manami app) {
    if (entry instanceof WatchListEntry) {
      command = new CmdDeleteDeadWatchListEntry((WatchListEntry) entry, app);
    } else if (entry instanceof FilterEntry) {
      command = new CmdDeleteDeadFilterListEntry((FilterEntry) entry, app);

    }
  }

  @Override
  public ReversibleCommand getCommand() {
    return command;
  }
}
