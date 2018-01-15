package io.github.manami.cache.strategies.headlessbrowser.extractor.util.mal;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import io.github.manamiproject.manami.dto.entities.InfoLink;

public class MyAnimeListNetUtilTest {

    private static final String DEATH_NOTE_URL_NO_PROTOCOL = MyAnimeListNetUtil.DOMAIN + "/anime/1535/Death_Note";


    @Test
    public void isResponsibleTrueHttpWww() {
        // given
        final InfoLink infoLink = new InfoLink("http://www." + DEATH_NOTE_URL_NO_PROTOCOL);

        // when
        final boolean result = MyAnimeListNetUtil.isResponsible(infoLink);

        // then
        assertTrue(result);
    }


    @Test
    public void isResponsibleTrueHttp() {
        // given
        final InfoLink infoLink = new InfoLink("http://" + DEATH_NOTE_URL_NO_PROTOCOL);

        // when
        final boolean result = MyAnimeListNetUtil.isResponsible(infoLink);

        // then
        assertTrue(result);
    }


    @Test
    public void isResponsibleTrueHttpsWww() {
        // given
        final InfoLink infoLink = new InfoLink("https://www." + DEATH_NOTE_URL_NO_PROTOCOL);

        // when
        final boolean result = MyAnimeListNetUtil.isResponsible(infoLink);

        // then
        assertTrue(result);
    }


    @Test
    public void isResponsibleTrueHttps() {
        // given
        final InfoLink infoLink = new InfoLink("https://" + DEATH_NOTE_URL_NO_PROTOCOL);

        // when
        final boolean result = MyAnimeListNetUtil.isResponsible(infoLink);

        // then
        assertTrue(result);
    }


    @Test
    public void isResponsibleFalse() {
        // given
        final InfoLink infoLink = new InfoLink("https://animenewsnetwork.com/encyclopedia/anime.php?id=6592");

        // when
        final boolean result = MyAnimeListNetUtil.isResponsible(infoLink);

        // then
        assertFalse(result);
    }


    @Test
    public void isResponsibleBlank() {
        // given
        final InfoLink urlEmpty = new InfoLink(EMPTY);
        final InfoLink urlWhitespace = new InfoLink(EMPTY);
        final InfoLink urlNull = new InfoLink(null);

        // when
        final boolean resultEmpty = MyAnimeListNetUtil.isResponsible(urlEmpty);
        final boolean resultWhitespace = MyAnimeListNetUtil.isResponsible(urlWhitespace);
        final boolean resultNull = MyAnimeListNetUtil.isResponsible(urlNull);

        // then
        assertFalse(resultEmpty);
        assertFalse(resultWhitespace);
        assertFalse(resultNull);
    }
}