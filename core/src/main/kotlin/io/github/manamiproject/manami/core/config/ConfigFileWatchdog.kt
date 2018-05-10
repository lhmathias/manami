package io.github.manamiproject.manami.core.config

import io.github.manamiproject.manami.common.LoggerDelegate
import org.apache.commons.io.IOUtils
import org.slf4j.Logger
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


internal class ConfigFileWatchdog(path: Path) {

    private val log: Logger by LoggerDelegate()

    private val stylesheetPlaceholder = "{{STYLESHEET-FILE-PATH}}"
    private var configFolder: Path = path.resolve(Paths.get("config"))
    private var themePath: Path = configFolder.resolve(Paths.get("theme"))
    private var stylesheetFile: Path = themePath.resolve(Paths.get("style.css"))


    fun validate() {
        checkConfigFolder()
        checkThemeFolder()
        checkDtdFile()
        checkStylesheetFile()
        checkTransformationFile()
    }


    private fun checkThemeFolder() {
        createDirectoryIfNotExist(themePath)
    }


    private fun checkConfigFolder() {
        createDirectoryIfNotExist(configFolder)
    }


    private fun createDirectoryIfNotExist(dir: Path) {
        val folderName: Path = dir.fileName

        log.info("Checking folder [{}]", folderName)

        if (!Files.exists(dir)) {
            log.info("Folder [{}] does not exist, creating it.", folderName)
            Files.createDirectory(dir)
            log.info("Folder [{}] created under [{}]", folderName, dir.toAbsolutePath())
        }

        if (Files.exists(dir) && !Files.isDirectory(dir)) {
            throw IllegalStateException("Config folder does not exist, but a file with the same name.")
        }
    }


    private fun checkDtdFile() {
        val animeDtd: Path = configFolder.resolve(Paths.get("animelist.dtd"))
        createFileIfNotExist(animeDtd)
    }


    private fun checkTransformationFile() {
        val transformationFile: Path = themePath.resolve(Paths.get("animelist_transform.xsl"))
        createFileIfNotExist(transformationFile)

        val transformationFileAsLines: MutableList<String> = Files.readAllLines(transformationFile)
        val absoluteStylesheetPath: String = themePath.resolve(Paths.get("style.css")).toAbsolutePath().toUri().toURL().toString()

        for (index in 0 until transformationFileAsLines.size) {
            val line: String = transformationFileAsLines[index]
            if (line.contains(stylesheetPlaceholder)) {
                transformationFileAsLines[index] = line.replace(stylesheetPlaceholder, absoluteStylesheetPath)
            }
        }

        // remove strange preamble which only is set in this file and only in
        // when starting a fat jar
        var firstLine: String = transformationFileAsLines[0]

        while (firstLine[0] != '<' && firstLine.isNotEmpty()) {
            firstLine = firstLine.substring(1)
        }

        transformationFileAsLines[0] = firstLine

        Files.write(transformationFile, transformationFileAsLines, StandardCharsets.UTF_8)
    }


    private fun checkStylesheetFile() {
        createFileIfNotExist(stylesheetFile)
    }


    private fun createFileIfNotExist(file: Path) {
        val fileName: Path = file.fileName

        log.info("Checking file [{}]", fileName)

        if (!Files.exists(file)) {
            log.info("File [{}] does not exist, creating it.", fileName)
            Files.createFile(file)

            val resourceFilename: String = String.format("releasebuild_%s", file.fileName)
            val resourceFileAsString: String = IOUtils.toString(ClassLoader.getSystemResourceAsStream(resourceFilename), Charset.forName("UTF-8"))
            val fileAsLines: List<String> = resourceFileAsString.split("\\r?\\n").toList()
            Files.write(file, fileAsLines, StandardCharsets.UTF_8)

            log.info("File [{}] created under [{}]", fileName, file.toAbsolutePath())
        }
    }
}
