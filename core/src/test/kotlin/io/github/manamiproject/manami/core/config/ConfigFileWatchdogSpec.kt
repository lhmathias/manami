package io.github.manamiproject.manami.core.config

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.nio.file.Files
import java.nio.file.Files.createTempDirectory
import java.nio.file.Path

@RunWith(JUnitPlatform::class)
class ConfigFileWatchdogSpec : Spek({
    
    given("a temp directory and a ConfigFileWatchDog instance") {
        val createTempDirectory: Path = createTempDirectory("test")

        // when
        val configFileWatchDog = ConfigFileWatchdog(createTempDirectory)
        
        on("letting the watch dog validate the current directory") {
            configFileWatchDog.validate()
            
            val configFolder: Path = createTempDirectory.resolve("config")
            val themeFolder: Path = configFolder.resolve("theme")
            val dtdFile: Path = configFolder.resolve("animelist.dtd")
            val transformationFile: Path = themeFolder.resolve("animelist_transform.xsl")
            val stylesheetFile: Path = themeFolder.resolve("style.css")
            
            it("must create a config folder") {
                assertThat(Files.exists(configFolder)).isTrue()
                assertThat(Files.isDirectory(configFolder)).isTrue()
            }

            it("must create a theme folder") {
                assertThat(Files.exists(themeFolder)).isTrue()
                assertThat(Files.isDirectory(themeFolder)).isTrue()
            }

            it("must create the dtd file") {
                assertThat(Files.exists(dtdFile)).isTrue()
                assertThat(Files.isRegularFile(dtdFile)).isTrue()
            }

            it("must create the xslt file") {
                assertThat(Files.exists(transformationFile)).isTrue()
                assertThat(Files.isRegularFile(transformationFile)).isTrue()
            }

            it("must create the css file") {
                assertThat(Files.exists(stylesheetFile)).isTrue()
                assertThat(Files.isRegularFile(stylesheetFile)).isTrue()
            }
        }
    }
})