package io.github.manami.persistence.exporter.csv;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import io.github.manami.persistence.exporter.csv.CsvConfig.CsvConfigType;

public class CsvConfigTest {

    @Test(groups = "unitTest")
    public void testThatNonMatchableStringReturnsNull() {
        // given

        // when
        final CsvConfigType result = CsvConfigType.findByName("anyNonMatchableString");

        // then
        assertEquals(result, null);
    }


    @Test(groups = "unitTest")
    public void testAnimeList() {
        // given
        final String exactSameString = "animeList";
        final String upperCase = exactSameString.toUpperCase();
        final String lowerCase = exactSameString.toLowerCase();

        // when
        final CsvConfigType resultExactSame = CsvConfigType.findByName(exactSameString);
        final CsvConfigType resultUpperCase = CsvConfigType.findByName(upperCase);
        final CsvConfigType resultLowerCase = CsvConfigType.findByName(lowerCase);

        // then
        assertEquals(resultExactSame, CsvConfigType.ANIMELIST);
        assertEquals(resultUpperCase, CsvConfigType.ANIMELIST);
        assertEquals(resultLowerCase, CsvConfigType.ANIMELIST);
    }


    @Test(groups = "unitTest")
    public void testWatchList() {
        // given
        final String exactSameString = "watchList";
        final String upperCase = exactSameString.toUpperCase();
        final String lowerCase = exactSameString.toLowerCase();

        // when
        final CsvConfigType resultExactSame = CsvConfigType.findByName(exactSameString);
        final CsvConfigType resultUpperCase = CsvConfigType.findByName(upperCase);
        final CsvConfigType resultLowerCase = CsvConfigType.findByName(lowerCase);

        // then
        assertEquals(resultExactSame, CsvConfigType.WATCHLIST);
        assertEquals(resultUpperCase, CsvConfigType.WATCHLIST);
        assertEquals(resultLowerCase, CsvConfigType.WATCHLIST);
    }


    @Test(groups = "unitTest")
    public void testFilterList() {
        // given
        final String exactSameString = "filterList";
        final String upperCase = exactSameString.toUpperCase();
        final String lowerCase = exactSameString.toLowerCase();

        // when
        final CsvConfigType resultExactSame = CsvConfigType.findByName(exactSameString);
        final CsvConfigType resultUpperCase = CsvConfigType.findByName(upperCase);
        final CsvConfigType resultLowerCase = CsvConfigType.findByName(lowerCase);

        // then
        assertEquals(resultExactSame, CsvConfigType.FILTERLIST);
        assertEquals(resultUpperCase, CsvConfigType.FILTERLIST);
        assertEquals(resultLowerCase, CsvConfigType.FILTERLIST);
    }
}