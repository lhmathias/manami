package io.github.manami.persistence.exporter.csv

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

class CsvConfigSpec : Spek({
    
    given("CsvConfigType string 'animeList'") {
        val csvConfigType = "animeList"

        on("finding CsvConfigType for 'animeList'") {
            val result = CsvConfig.CsvConfigType.findByName(csvConfigType)

            it("must return the corresponding CsvConfigType") {
                assertThat(result).isEqualTo(CsvConfig.CsvConfigType.ANIMELIST)
            }
        }

        on("finding CsvConfigType for uppercase string 'ANIMELIST'") {
            val result = CsvConfig.CsvConfigType.findByName(csvConfigType.toUpperCase())

            it("must return the corresponding CsvConfigType") {
                assertThat(result).isEqualTo(CsvConfig.CsvConfigType.ANIMELIST)
            }
        }

        on("finding CsvConfigType for lowercase string 'animelist'") {
            val result = CsvConfig.CsvConfigType.findByName(csvConfigType.toLowerCase())

            it("must return the corresponding CsvConfigType") {
                assertThat(result).isEqualTo(CsvConfig.CsvConfigType.ANIMELIST)
            }
        }
    }


    given("CsvConfigType string 'watchList'") {
        val csvConfigType = "watchList"

        on("finding CsvConfigType for 'watchList'") {
            val result = CsvConfig.CsvConfigType.findByName(csvConfigType)

            it("must return the corresponding CsvConfigType") {
                assertThat(result).isEqualTo(CsvConfig.CsvConfigType.WATCHLIST)
            }
        }

        on("finding CsvConfigType for uppercase string 'WATCHLIST'") {
            val result = CsvConfig.CsvConfigType.findByName(csvConfigType.toUpperCase())

            it("must return the corresponding CsvConfigType") {
                assertThat(result).isEqualTo(CsvConfig.CsvConfigType.WATCHLIST)
            }
        }

        on("finding CsvConfigType for lowercase string 'watchlist'") {
            val result = CsvConfig.CsvConfigType.findByName(csvConfigType.toLowerCase())

            it("must return the corresponding CsvConfigType") {
                assertThat(result).isEqualTo(CsvConfig.CsvConfigType.WATCHLIST)
            }
        }
    }


    given("CsvConfigType string 'filterList'") {
        val csvConfigType = "filterList"

        on("finding CsvConfigType for 'filterList'") {
            val result = CsvConfig.CsvConfigType.findByName(csvConfigType)

            it("must return the corresponding CsvConfigType") {
                assertThat(result).isEqualTo(CsvConfig.CsvConfigType.FILTERLIST)
            }
        }

        on("finding CsvConfigType for uppercase string 'FILTERLIST'") {
            val result = CsvConfig.CsvConfigType.findByName(csvConfigType.toUpperCase())

            it("must return the corresponding CsvConfigType") {
                assertThat(result).isEqualTo(CsvConfig.CsvConfigType.FILTERLIST)
            }
        }

        on("finding CsvConfigType for lowercase string 'filterlist'") {
            val result = CsvConfig.CsvConfigType.findByName(csvConfigType.toLowerCase())

            it("must return the corresponding CsvConfigType") {
                assertThat(result).isEqualTo(CsvConfig.CsvConfigType.FILTERLIST)
            }
        }
    }


    given("an invalid CsvConfigType string 'akjbfJKdsd'") {
        val csvConfigType = "akjbfJKdsd"

        on("finding CsvConfigType for 'akjbfJKdsd'") {
            val result = CsvConfig.CsvConfigType.findByName(csvConfigType)

            it("must return null") {
                assertThat(result).isNull()
            }
        }

        on("finding CsvConfigType for uppercase string 'AKJBFJKDSD'") {
            val result = CsvConfig.CsvConfigType.findByName(csvConfigType.toUpperCase())

            it("must return null") {
                assertThat(result).isNull()
            }
        }

        on("finding CsvConfigType for lowercase string 'akjbfjkdsd'") {
            val result = CsvConfig.CsvConfigType.findByName(csvConfigType.toLowerCase())

            it("must return null") {
                assertThat(result).isNull()
            }
        }
    }
})