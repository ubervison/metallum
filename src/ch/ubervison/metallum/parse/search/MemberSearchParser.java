package ch.ubervison.metallum.parse.search;

import ch.ubervison.metallum.entity.Member;
import ch.ubervison.metallum.enums.Country;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class that parses search results for members.
 *
 * @author ubervison
 */
public class MemberSearchParser extends AbstractSimpleSearchParser {

    public MemberSearchParser(JsonObject searchResults){
        super(searchResults);
    }

    /**
     * Returns a list of the members given by the search results.
     *
     * @return a list formed of all the members that match the query
     */
    public List<Member> getEntityList(){
        JsonArray memberArray = searchResults.getJsonArray(JSON_DATA_KEY);
        List<Member> members = new ArrayList<>();
        for(JsonValue v : memberArray){
            if(v.getValueType() == JsonValue.ValueType.ARRAY){
                String name = ((JsonArray)v).getJsonString(0).getString();
                String realName = ((JsonArray)v).getJsonString(1).getString();
                String country = ((JsonArray)v).getJsonString(2).getString();
                // string containing html links to this member's bands. not sure what to do with it.
                String bands = ((JsonArray)v).getJsonString(3).getString();

                String memberName = "";
                String aka = "";
                long memberId = 0;

                Pattern memberPat = Pattern.compile("<a href=\"http://www.metal-archives.com/artists/.*/(?<id>[0-9]+)\">(?<name>.*)</a>(?<aka>.*)");

                Matcher memberMatcher = memberPat.matcher(name);
                if(memberMatcher.matches()){
                    memberName = memberMatcher.group("name");
                    memberId = Long.parseLong(memberMatcher.group("id"));
                    aka = memberMatcher.group("aka");
                }

                Country memberCountry = Country.parseCountry(country);

                Member m = new Member(memberId, memberName);
                m.setRealName(realName);
                m.setCountry(memberCountry);
                m.setAka(aka);
                members.add(m);
            }
        }
        return members;
    }
}
