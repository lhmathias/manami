package io.github.manamiproject.manami.cache.offlinedatabase

import io.github.manamiproject.manami.cache.CacheFacade
import io.github.manamiproject.manami.entities.AnimeType
import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.entities.InfoLink
import io.github.manamiproject.manami.entities.NORMALIZED_ANIME_DOMAIN
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.jupiter.api.Tag
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.nio.file.*
import java.util.*
import java.util.zip.CRC32


private const val DATABASE_RESOURCE_FOLDER = "offlineDatabaseForUpdate"
private const val DATABASE_TEST_DESTINATION_FOLDER = "./database"

@RunWith(JUnitPlatform::class)
@Tag("integrationTest")
class OfflineDatabaseIntegrationTest : Spek({

    afterEachTest {
        removeOfflineDatabase()
    }

    given("a valid, but outdated clone of the offline database") {
        val resourceDatabaseFolder: Path = Paths.get(OfflineDatabaseIntegrationTest::class.java.classLoader.getResource(DATABASE_RESOURCE_FOLDER).toURI())
        Files.createDirectory(Paths.get(DATABASE_TEST_DESTINATION_FOLDER))
        Files.walk(resourceDatabaseFolder, FileVisitOption.FOLLOW_LINKS).forEach {
            if(it!=resourceDatabaseFolder) {
                Files.copy(it, Paths.get(DATABASE_TEST_DESTINATION_FOLDER).resolve(it.fileName), StandardCopyOption.REPLACE_EXISTING)
            }
            if(Files.isDirectory(it)) {
                println("there it is ${it.fileName}")
            }
        }

        on("initializing the cache") {
            val infoLink = InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}1535")
            val crcSum1 = CRC32().apply {
                update(Files.readAllBytes(Paths.get("database/anime-offline-database.json")))
            }
            println("Here is the first: ${Integer.toHexString(crcSum1.value.toInt())}")
            val cacheResult = CacheFacade.fetchAnime(infoLink)
            
            it("must have updated the offline database file") {
                val crcSum: Long = CRC32().apply {
                    update(Files.readAllBytes(Paths.get("database/anime-offline-database.json")))  
                }.value
                
                assertThat(crcSum).isNotEqualTo(crcSum1)
            }

            it("must not return null") {
                assertThat(cacheResult).isNotNull()
            }

            it("must return the correct title") {
                assertThat(cacheResult?.title).isEqualTo("Death Note")
            }

            it("must return the correct infolink") {
                assertThat(cacheResult?.infoLink).isEqualTo(infoLink)
            }

            it("must return the correct number of episodes") {
                assertThat(cacheResult?.episodes).isEqualTo(37)
            }

            it("must return the correct type") {
                assertThat(cacheResult?.type).isEqualTo(AnimeType.TV)
            }

            it("must return the default value for a location") {
                assertThat(cacheResult?.location).isEqualTo("/")
            }

            it("must return a newly generated id") {
                assertThat(cacheResult?.id).isEqualTo(UUID.fromString("2d88de4c-9dbd-4837-b3ab-66c597c379ce"))
            }
        }
    }


    given("no offline database to enforce cloning of the git repo and a valid MAL infolink") {
        val infoLink = InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}1535")

        on("fetching the anime via cache") {
            val result: Anime? = CacheFacade.fetchAnime(infoLink)

            it("must not return null") {
                assertThat(result).isNotNull()
            }

            it("must return the correct title") {
                assertThat(result?.title).isEqualTo("Death Note")
            }

            it("must return the correct infolink") {
                assertThat(result?.infoLink).isEqualTo(infoLink)
            }

            it("must return the correct number of episodes") {
                assertThat(result?.episodes).isEqualTo(37)
            }

            it("must return the correct type") {
                assertThat(result?.type).isEqualTo(AnimeType.TV)
            }

            it("must return the default value for a location") {
                assertThat(result?.location).isEqualTo("/")
            }

            it("must return the correct id") {
                assertThat(result?.id).isEqualTo(UUID.fromString("2d88de4c-9dbd-4837-b3ab-66c597c379ce"))
            }
        }

        on("fetching the related anime via cache") {
            val result: Set<InfoLink> = CacheFacade.fetchRelatedAnime(infoLink)

            it("must not be empty") {
                assertThat(result).isNotEmpty()
            }

            it("must contain one entry") {
                assertThat(result).hasSize(1)
            }

            it("must contain the correct related anime") {
                assertThat(result.contains(InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}2994"))).isTrue()
            }
        }
    }


    given("no offline database to enforce cloning of the git repo and a valid ANIDB infolink") {
        val infoLink = InfoLink("${NORMALIZED_ANIME_DOMAIN.ANIDB.value}4563")

        on("fetching the anime via cache") {
            val result: Anime? = CacheFacade.fetchAnime(infoLink)

            it("must not return null") {
                assertThat(result).isNotNull()
            }

            it("must return the correct title") {
                assertThat(result?.title).isEqualTo("Death Note")
            }

            it("must return the correct infolink") {
                assertThat(result?.infoLink).isEqualTo(infoLink)
            }

            it("must return the correct number of episodes") {
                assertThat(result?.episodes).isEqualTo(37)
            }

            it("must return the correct type") {
                assertThat(result?.type).isEqualTo(AnimeType.TV)
            }

            it("must return the default value for a location") {
                assertThat(result?.location).isEqualTo("/")
            }

            it("must return the correct id") {
                assertThat(result?.id).isEqualTo(UUID.fromString("2d88de4c-9dbd-4837-b3ab-66c597c379ce"))
            }
        }

        on("fetching the related anime via cache") {
            val result: Set<InfoLink> = CacheFacade.fetchRelatedAnime(infoLink)

            it("must not be empty") {
                assertThat(result).isNotEmpty()
            }

            it("must contain two entries") {
                assertThat(result).hasSize(2)
            }

            it("must contain the correct related anime") {
                assertThat(result.contains(InfoLink("${NORMALIZED_ANIME_DOMAIN.ANIDB.value}8147"))).isTrue()
            }

            it("must contain the correct related anime") {
                assertThat(result.contains(InfoLink("${NORMALIZED_ANIME_DOMAIN.ANIDB.value}8146"))).isTrue()
            }
        }
    }
}) {
    companion object {
        fun removeOfflineDatabase() {
            val databaseFolder: Path = Paths.get("database")

            if(Files.exists(databaseFolder)) {
                Files.walk(databaseFolder)
                        .sorted(Comparator.reverseOrder())
                        .forEach(Files::delete)
            }
        }
    }
}