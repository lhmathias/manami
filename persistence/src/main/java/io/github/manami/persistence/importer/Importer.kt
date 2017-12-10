package io.github.manami.persistence.importer;

import java.io.IOException;
import java.nio.file.Path;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 * Interface for an importer.
 */
interface Importer {

  /**
   * Imports a list from a file and enriches the given list.
   *
   * @param file File
   */
  fun importFile(file: Path) throws SAXException, ParserConfigurationException, IOException
}
