package io.github.manami.persistence.exporter.csv

import org.supercsv.cellprocessor.Optional
import org.supercsv.cellprocessor.constraint.NotNull


/**
 * Configuration for the CSV import and export.
 */
class CsvConfig {


    enum class CsvConfigType(val value: String) {

        ANIMELIST("animeList"), WATCHLIST("watchList"), FILTERLIST("filterList");

        companion object {
            fun findByName(name: String): CsvConfigType? {
                return values().firstOrNull { it.value.equals(name, ignoreCase = true) }
            }
        }
    }


    /**
     * Returns the names of the columns.
     *
     * @return A String with the names of the columns for the csv file.
     */
    fun getHeaders() = arrayOf("list", "title", "type", "episodes", "infolink", "location")


    /**
     * Type of Processors.
     *
     * @return An array with indication of the column's type.
     */
    fun getProcessors() = arrayOf(
            NotNull(),  // List (e.g. animeList, filterList, watchList)
            NotNull(),  // Title
            Optional(), // Type
            Optional(), // Episodes
            Optional(), // InfoLink
            Optional()  // Location
    )
}
