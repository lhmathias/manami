package io.github.manami.persistence.importer.xml

import io.github.manami.persistence.importer.Importer
import io.github.manami.persistence.importer.xml.XmlImporter.XmlStrategy.MAL
import io.github.manami.persistence.importer.xml.XmlImporter.XmlStrategy.MANAMI
import io.github.manami.persistence.importer.xml.parser.MalSaxParser
import io.github.manami.persistence.importer.xml.parser.ManamiSaxParser
import org.xml.sax.ContentHandler
import org.xml.sax.InputSource
import java.nio.file.Path
import javax.inject.Inject
import javax.inject.Named
import javax.xml.parsers.SAXParserFactory

/**
 * Importer for opening xml files which are specific for this application.
 */
@Named("xmlImporter")
internal class XmlImporter @Inject constructor(
        private val manamiSaxParser: ManamiSaxParser,
        private val malSaxParser: MalSaxParser
) : Importer {

    private var strategy: XmlStrategy = MANAMI

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
            saxParser.contentHandler = createParserFromStrategy(strategy)
            saxParser.parse(InputSource(file.toAbsolutePath().toString()))
        }
    }


    fun using(xmlStrategy: XmlStrategy): XmlImporter {
        strategy = xmlStrategy
        return this
    }


    /**
     * Creates a parser from the given strategy.
     *
     * @param strategy
     * @return Parser
     */
    private fun createParserFromStrategy(strategy: XmlStrategy): ContentHandler {
        return when (strategy) {
            MANAMI -> manamiSaxParser
            MAL -> malSaxParser
        }
    }
}
