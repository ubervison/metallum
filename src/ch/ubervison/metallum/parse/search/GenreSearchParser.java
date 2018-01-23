package ch.ubervison.metallum.parse.search;

import ch.ubervison.metallum.entity.Band;
import ch.ubervison.metallum.enums.Country;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class that parses search results for a genre.
 *
 * @author ubervison
 */
public class GenreSearchParser extends AbstractSimpleSearchParser {

    public GenreSearchParser(JsonObject searchResults){
        super(searchResults);
    }

    /**
     * Return a list of the bands given by the search result.
     *
     * @return a list of all bands that match the query
     */
    public List<Band> getEntityList(){
        JsonArray bandArray = searchResults.getJsonArray(JSON_DATA_KEY);
        List<Band> bands = new ArrayList<>();
        for (JsonValue v : bandArray) {
            if(v.getValueType() == JsonValue.ValueType.ARRAY){
                String band = ((JsonArray)v).getJsonString(0).getString();
                String genre = ((JsonArray)v).getJsonString(1).getString();
                String country = ((JsonArray)v).getJsonString(2).getString();

                String bandName = "";
                String aka = null;
                long bandId = 0;

                Pattern bandPat = Pattern.compile("<a href=\"" + BASE_URL + "/bands/.*/(?<id>[0-9]+)\">(?<name>.*)</a>(<strong>(.*)</strong>(.*)|(.*))<!-- .* -->");

                Matcher bandMatcher = bandPat.matcher(band);
                if(bandMatcher.matches()){
                    bandName = bandMatcher.group("name");
                    bandId = Long.parseLong(bandMatcher.group("id"));
                    aka = bandMatcher.group(3).replaceAll("<[^>]*>","");
                }

                Band b = new Band(bandId, bandName);
                b.setGenre(genre);
                b.setCountry(Country.parseCountry(country));
                b.setAlternateSpelling(aka);
                bands.add(b);
            }
        }
        return bands;
    }
}
