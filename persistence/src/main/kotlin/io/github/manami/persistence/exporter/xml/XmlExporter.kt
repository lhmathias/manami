package io.github.manami.persistence.exporter.xml

import io.github.manami.common.LoggerDelegate
import io.github.manami.persistence.utility.ToolVersion
import io.github.manami.dto.entities.Anime
import io.github.manami.dto.entities.FilterListEntry
import io.github.manami.dto.entities.WatchListEntry
import io.github.manami.persistence.InternalPersistenceHandler
import io.github.manami.persistence.exporter.Exporter
import io.github.manami.persistence.utility.PathResolver
import org.slf4j.Logger
import org.w3c.dom.Document
import org.w3c.dom.DocumentType
import org.w3c.dom.Element
import java.io.IOException
import java.io.PrintWriter
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Path
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

private const val RELATIVE_PATH_SEPARATOR = "/"

internal class XmlExporter(private val persistence: InternalPersistenceHandler) : Exporter {

    private val log: Logger by LoggerDelegate()
    private var doc: Document? = null

    /**
     * File to save to.
     */
    private var file: Path? = null


    override fun save(file: Path): Boolean {
        this.file = file
        val factory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
        factory.isValidating = true
        val builder: DocumentBuilder

        try {
            builder = factory.newDocumentBuilder()

            if (Files.notExists(file)) {
                try {
                    Files.createFile(file)
                } catch (e: IOException) {
                    log.error("Could not create XML file {}", file.fileName, e)
                }
            }

            doc = builder.newDocument()

            // add doctype
            doc?.implementation?.let {
                val doctype: DocumentType = it.createDocumentType("animeList", "SYSTEM", createDtdPath())
                doc?.appendChild(doctype)
            }
        } catch (e: ParserConfigurationException) {
            log.error("An error occurred while trying to initialize the dom tree: ", e)
            return false
        }

        createDomTree()
        prettyPrintXML2File()
        return true
    }


    private fun createConfigFilePath(): String {
        var appDir = codeSourcePath

        if (appDir.isBlank()) {
            return ""
        }

        var endOfString: Int = appDir.lastIndexOf(RELATIVE_PATH_SEPARATOR)

        if (endOfString == -1) {
            endOfString = appDir.lastIndexOf("\\")
        }

        appDir = appDir.substring(0, endOfString)

        if (appDir.startsWith(RELATIVE_PATH_SEPARATOR)) {
            appDir = appDir.substring(1)
        }

        file?.parent?.let {
            return PathResolver.buildRelativizedPath(appDir, it)
        }

        return ""
    }


    private fun createDtdPath(): String {
        var relativeConfigPath: String = createConfigFilePath()

        if (relativeConfigPath.isBlank()) {
            relativeConfigPath = ""
        }

        if (!relativeConfigPath.endsWith(RELATIVE_PATH_SEPARATOR)) {
            relativeConfigPath = relativeConfigPath.plus(RELATIVE_PATH_SEPARATOR)
        }

        return relativeConfigPath.plus("config/animelist.dtd")
    }


    private fun createXsltPath(): String {
        var relativeConfigPath: String = createConfigFilePath()

        if (relativeConfigPath.isBlank()) {
            relativeConfigPath = ""
        }

        if (!relativeConfigPath.endsWith(RELATIVE_PATH_SEPARATOR)) {
            relativeConfigPath = relativeConfigPath.plus(RELATIVE_PATH_SEPARATOR)
        }

        return relativeConfigPath.plus("config/theme/animelist_transform.xsl")
    }


    /**
     * Method to create the dom tree.
     */
    private fun createDomTree() {
        createRootElement()?.let {
            createAnimeList(it)
            createWatchList(it)
            createFilterList(it)
        }
    }


    private fun createRootElement(): Element? {
        return doc?.createElement("manami")?.apply {
            setAttribute("version", ToolVersion.getToolVersion())
            doc?.appendChild(this)

            // create transformation and css information
            val xslt = doc?.createProcessingInstruction("xml-stylesheet", "type=\"text/xml\" href=\"${createXsltPath()}\"")
            doc?.insertBefore(xslt, this)
        }
    }


    private fun createAnimeList(parent: Element) {
        val elementAnimeList: Element? = doc?.createElement("animeList")

        persistence.fetchAnimeList().forEach { anime: Anime ->
            // element "anime"
            doc?.createElement("anime")?.apply {
                // attribute "title"
                this.setAttribute("title", anime.title)

                // attribute "type"
                this.setAttribute("type", anime.type.toString())

                // attribute "episodes"
                this.setAttribute("episodes", anime.episodes.toString())

                // attribute "infolink"
                this.setAttribute("infoLink", anime.infoLink.toString())

                // attribute "location"
                this.setAttribute("location", anime.location)

                // append to animeList
                elementAnimeList?.appendChild(this)
            }
        }

        parent.appendChild(elementAnimeList)
    }


    private fun createWatchList(parent: Element) {
        val elementWatchList: Element? = doc?.createElement("watchList")
        var actEntry: Element?

        persistence.fetchWatchList().forEach { anime: WatchListEntry ->
            // element "anime"
            actEntry = doc?.createElement("watchListEntry")

            // attribute "title"
            actEntry?.setAttribute("title", anime.title)

            actEntry?.setAttribute("thumbnail", anime.thumbnail.toString())

            // attribute "infolink"
            actEntry?.setAttribute("infoLink", anime.infoLink.toString())

            // append to animeList
            elementWatchList?.appendChild(actEntry)
        }

        parent.appendChild(elementWatchList)
    }


    private fun createFilterList(parent: Element) {
        val elementFilterList: Element? = doc?.createElement("filterList")
        var actEntry: Element?

        persistence.fetchFilterList().forEach { anime: FilterListEntry ->
            // element "anime"
            actEntry = doc?.createElement("filterEntry")

            // attribute "title"
            actEntry?.setAttribute("title", anime.title)

            actEntry?.setAttribute("thumbnail", anime.thumbnail.toString())

            // attribute "infolink"
            actEntry?.setAttribute("infoLink", anime.infoLink.toString())

            // append to animeList
            elementFilterList?.appendChild(actEntry)
        }

        parent.appendChild(elementFilterList)
    }


    /**
     * Write the tree into the file and save it.
     */
    private fun prettyPrintXML2File() {
        val tfactory: TransformerFactory = TransformerFactory.newInstance()

        try {
            val transformer: Transformer = tfactory.newTransformer().apply {
                setOutputProperty(OutputKeys.INDENT, "yes")
                setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")
            }


            doc?.doctype?.let {
                transformer.setOutputProperty("doctype-system", it.systemId)
            }

            val output = PrintWriter(file?.toFile(), "UTF-8")
            transformer.transform(DOMSource(doc), StreamResult(output))
            output.close()
        } catch (e: Exception) {
            log.error("An error occurred while trying to exportToJsonFile the list to a xml file: ", e)
        }
    }

    companion object {
        private val codeSourcePath = XmlExporter::class.java.protectionDomain.codeSource.location.path
    }
}