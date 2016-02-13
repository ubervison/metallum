package ch.ubervison.metallum.search;

import ch.ubervison.metallum.enums.SimpleSearchType;
import javax.json.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A class that represent a simple search query to metal-archives.com.
 *
 * @author ubervison
 */
public class SimpleSearchQuery {

    private String query;
    private SimpleSearchType queryType;

    /**
     * Constructs a new simple search query with the given search string and query type.
     *
     * @param query the character string we wish to search for
     * @param queryType the type of the query (e.g. band name, release name, song, etc)
     */
    public SimpleSearchQuery(String query, SimpleSearchType queryType){
        this.query = query;
        this.queryType = queryType;
    }

    /**
     * A method that returns a JSONObject containing the results of the search query.
     * If the query contains spaces, they will be replaced by "+" signs.
     *
     * @return a new JSONObject containing the results of the search query
     */
    public JsonObject getJSONData() throws IOException {
        long startTime = System.nanoTime();
        URL url = SearchUtils.formJSONURL(query, queryType);

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

        long endTime = System.nanoTime();
        System.out.println("\nTime to get query results : " + (endTime - startTime)/1000000 + " ms");

        return result;
    }
}
