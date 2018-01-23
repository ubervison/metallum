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
 * A class that parses search results for lyrical themes.
 *
 * @author ubervison
 */
public class LyricalSearchParser extends AbstractSimpleSearchParser<Band> {

    public LyricalSearchParser(JsonObject searchResults){
        super(searchResults);
    }

    /**
     * Return a list of the bands given by the search query.
     *
     * @return a list formed of all bands that match the result
     */
    public List<Band> getEntityList(){
        JsonArray bandArray = searchResults.getJsonArray(JSON_DATA_KEY);
        List<Band> bands = new ArrayList<>();
        for(JsonValue v : bandArray){
            if(v.getValueType() == JsonValue.ValueType.ARRAY){
                String band = ((JsonArray)v).getJsonString(0).getString();
                String genre = ((JsonArray)v).getJsonString(1).getString();
                String coutry = ((JsonArray)v).getJsonString(2).getString();
                String themes = ((JsonArray)v).getJsonString(3).getString();

                String bandName = "";
                String aka = "";
                long bandId = 0;

                Pattern bandPat = Pattern.compile("<a href=\"" + BASE_URL + "/bands/.*/(?<id>[0-9]+)\">(?<name>.*)</a>(<strong>(.*)</strong>(.*)|(.*))<!-- .* -->");

                Matcher bandMatcher = bandPat.matcher(band);
                if(bandMatcher.matches()){
                    bandName = bandMatcher.group("name");
                    bandId = Long.parseLong(bandMatcher.group("id"));
                    aka = bandMatcher.group(3).replaceAll("<[^>]*>","");
                }

                Band b = new Band(bandId, bandName);
                b.setCountry(Country.parseCountry(coutry));
                b.setAlternateSpelling(aka);
                b.setGenre(genre);
                b.setLyricalThemes(themes);

                bands.add(b);
            }
        }
        return bands;
    }
}
