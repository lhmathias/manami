package io.github.manamiproject.manami.core.config

import io.github.manamiproject.manami.common.exists
import io.github.manamiproject.manami.common.isDirectory
import io.github.manamiproject.manami.common.isRegularFile
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.nio.file.Files.createTempDirectory
import java.nio.file.Path

object ConfigFileWatchdogSpec : Spek({
    
    given("a temp directory and a ConfigFileWatchDog instance") {
        val tempDirectory: Path = createTempDirectory("test")

        // when
        val configFileWatchDog = ConfigFileWatchdog(tempDirectory)
        
        on("letting the watch dog validate the current directory") {
            configFileWatchDog.validate()
            
            val configFolder: Path = tempDirectory.resolve("config")
            val themeFolder: Path = configFolder.resolve("theme")
            val dtdFile: Path = configFolder.resolve("animelist.dtd")
            val transformationFile: Path = themeFolder.resolve("animelist_transform.xsl")
            val stylesheetFile: Path = themeFolder.resolve("style.css")
            
            it("must create a config folder") {
                assertThat(configFolder.exists()).isTrue()
                assertThat(configFolder.isRegularFile()).isTrue()
            }

            it("must create a theme folder") {
                assertThat(themeFolder.exists()).isTrue()
                assertThat(themeFolder.isDirectory()).isTrue()
            }

            it("must create the dtd file") {
                assertThat(dtdFile.exists()).isTrue()
                assertThat(dtdFile.isRegularFile()).isTrue()
            }

            it("must create the xslt file") {
                assertThat(transformationFile.exists()).isTrue()
                assertThat(transformationFile.isRegularFile()).isTrue()
            }

            it("must create the css file") {
                assertThat(stylesheetFile.exists()).isTrue()
                assertThat(stylesheetFile.isRegularFile()).isTrue()
            }
        }
    }
})