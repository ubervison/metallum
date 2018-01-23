package ch.ubervison.metallum.parse.search;

import ch.ubervison.metallum.entity.Band;
import ch.ubervison.metallum.entity.Release;
import ch.ubervison.metallum.enums.ReleaseType;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class that parses search results for album titles.
 *
 * @author ubervison
 */
public class ReleaseSearchParser extends AbstractSimpleSearchParser<Release>{

    public ReleaseSearchParser(JsonObject searchResults){
        super(searchResults);
    }

    /**
     * Returns a list of the releases given by the search result
     *
     * @return a list formed of all releases that match the query
     */
    public List<Release> getEntityList(){
        JsonArray releaseArray = searchResults.getJsonArray(JSON_DATA_KEY);
        List<Release> releases = new ArrayList<>();
        for (JsonValue v : releaseArray){
            if(v.getValueType() == JsonValue.ValueType.ARRAY){
                String band = ((JsonArray)v).getJsonString(0).getString();
                String title = ((JsonArray)v).getJsonString(1).getString();
                String type = ((JsonArray)v).getJsonString(2).getString();
                String date = ((JsonArray)v).getJsonString(3).getString();

                String releaseTitle = "";
                ReleaseType releaseType;
                String releaseDate = "";
                long releaseId = 0;

                Pattern releasePat = Pattern.compile("<a href=\"" + BASE_URL + "/albums/.*/.*/(?<id>[0-9]+)\">(?<title>.*)</a> <!-- .* -->");
                Pattern datePat = Pattern.compile("(?<full>.*) <!-- (?<standard>[0-9]{4}-[0-9]{2}-[0-9]{2}) -->");

                Matcher releaseMatcher = releasePat.matcher(title);
                if(releaseMatcher.matches()){
                    releaseTitle = releaseMatcher.group("title");
                    releaseId = Long.parseLong(releaseMatcher.group("id"));
                }

                Matcher dateMatcher = datePat.matcher(date);
                if(dateMatcher.matches()){
                    releaseDate = dateMatcher.group("standard");
                }

                releaseType = ReleaseType.parseReleaseType(type);

                Release r = new Release(releaseId, releaseTitle);

                Pattern bandPat = Pattern.compile("<a href=\"" + BASE_URL + "/bands/.*/(?<id>[0-9]+)\" title=\".*\">(?<name>.*)</a>");

                if(releaseType == ReleaseType.SPLIT || releaseType == ReleaseType.SPLIT_VIDEO){
                    String[] bands = band.split(" \\| ");
                    for(String s : bands) {
                        String bandName = "";
                        long bandId = 0;
                        Matcher bandMatcher = bandPat.matcher(s);
                        if(bandMatcher.matches()){
                            bandName = bandMatcher.group("name");
                            bandId = Long.parseLong(bandMatcher.group("id"));
                        }
                        r.addSplitBands(new Band(bandId, bandName));
                    }
                }
                else{
                    String bandName = "";
                    long bandId = 0;
                    Matcher bandMatcher = bandPat.matcher(band);
                    if(bandMatcher.matches()){
                        bandName = bandMatcher.group("name");
                        bandId = Long.parseLong(bandMatcher.group("id"));
                    }
                    r.setBand(new Band(bandId, bandName));
                }

                r.setType(releaseType);
                r.setReleaseDate(releaseDate);
                releases.add(r);
            }
        }
        return releases;
    }

}
