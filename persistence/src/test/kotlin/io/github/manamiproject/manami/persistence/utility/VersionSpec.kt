package io.github.manamiproject.manami.persistence.utility

import io.github.manamiproject.manami.persistence.utility.Version
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.lang.IllegalArgumentException


@RunWith(JUnitPlatform::class)
class VersionSpec : Spek({

    given("a valid version string") {
        val version = "1.2.3"

        on("checking validity") {
            val result = Version.isValid(version)

            it("must return true") {
                assertThat(result).isTrue()
            }
        }
    }


    given("a valid version string with high numbers") {
        val version = "134.212.356"

        on("checking validity") {
            val result = Version.isValid(version)

            it("must return true") {
                assertThat(result).isTrue()
            }
        }
    }


    given("a version with a non-numerical character as major version") {
        val version = "?.2.3"

        on("checking validity") {
            val result = Version.isValid(version)

            it("must return false, because major version must be numerical") {
                assertThat(result).isFalse()
            }
        }
    }


    given("a version with a non-numerical character as minor version") {
        val version = "1.$.3"

        on("checking validity") {
            val result = Version.isValid(version)

            it("must return false, because minor version must be numerical") {
                assertThat(result).isFalse()
            }
        }
    }


    given("a version with a non-numerical character as bugfix version") {
        val version = "1.2.!"

        on("checking validity") {
            val result = Version.isValid(version)

            it("must return false, because bugfix version must be numerical") {
                assertThat(result).isFalse()
            }
        }
    }


    given("a version containing more than three parts") {
        val version = "1.4.3.1"

        on("checking validity") {
            val result = Version.isValid(version)

            it("must return false, because semantic versioning with exactly three parts is expected") {
                assertThat(result).isFalse()
            }
        }
    }


    given("a version containing less than three parts") {
        val version = "1.4"

        on("checking validity") {
            val result = Version.isValid(version)

            it("must return false, because semantic versioning with exactly three parts is expected") {
                assertThat(result).isFalse()
            }
        }
    }


    given("a version containing three parts, but major version is missing") {
        val version = ".2.3"

        on("checking validity") {
            val result = Version.isValid(version)

            it("must return false, because semantic versioning with exactly three numerical parts is expected") {
                assertThat(result).isFalse()
            }
        }
    }


    given("a version containing three parts, but minor version is missing") {
        val version = "1..3"

        on("checking validity") {
            val result = Version.isValid(version)

            it("must return false, because semantic versioning with exactly three numerical parts is expected") {
                assertThat(result).isFalse()
            }
        }
    }


    given("a version containing three parts, but bugfix version is missing") {
        val version = "1.2."

        on("checking validity") {
            val result = Version.isValid(version)

            it("must return false, because semantic versioning with exactly three numerical parts is expected") {
                assertThat(result).isFalse()
            }
        }
    }


    given("a blank version") {
        val version = "   "

        on("checking validity") {
            val result = Version.isValid(version)

            it("must return false, because semantic versioning with exactly three numerical parts separated by a dot is expected") {
                assertThat(result).isFalse()
            }
        }
    }


    given("an empty version") {
        val version = ""

        on("checking validity") {
            val result = Version.isValid(version)

            it("must return false, because semantic versioning with exactly three numerical parts separated by a dot is expected") {
                assertThat(result).isFalse()
            }
        }
    }


    given("a valid version") {
        val version = Version("2.3.4")

        on("checking if the version is newer against the exact same version") {
            val result = version.isNewerThan("2.3.4")

            it("must be false, because they are identical") {
                assertThat(result).isFalse()
            }
        }

        on("checking if the version is older against the exact same version") {
            val result = version.isOlderThan("2.3.4")

            it("must be false, because they are identical") {
                assertThat(result).isFalse()
            }
        }

        on("checking if the version is newer against a version with higher major version") {
            val result = version.isNewerThan("3.2.3")

            it("must be older and therefore false") {
                assertThat(result).isFalse()
            }
        }

        on("checking if the version is older against a version with higher major version") {
            val result = version.isOlderThan("3.2.3")

            it("must be older and therefore true") {
                assertThat(result).isTrue()
            }
        }

        on("checking if the version is newer against a version with lower major version") {
            val result = version.isNewerThan("1.2.3")

            it("must be newer and therefore true") {
                assertThat(result).isTrue()
            }
        }

        on("checking if the version is older against a version with lower major version") {
            val result = version.isOlderThan("1.2.3")

            it("must be newer and therefore false") {
                assertThat(result).isFalse()
            }
        }

        on("checking if the version is newer against a version with higher minor version") {
            val result = version.isNewerThan("2.4.4")

            it("must be older and therefore false") {
                assertThat(result).isFalse()
            }
        }

        on("checking if the version is older against a version with higher minor version") {
            val result = version.isOlderThan("2.4.4")

            it("must be older and therefore true") {
                assertThat(result).isTrue()
            }
        }

        on("checking if the version is newer against a version with lower minor version") {
            val result = version.isNewerThan("2.2.4")

            it("must be newer and therefore true") {
                assertThat(result).isTrue()
            }
        }

        on("checking if the version is older against a version with lower minor version") {
            val result = version.isOlderThan("2.2.4")

            it("must be newer and therefore false") {
                assertThat(result).isFalse()
            }
        }

        on("checking if the version is newer against a version with higher bugfix version") {
            val result = version.isNewerThan("2.3.5")

            it("must be older and therefore false") {
                assertThat(result).isFalse()
            }
        }

        on("checking if the version is older against a version with higher bugfix version") {
            val result = version.isOlderThan("2.3.5")

            it("must be older and therefore true") {
                assertThat(result).isTrue()
            }
        }

        on("checking if the version is newer against a version with lower bugfix version") {
            val result = version.isNewerThan("2.3.3")

            it("must be newer and therefore true") {
                assertThat(result).isTrue()
            }
        }

        on("checking if the version is older against a version with lower bugfix version") {
            val result = version.isOlderThan("2.3.3")

            it("must be newer and therefore false") {
                assertThat(result).isFalse()
            }
        }
    }


    given("a valid version and an invalid other version") {
        val version = Version("1.2.3")
        val otherVersion = " ? "

        on("checking if the otherVersion is newer") {
            var result = false

            try {
                version.isNewerThan(otherVersion)
            } catch (e: IllegalArgumentException) {
                result = true
            }

            it("must fail with an IllegalArgumentException") {
                assertThat(result).isTrue()
            }
        }

        on("checking if the otherVersion is older") {
            var result = false

            try {
                version.isOlderThan(otherVersion)
            } catch (e: IllegalArgumentException) {
                result = true
            }

            it("must fail with an IllegalArgumentException") {
                assertThat(result).isTrue()
            }
        }
    }


    given("a version string with a non-numerical character as major version") {
        val version = "#.2.3"

        on("creating a new version instance") {
            var result = false

            try {
                Version(version)
            } catch (e: IllegalArgumentException) {
                result = true
            }

            it("must fail with an IllegalArgumentException") {
                assertThat(result).isTrue()
            }
        }
    }


    given("a version string with a non-numerical character as minor version") {
        val version = "1.$.3"

        on("creating a new version instance") {
            var result = false

            try {
                Version(version)
            } catch (e: IllegalArgumentException) {
                result = true
            }

            it("must fail with an IllegalArgumentException") {
                assertThat(result).isTrue()
            }
        }
    }


    given("a version string with a non-numerical character as bugifx version") {
        val version = "1.2.!"

        on("creating a new version instance") {
            var result = false

            try {
                Version(version)
            } catch (e: IllegalArgumentException) {
                result = true
            }

            it("must fail with an IllegalArgumentException") {
                assertThat(result).isTrue()
            }
        }
    }
})