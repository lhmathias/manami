package io.github.manami.persistence.importer.xml;

import io.github.manami.persistence.PersistenceFacade
import io.github.manami.persistence.importer.Importer
import io.github.manami.persistence.importer.xml.XmlImporter.XmlStrategy.MAL
import io.github.manami.persistence.importer.xml.XmlImporter.XmlStrategy.MANAMI
import io.github.manami.persistence.importer.xml.parser.MalSaxParser
import io.github.manami.persistence.importer.xml.parser.ManamiSaxParser
import org.xml.sax.ContentHandler
import org.xml.sax.InputSource
import java.nio.file.Path
import javax.xml.parsers.SAXParserFactory

/**
 * Importer for opening xml files which are specific for this application.
 */
class XmlImporter(
        private val strategy: XmlStrategy,
        private val persistence: PersistenceFacade
) : Importer {


    /**
     * Strategy for XML import.
     */
    enum class XmlStrategy {
        /**
         * Strategy for opening manami files.
         */
        MANAMI,
        /**
         * Strategy for myanimelist.net files.
         */
        MAL
    }

    override fun importFile(file: Path) {
        val parserFactory: SAXParserFactory = SAXParserFactory.newInstance().apply {
            isValidating = true
            isNamespaceAware = true
        }

        parserFactory.newSAXParser().xmlReader.let { saxParser ->
            saxParser.contentHandler = createParserFromStrategy()
            saxParser.parse(InputSource(file.toAbsolutePath().toString()))
        }
    }


    /**
     * Creates a parser from the given strategy.
     *
     * @return Parser
     */
    private fun createParserFromStrategy(): ContentHandler {
        return when (strategy) {
            MANAMI -> ManamiSaxParser(persistence)
            MAL -> MalSaxParser(persistence)
        }
    }
}
