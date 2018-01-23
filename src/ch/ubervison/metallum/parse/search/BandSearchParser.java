package ch.ubervison.metallum.parse.search;

import ch.ubervison.metallum.entity.Band;
import ch.ubervison.metallum.enums.Country;

import javax.json.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class that parses search results for bands.
 *
 * @author ubervison
 */
public class BandSearchParser extends AbstractSimpleSearchParser<Band>{

    public BandSearchParser(JsonObject searchResults){
        super(searchResults);
    }

    /**
     * Returns a list of the bands given by the search result.
     *
     * @return a list formed of all bands that match the query
     */
    public List<Band> getEntityList(){
        long startTime = System.nanoTime();
        JsonArray bandArray = searchResults.getJsonArray(JSON_DATA_KEY);
        List<Band> bands = new ArrayList<>();
        for (JsonValue v : bandArray) {
            if(v.getValueType() == JsonValue.ValueType.ARRAY){
                String name = ((JsonArray)v).getJsonString(0).getString();
                String genre = ((JsonArray)v).getJsonString(1).getString();
                String country = ((JsonArray)v).getJsonString(2).getString();

                String bandName = "";
                String aka = null;
                long id = 0;

                Pattern pattern = Pattern.compile("<a href=\"" + BASE_URL + "/bands/.*/(?<id>[0-9]+)\">(?<name>.*)</a>(<strong>(.*)</strong>(.*)|(.*))<!-- .* -->");

                Matcher m = pattern.matcher(name);
                if(m.matches()) {
                    bandName = m.group("name");
                    id = Long.parseLong(m.group("id"));
                    aka = m.group(3).replaceAll("<[^>]*>","");
                }

                Band b = new Band(id, bandName);
                b.setGenre(genre);
                b.setCountry(Country.parseCountry(country));
                b.setAlternateSpelling(aka);
                bands.add(b);
            }
        }
        long endTime = System.nanoTime();
        System.out.println("\nSearch parsing time : " + (endTime - startTime)/1000000 + " ms");
        return bands;
    }
}
