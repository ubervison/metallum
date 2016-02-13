package ch.ubervison.metallum.parse.search;

import ch.ubervison.metallum.entity.Label;
import ch.ubervison.metallum.enums.Country;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class that parses search results for labels.
 *
 * @author ubervison
 */
public class LabelSearchParser extends AbstractSimpleSearchParser<Label> {

    public LabelSearchParser(JsonObject searchResults){
        super(searchResults);
    }

    /**
     * Return a list of all the bands given by the search result.
     *
     * @return a list of all the bands that match the query
     */
    public List<Label> getEntityList(){
        JsonArray labelArray = searchResults.getJsonArray(JSON_DATA_KEY);
        List<Label> labels = new ArrayList<>();
        for(JsonValue v : labelArray){
            if(v.getValueType() == JsonValue.ValueType.ARRAY){
                String label = ((JsonArray)v).getJsonString(0).getString();
                String country = ((JsonArray)v).getJsonString(1).getString();
                String specialization = ((JsonArray)v).getJsonString(2).getString();

                String labelName = "";
                long labelId = 0;

                Pattern labelPat = Pattern.compile("<a href=\"http://www.metal-archives.com/labels/.*/(?<id>[0-9]+)\">(?<name>.*)</a>");

                Matcher labelMatcher = labelPat.matcher(label);
                if(labelMatcher.matches()){
                    labelName = labelMatcher.group("name");
                    labelId = Long.parseLong(labelMatcher.group("id"));
                }

                Label l = new Label(labelId, labelName);
                l.setCountry(Country.parseCountry(country));
                l.setSpecialization(specialization);

                labels.add(l);
            }
        }
        return labels;
    }
}
