package io.github.manami.cache.strategies.headlessbrowser.extractor.anime.mal;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import io.github.manami.cache.strategies.headlessbrowser.extractor.Extractor;
import io.github.manami.cache.strategies.headlessbrowser.extractor.anime.AbstractAnimeSitePlugin;
import io.github.manami.cache.strategies.headlessbrowser.extractor.util.mal.MyAnimeListNetUtil;
import io.github.manami.dto.AnimeType;
import io.github.manami.dto.entities.InfoLink;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to gather information from myanimelist.net automatically.
 *
 * @author manami-project
 * @since 2.0.0
 */
@Named
@Extractor
public class MyAnimeListNetAnimeExtractor extends AbstractAnimeSitePlugin {

  private static final Logger log = LoggerFactory.getLogger(MyAnimeListNetAnimeExtractor.class);
  public static final String UNABLE_TO_PARSE_URL_MESSAGE = "Unable to parse url: ";

  /**
   * Regex Pattern.
   */
  private Pattern pattern;

  /**
   * Regex matcher.
   */
  private Matcher matcher;

  /**
   * String which is shown on the site in case an id does not exists.
   */
  private final static String INVALID_REQUEST = "Invalid Request";

  /**
   * Request unsuccessful by incapsula.
   */
  private static final String REQUEST_UNSUCCESSFUL = "Request unsuccessful. Incapsula incident ID";


  @Override
  protected boolean isValidInfoLink() {
    return !siteContent.contains(INVALID_REQUEST) && !siteContent.contains(REQUEST_UNSUCCESSFUL);
  }


  @Override
  protected String extractTitle() {
    pattern = Pattern.compile("<h1 class=\"h1\">.+</h1>");
    matcher = pattern.matcher(siteContent);
    String title = null;

    if (matcher.find()) {
      title = matcher.group();
      title = title.replace("<h1 class=\"h1\"><span itemprop=\"name\">", EMPTY).replace("</span></h1>", EMPTY);
      title = title.trim();
    }

    if (isNotBlank(title) && title.contains("404 Not Found")) {
      title = null;
    }

    return title;
  }


  @Override
  protected AnimeType extractType() {
    pattern = Pattern.compile("<span class=\"dark_text\">(.??)Type:(.??)</span>(.+?)</a>");
    matcher = pattern.matcher(siteContent);
    AnimeType type = null;

    if (matcher.find()) {
      String subStr = matcher.group();

      pattern = Pattern.compile("type=(.+?)\\\">(.*?)</a>");
      matcher = pattern.matcher(subStr);

      if (matcher.find()) {
        subStr = matcher.group();
        subStr = subStr.replace("</a>", EMPTY);
        subStr = subStr.substring(subStr.lastIndexOf(">")).replace(">", EMPTY);
        subStr = subStr.trim();
        type = AnimeType.findByName(subStr);
      }
    }

    if (type == null) {
      log.debug("Could not find any type in siteContent. Fallback: Setting type to TV");
      type = AnimeType.TV;
    }

    return type;
  }


  @Override
  protected String extractEpisodes() {
    pattern = Pattern.compile("<span class=\"dark_text\">(.??)Episodes:(.??)</span>(.+?)</div>");
    matcher = pattern.matcher(siteContent);
    String episodes = "1";

    if (matcher.find()) {
      episodes = matcher.group();

      pattern = Pattern.compile("</span>(.*?)</div>");
      matcher = pattern.matcher(episodes);

      if (matcher.find()) {
        episodes = matcher.group();
        episodes = episodes.replace("</span>", EMPTY).replace("</div>", EMPTY);
        episodes = episodes.trim();
      }
    }

    return episodes;
  }


  @Override
  protected URL extractPictureLink() {
    pattern = Pattern.compile("https://myanimelist\\.cdn-dena\\.com/images/anime/[0-9]+/[0-9]+\\.jpg");
    matcher = pattern.matcher(siteContent);
    String picture = null;

    if (matcher.find()) {
      picture = matcher.group();
    }

    if (isNotBlank(picture)) {
      try {
        return new URL(picture);
      } catch (MalformedURLException e) {
        log.error(UNABLE_TO_PARSE_URL_MESSAGE, e);
      }
    }

    return null;
  }


  @Override
  protected URL extractThumbnail() {
    URL picture = extractPictureLink();
    String thumbnailUrl;

    if (picture != null) {
      final StringBuilder strBuilder = new StringBuilder(picture.toString());
      thumbnailUrl = strBuilder.insert(picture.toString().length() - 4, "t").toString();

      try {
        return new URL(thumbnailUrl);
      } catch (MalformedURLException e) {
        log.error(UNABLE_TO_PARSE_URL_MESSAGE, e);
      }
    }

    return null;
  }


  @Override
  public InfoLink normalizeInfoLink(final InfoLink url) {
    return MyAnimeListNetUtil.normalizeInfoLink(url);
  }


  @Override
  public boolean isResponsible(final InfoLink url) {
    return MyAnimeListNetUtil.isResponsible(url);
  }
}