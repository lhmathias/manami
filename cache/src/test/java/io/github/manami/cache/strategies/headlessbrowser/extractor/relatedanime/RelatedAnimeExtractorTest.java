package io.github.manami.cache.strategies.headlessbrowser.extractor.relatedanime;

import static io.github.manami.dto.TestConst.UNIT_TEST_GROUP;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Set;

import org.springframework.core.io.ClassPathResource;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.manami.cache.strategies.headlessbrowser.extractor.relatedanime.mal.MyAnimeListNetRelatedAnimeExtractor;
import io.github.manami.cache.strategies.headlessbrowser.extractor.util.mal.MyAnimeListNetUtil;
import io.github.manami.dto.entities.InfoLink;

public class RelatedAnimeExtractorTest {

    private static final String DEATH_NOTE_URL_NO_PROTOCOL = MyAnimeListNetUtil.DOMAIN + "/anime/1535/Death_Note";
    private static final String EXPECTED_DEATH_NOTE_URL = "https://myanimelist.net/anime/1535";
    private static final String TEST_FILE = "related_anime_test_entry.html";
    private MyAnimeListNetRelatedAnimeExtractor sut;
    private String rawHtml;


    @BeforeMethod
    public void beforeMethod() throws IOException {
        sut = new MyAnimeListNetRelatedAnimeExtractor();
        final ClassPathResource resource = new ClassPathResource(TEST_FILE);
        final StringBuilder strBuilder = new StringBuilder();
        Files.readAllLines(resource.getFile().toPath()).forEach(strBuilder::append);
        rawHtml = strBuilder.toString();
    }


    @Test(groups = UNIT_TEST_GROUP)
    public void extractRelatedAnime() {
        // given

        // when
        final Set<InfoLink> result = sut.extractRelatedAnime(rawHtml);

        // then
        assertThat(result).isNotNull();
        assertThat(result.isEmpty()).isFalse();
        assertThat(result.size()).isEqualTo(3);
        assertThat(result.contains(new InfoLink("https://myanimelist.net/anime/849"))).isTrue();
        assertThat(result.contains(new InfoLink("https://myanimelist.net/anime/7311"))).isTrue();
        assertThat(result.contains(new InfoLink("https://myanimelist.net/anime/26351"))).isTrue();
    }


    @Test(groups = UNIT_TEST_GROUP)
    public void isResponsibleTrueHttpWww() {
        // given
        final InfoLink infoLink = new InfoLink("http://www." + DEATH_NOTE_URL_NO_PROTOCOL);

        // when
        final boolean result = sut.isResponsible(infoLink);

        // then
        assertThat(result).isTrue();
    }


    @Test(groups = UNIT_TEST_GROUP)
    public void isResponsibleTrueHttp() {
        // given
        final InfoLink infoLink = new InfoLink("http://" + DEATH_NOTE_URL_NO_PROTOCOL);

        // when
        final boolean result = sut.isResponsible(infoLink);

        // then
        assertThat(result).isTrue();
    }


    @Test(groups = UNIT_TEST_GROUP)
    public void isResponsibleTrueHttpsWww() {
        // given
        final InfoLink infoLink = new InfoLink("https://www." + DEATH_NOTE_URL_NO_PROTOCOL);

        // when
        final boolean result = sut.isResponsible(infoLink);

        // then
        assertThat(result).isTrue();
    }


    @Test(groups = UNIT_TEST_GROUP)
    public void isResponsibleTrueHttps() {
        // given
        final InfoLink infoLink = new InfoLink("https://" + DEATH_NOTE_URL_NO_PROTOCOL);

        // when
        final boolean result = sut.isResponsible(infoLink);

        // then
        assertThat(result).isTrue();
    }


    @Test(groups = UNIT_TEST_GROUP)
    public void isResponsibleFalse() {
        // given
        final InfoLink infoLink = new InfoLink("https://animenewsnetwork.com/encyclopedia/anime.php?id=6592");

        // when
        final boolean result = sut.isResponsible(infoLink);

        // then
        assertThat(result).isFalse();
    }


    @Test(groups = UNIT_TEST_GROUP)
    public void isResponsibleBlank() {
        // given
        final InfoLink urlEmpty = new InfoLink(EMPTY);
        final InfoLink urlWhitespace = new InfoLink(EMPTY);
        final InfoLink urlNull = new InfoLink(null);

        // when
        final boolean resultEmpty = sut.isResponsible(urlEmpty);
        final boolean resultWhitespace = sut.isResponsible(urlWhitespace);
        final boolean resultNull = sut.isResponsible(urlNull);

        // then
        assertThat(resultEmpty).isFalse();
        assertThat(resultWhitespace).isFalse();
        assertThat(resultNull).isFalse();
    }


    @Test(groups = UNIT_TEST_GROUP)
    public void normalizeInfoLinkQueryParameter() {
        // given
        final InfoLink infoLink = new InfoLink("https://myanimelist.net/anime.php?id=1535");

        // when
        final InfoLink result = sut.normalizeInfoLink(infoLink);

        // then
        assertThat(result.getUrl()).isEqualTo(EXPECTED_DEATH_NOTE_URL);
    }


    @Test(groups = UNIT_TEST_GROUP)
    public void normalizeInfoLinkSearch() {
        // given
        final InfoLink infoLink = new InfoLink("https://myanimelist.net/anime/1535/Death_Note?q=death%20note");

        // when
        final InfoLink result = sut.normalizeInfoLink(infoLink);

        // then
        assertThat(result.getUrl()).isEqualTo(EXPECTED_DEATH_NOTE_URL);
    }


    @Test(groups = UNIT_TEST_GROUP)
    public void normalizeInfoLinkDefault() {
        // given
        final InfoLink infoLink = new InfoLink("https://myanimelist.net/anime/1535/Death_Note");

        // when
        final InfoLink result = sut.normalizeInfoLink(infoLink);

        // then
        assertThat(result.getUrl()).isEqualTo(EXPECTED_DEATH_NOTE_URL);
    }


    @Test(groups = UNIT_TEST_GROUP, description = "If you get the expected url already it is being returned as is, but with http instead of https.")
    public void normalizeInfoLinkIdentical() {
        // given
        final InfoLink infoLink = new InfoLink("https://myanimelist.net/anime/1535/Death_Note");

        // when
        final InfoLink result = sut.normalizeInfoLink(infoLink);

        // then
        assertThat(result.getUrl()).isEqualTo(EXPECTED_DEATH_NOTE_URL);
    }


    @Test(groups = UNIT_TEST_GROUP, description = "Urls with http will be changed to https.")
    public void normalizeSchemeFromHttpToHttps() {
        // given
        final InfoLink infoLink = new InfoLink("http://myanimelist.net/anime/1535");

        // when
        final InfoLink result = sut.normalizeInfoLink(infoLink);

        // then
        assertThat(result.getUrl()).isEqualTo(EXPECTED_DEATH_NOTE_URL);
    }


    @Test(groups = UNIT_TEST_GROUP, description = "If the url does not match the expected pattern it is being returned unchanged.")
    public void normalizeInfoLinkDifferentPattern() {
        // given
        final InfoLink infoLink = new InfoLink(" https://myanimelist.net/news?_location=mal_h_m");

        // when
        final InfoLink result = sut.normalizeInfoLink(infoLink);

        // then
        assertThat(result.getUrl()).isEqualTo(infoLink.getUrl());
    }
}
