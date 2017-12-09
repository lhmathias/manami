package io.github.manami.core.services.events;

import java.nio.file.Path;

/**
 * @author manami-project
 */
public class CrcEvent extends AbstractEvent {

  private Path path;

  private String crcSum;

  public Path getPath() {
    return path;
  }

  public void setPath(Path path) {
    this.path = path;
  }

  public String getCrcSum() {
    return crcSum;
  }

  public void setCrcSum(String crcSum) {
    this.crcSum = crcSum;
  }
}
