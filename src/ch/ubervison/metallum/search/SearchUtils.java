package ch.ubervison.metallum.search;

import ch.ubervison.metallum.entity.AbstractEntity;
import ch.ubervison.metallum.entity.Track;
import ch.ubervison.metallum.enums.SimpleSearchType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A utility class useful to form JSON and search URLs.
 *
 * @author ubervison
 */
public class SearchUtils {

    private static String BASE_URL = "http://www.metal-archives.com";
    private static String SIMPLE_SEARCH_PREFIX = "/search?searchString=";

    private static String AJAX_BAND_SEARCH_PREFIX = "/search/ajax-band-search/";
    private static String AJAX_ALBUM_SEARCH_PREFIX = "/search/ajax-album-search/";
    private static String AJAX_SONG_SEARCH_PREFIX = "/search/ajax-song-search/";
    private static String AJAX_LABEL_SEARCH_PREFIX = "/search/ajax-label-search/";
    private static String AJAX_ARTIST_SEARCH_PREFIX = "/search/ajax-artist-search/";
    private static String AJAX_USER_SEARCH_PREFIX = "/search/ajax-user-search/";

    /**
     * Returns a search URL for a given string and a given search type. This is the URL one would get when searching for the string on the metal archives.
     *
     * @param searchString the string we want to search for in the metal archives
     * @param searchType the type or category we want to search
     * @return a new URL object pointing to the desired search results page
     * @throws MalformedURLException
     */
    public static URL formSimpleSearchURL(String searchString, SimpleSearchType searchType) throws MalformedURLException {
        String searchURL = BASE_URL + SIMPLE_SEARCH_PREFIX + searchString;
        switch (searchType){
            case BAND_NAME:
                searchURL += "&type=band_name";
                break;
            case MUSIC_GENRE:
                searchURL += "&type=band_genre";
                break;
            case LYRICAL_THEMES:
                searchURL += "&type=band_themes";
                break;
            case ALBUM_TITLE:
                searchURL += "&type=album_title";
                break;
            case SONG_TITLE:
                searchURL += "&type=song_title";
                break;
            case LABEL:
                searchURL += "&type=label_name";
                break;
            case ARTIST:
                searchURL += "&type=artist_alias";
                break;
            case USER_PROFILE:
                searchURL += "&type=user_name";
                break;
            default:
                throw new Error("Simple search case not handled");
        }
        return new URL(searchURL);
    }

    /**
     * Returns an URL pointing directly to the raw JSON data containing the results of a given query. Useful to extract search results efficiently.
     * Note that the query is sanitized, i.e. all the spaces are replaced by "+" signs.
     *
     * @param query the desired query
     * @param queryType the type or category of the query
     * @return a new URL object pointing to the raw JSON data containing the results of the query.
     * @throws MalformedURLException
     * @throws IOException
     */
    public static URL formJSONURL(String query, SimpleSearchType queryType) throws MalformedURLException, IOException {
        String trimmedSearchString = query.replaceAll(" ", "+");
        String jsonURL = BASE_URL;
        switch (queryType){
            case BAND_NAME:
                jsonURL += AJAX_BAND_SEARCH_PREFIX + "?field=name&query=" + trimmedSearchString;
                break;
            case MUSIC_GENRE:
                jsonURL += AJAX_BAND_SEARCH_PREFIX + "?field=genre&query=" + trimmedSearchString;
                break;
            case LYRICAL_THEMES:
                jsonURL += AJAX_BAND_SEARCH_PREFIX + "?field=themes&query=" + trimmedSearchString;
                break;
            case ALBUM_TITLE:
                jsonURL += AJAX_ALBUM_SEARCH_PREFIX + "?field=title&query=" + trimmedSearchString;
                break;
            case SONG_TITLE:
                jsonURL += AJAX_SONG_SEARCH_PREFIX + "?field=title&query=" + trimmedSearchString;
                break;
            case LABEL:
                jsonURL += AJAX_LABEL_SEARCH_PREFIX + "?field=name&query=" + trimmedSearchString + "&sEcho=0";
                break;
            case ARTIST:
                jsonURL += AJAX_ARTIST_SEARCH_PREFIX + "?field=alias&query=" + trimmedSearchString;
                break;
            case USER_PROFILE:
                jsonURL += AJAX_USER_SEARCH_PREFIX + "?field=name&query=" + trimmedSearchString;
                break;
            default:
                throw new Error("Mishandeld query type case");
        }

        return new URL(jsonURL);
    }

    /**
     * Returns the raw JSON data from the given url.
     *
     * @param url the url of the desired JSON data
     * @return a new JsonObject containing the desired JSON data
     * @throws IOException
     */
    public static JsonObject getJSONData(URL url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.connect();
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        JsonReader jreader = Json.createReader(in);
        JsonObject result = jreader.readObject();

        in.close();
        jreader.close();
        con.disconnect();

        return result;
    }

    public static String getLyrics(Track track) throws IOException {
        String LYRICS_URL = "http://www.metal-archives.com/release/ajax-view-lyrics/id/" + track.getId();
        System.out.println(LYRICS_URL);
        Document lyrics_doc = Jsoup.connect(LYRICS_URL).get();
        String html = Jsoup.parse(lyrics_doc.html().replaceAll("(?i)<br[^>]*>", "br2n")).text();

        /* replace the <br> tags with \n */
        StringBuffer strBuf = new StringBuffer();
        for(String str : html.split("br2n")){
            strBuf.append(str);
            strBuf.append("\n");
        }

        return strBuf.toString();
    }
}
