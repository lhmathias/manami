package io.github.manami.cache.strategies.headlessbrowser.extractor.util.mal;


import io.github.manami.dto.entities.InfoLink;

public final class MyAnimeListNetUtil {

    /** Domain name. */
    public final static String DOMAIN = "myanimelist.net";


    private MyAnimeListNetUtil() {
    }


    public static boolean isResponsible(final InfoLink infoLink) {
        if (!infoLink.isValid()) {
            return false;
        }

        final String url = infoLink.toString();

        return url.startsWith("http://" + DOMAIN) || url.startsWith("http://www." + DOMAIN) || url.startsWith("https://" + DOMAIN) || url.startsWith("https://www." + DOMAIN);
    }
}
