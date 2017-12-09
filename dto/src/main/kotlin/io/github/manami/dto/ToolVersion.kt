package io.github.manami.dto

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*


/**
 * Contains the current artifact version.
 */
object ToolVersion {

    private val log: Logger = LoggerFactory.getLogger(ToolVersion::class.java)

    @JvmStatic
    fun getToolVersion(): String {
        val propertiesPath = "/META-INF/maven/io.github.manami/persistence/pom.properties"


        try {
            ToolVersion::class.java.getResourceAsStream(propertiesPath).use { stream ->
                val properties = Properties()
                properties.load(stream)

                return properties.getProperty("version")
            }
        } catch (e: Exception) {
            log.error("Could not determine software version: ", e)
        }

        return "unknown"
    }
}
