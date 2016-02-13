package ch.ubervison.metallum.parse.search;

import ch.ubervison.metallum.entity.Band;
import ch.ubervison.metallum.entity.Release;
import ch.ubervison.metallum.entity.Track;
import ch.ubervison.metallum.enums.ReleaseType;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class that parses search results for tracks.
 *
 * @author ubervison
 */
public class TrackSearchParser extends AbstractSimpleSearchParser<Track>{

    public TrackSearchParser(JsonObject searchResults){
        super(searchResults);
    }

    /**
     * Returns a list of the tracks given by the search results.
     *
     * @return a list formed of all the tracks that match the query
     */
    public List<Track> getEntityList(){
        JsonArray trackArray = searchResults.getJsonArray(JSON_DATA_KEY);
        List<Track> tracks = new ArrayList<>();
        for(JsonValue v : trackArray){
            if(v.getValueType() == JsonValue.ValueType.ARRAY){
                String band = ((JsonArray)v).getJsonString(0).getString();
                String release = ((JsonArray)v).getJsonString(1).getString();
                String type = ((JsonArray)v).getJsonString(2).getString();
                String title = ((JsonArray)v).getJsonString(3).getString();

                String bandName = "";
                String releaseTitle = "";
                String splitName = "";
                ReleaseType releaseType;
                long bandId = 0;
                long releaseId = 0;

                Pattern bandPat = Pattern.compile("(<a href=\"http://www.metal-archives.com/bands/.*/(?<id>[0-9]+)\" title=\".*\">(?<name>.*)</a>|<span title=\"This band participates on a split, but is not listed on the site.\">(?<splitName>.*)</span>)");
                Pattern releasePat = Pattern.compile("<a href=\"http://www.metal-archives.com/albums/.*/.*/(?<id>[0-9]+)\">(?<title>.*)</a>");

                Matcher bandMatcher = bandPat.matcher(band);
                if(bandMatcher.matches()){
                    bandName = bandMatcher.group("name");
                    String bandIdString = bandMatcher.group("id");
                    if(bandIdString != null) {
                        bandId = Long.parseLong(bandMatcher.group("id"));
                    }
                    splitName = bandMatcher.group("splitName");
                }

                Matcher releaseMatcher = releasePat.matcher(release);
                if(releaseMatcher.matches()){
                    releaseTitle = releaseMatcher.group("title");
                    releaseId = Long.parseLong(releaseMatcher.group("id"));
                }

                releaseType = ReleaseType.parseReleaseType(type);

                // band associated with this track. not sure what to do with it.
                Band b = new Band(bandId, bandName);
                Release r = new Release(releaseId, releaseTitle);
                r.setType(releaseType);
                Track t = new Track(r.getId(), title);
                t.setFromRelease(r);
                t.setSplitBandName(splitName);
                tracks.add(t);
            }
        }
        return tracks;
    }
}
