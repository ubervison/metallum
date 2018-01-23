package ch.ubervison.metallum.parse.site;

import ch.ubervison.metallum.entity.*;
import ch.ubervison.metallum.enums.BandStatus;
import ch.ubervison.metallum.enums.Country;
import ch.ubervison.metallum.enums.LinkCategory;
import ch.ubervison.metallum.enums.ReleaseType;
import ch.ubervison.metallum.search.SearchUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A class that parses the page of a given band.
 *
 * @author ubervison
 */
public class BandSiteParser extends AbstractSiteParser<Band> {

    private enum ParseMemberScope {COMPLETE_LINEUP, CURRENT_LINEUP, PAST_MEMBERS, LIVE_MUSICIANS}

    public BandSiteParser(Band band) throws IOException{
        super(band);
    }

    /**
     * Returns the band with the additional details found on the band's page.
     *
     * @return the band with updated information from the band's page
     * @throws IOException
     */
    public Band parse() throws IOException {
        parseInfos(entity);
        parseDiscography(entity);
        parseMembers(entity, ParseMemberScope.COMPLETE_LINEUP);
        parseLinks(entity);
        parseReviews(entity);
        parseRecommendations(entity);
        entity.setInfo(getComments());
        entity.printData();
        return entity;
    }

    private void parseInfos(Band band){
        long startTime = System.nanoTime();
        Element band_stats = doc.getElementById("band_stats");
        Elements dd = band_stats.getElementsByTag("dd"); // list of "dd" html tags. order matters
        /* we take each info item and put them in a list */
        List<String> infos = dd.stream().map(element -> element.html()).collect(Collectors.toList());

        band.setCountry(Country.parseCountry(infos.get(0).replaceAll("<a href=\".*\">|</a>","")));
        band.setLocation(infos.get(1));
        band.setStatus(BandStatus.parseBandStatus(infos.get(2)));
        band.setYearFormed(Integer.parseInt(infos.get(3)));
        band.setGenre(infos.get(4));
        band.setLyricalThemes(infos.get(5));

        String labelRaw = infos.get(6);
        String labelName = labelRaw.replaceAll("<a href=\".*\">|</a>", "");

        /* Label id is 0 if unsigned/independent */
        long labelId = 0;
        if(!labelName.equals("Unsigned/independent")){
            labelId = Long.parseLong(labelRaw.replaceAll("<a href=\"" + BASE_URL + "/labels/.*/([0-9]+)\">.*</a>", "$1"));
        }
        band.setLabel(new Label(labelId, labelName));

        band.setYearsActive(infos.get(7));

        long endTime = System.nanoTime();
        System.out.println("\nInfos parsing time : " + (endTime - startTime)/1000000 + " ms");
    }

    private String getComments() throws IOException {
        long startTime = System.nanoTime();
        String COMMENT_URL = "" + BASE_URL + "/band/read-more/id/" + entity.getId();
        Document doc = Jsoup.connect(COMMENT_URL).get();
        String html = Jsoup.parse(doc.html().replaceAll("(?i)<br[^>]*>", "br2n")).text();

        /* replace the <br> tags with \n */
        StringBuffer strBuf = new StringBuffer();
        for(String str : html.split("br2n")){
            strBuf.append(str);
            strBuf.append("\n");
        }

        long endTime = System.nanoTime();
        System.out.println("\nComments parsing time : " + (endTime - startTime)/1000000 + " ms");

        return strBuf.toString();
    }

    private void parseDiscography(Band band) throws IOException {
        long startTime = System.nanoTime();
        String DISCO_URL = "" + BASE_URL + "/band/discography/id/" + band.getId() + "/tab/all";
        Document disco_doc = Jsoup.connect(DISCO_URL).get();
        Element disco = disco_doc.getElementsByTag("tbody").first();
        Elements releases = disco.getElementsByTag("tr");
        /* each element is a discography item (release) */
        for(Element release : releases){
            Elements item_elements = release.getElementsByTag("td");
            /* we take each release element and put it into a list */
            List<String> infos = item_elements.stream().map(element -> element.html()).collect(Collectors.toList());

            String[] name_id = infos.get(0).replaceAll("<a href=\"" + BASE_URL + "/albums/.*/.*/([0-9]+)\" class=.*>(.*)</a>", "$1" + "\n" + "$2").split("\n");
            Release r = new Release(Long.parseLong(name_id[0]), name_id[1]);

            ReleaseType type = ReleaseType.parseReleaseType(infos.get(1));
            r.setType(type);

            String date = infos.get(2);
            r.setReleaseDate(date);

            String reviews = infos.get(3);
            if(reviews.equals("&nbsp;")){
                r.setHasReviews(false);
            }
            else{
                r.setHasReviews(true);
                Pattern reviewPat = Pattern.compile("([0-9]+) (([0-9]+)%)");
                Matcher reviewMatcher = reviewPat.matcher(reviews);
                if(reviewMatcher.matches()){
                    r.setReviewCount(Integer.parseInt(reviewMatcher.group("$1")));
                    r.setReviewAverage(Integer.parseInt(reviewMatcher.group("$2")));
                }
            }
            band.addRelease(r);
        }

        long endTime = System.nanoTime();
        System.out.println("\nDiscography parsing time : " + (endTime - startTime)/1000000 + " ms");
    }

    private void parseMembers(Band band, ParseMemberScope scope){
        long startTime = System.nanoTime();
        Map<Member, String> lineUp = new HashMap<>();
        String scope_div = "";
        switch(scope){
            case COMPLETE_LINEUP:
                parseMembers(band, ParseMemberScope.CURRENT_LINEUP);
                parseMembers(band,ParseMemberScope.PAST_MEMBERS);
                parseMembers(band, ParseMemberScope.LIVE_MUSICIANS);
                return;
            case CURRENT_LINEUP:
                scope_div = "band_tab_members_current";
                break;
            case PAST_MEMBERS:
                scope_div = "band_tab_members_past";
                break;
            case LIVE_MUSICIANS:
                scope_div = "band_tab_members_live";
                break;
            default:
                throw new Error("undefined case");
        }

        Element scope_div_elem = doc.getElementById(scope_div);
        if(scope_div_elem != null) { /* null means there are no members fit this scope */
            Elements members = scope_div_elem.getElementsByClass("lineUpRow");
            for (Element member : members) {
                List<String> infos = member.select("td").stream().map(info -> info.html()).collect(Collectors.toList());
                String[] name_id = infos.get(0).replaceAll("<a href=\"" + BASE_URL + "/artists/.*/([0-9]+)\" class=.*>(.*)</a>", "$1" + "\n" + "$2").split("\n");
                long id = Long.parseLong(name_id[0]);
                String name = name_id[1];
                Member m = new Member(id, name);
                String role = infos.get(1).replaceAll("&nbsp;", " ");
                lineUp.put(m, role);
            }
        }

        switch(scope){
            case CURRENT_LINEUP:
                band.setCurrentLineup(lineUp);
                break;
            case PAST_MEMBERS:
                band.setPastLineup(lineUp);
                break;
            case LIVE_MUSICIANS:
                band.setLiveLineup(lineUp);
                break;
            default:
                throw new Error("undefined case");
        }
        long endTime = System.nanoTime();
        System.out.println("\nMembers parsing time : " + (endTime - startTime)/1000000 + " ms");
    }

    private void parseLinks(Band band) throws IOException {
        long startTime = System.nanoTime();
        String LINKS_URL = "" + BASE_URL + "/link/ajax-list/type/band/id/" + band.getId();
        Document links_doc = Jsoup.connect(LINKS_URL).get();
        /* order matters */
        List<String> div_ids = Arrays.asList("band_links_Official", "band_links_Official_merchandise", "band_links_Unofficial", "band_links_Tablatures", "band_links_Labels");
        LinkCategory[] categories = LinkCategory.values();

        for(String div_id : div_ids){
            Element div = links_doc.getElementById(div_id);
            if(div != null) { /* null means there are no links for this category */
                Elements links = div.getElementsByTag("a");
                LinkCategory category = categories[div_ids.indexOf(div_id)];
                for (Element link : links) {
                    Pattern linkPat = Pattern.compile("<a id=\".*\" href=\"(?<url>.*)\" title=\".*\" target=\"_blank\">(?<name>.*)</a>");
                    Matcher m = linkPat.matcher(link.toString());
                    if (m.matches()) {
                        Link bandLink = new Link(m.group("url"), m.group("name"));
                        bandLink.setCategory(category);
                        band.addLinks(bandLink);
                    }
                }
            }
        }
        long endTime = System.nanoTime();
        System.out.println("\nLinks parsing time : " + (endTime - startTime)/1000000 + " ms");
    }

    private void parseReviews(Band band) throws IOException {
        long startTime = System.nanoTime();
        URL REVIEW_URL = new URL("" + BASE_URL + "/review/ajax-list-band/id/" + band.getId() + "/json/1?sEcho=1&iColumns=4&sColumns=&iDisplayStart=0&iDisplayLength=200&mDataProp_0=0&mDataProp_1=1&mDataProp_2=2&mDataProp_3=3&iSortCol_0=3");
        JsonArray reviewArray = SearchUtils.getJSONData(REVIEW_URL).getJsonArray(JSON_DATA_KEY);
        List<Review> reviews = new ArrayList<>();
        for(JsonValue v : reviewArray){
            if(v.getValueType() == JsonValue.ValueType.ARRAY){
                String name = ((JsonArray)v).getJsonString(0).getString();
                int score = Integer.parseInt(((JsonArray) v).getJsonString(1).getString().replaceAll("%", ""));
                String user = ((JsonArray)v).getJsonString(2).getString();
                String date = ((JsonArray)v).getJsonString(3).getString();

                String releaseName = "";
                String userName = "";
                long release_id = 0;
                long review_id = 0;

                Pattern releasePat = Pattern.compile("<a href=\'" + BASE_URL + "/reviews/.*/.*/(?<releaseid>[0-9]+)/(?<user>.*)/(?<reviewid>[0-9]+)\'>(?<name>.*)</a>");
                Matcher releaseMatcher = releasePat.matcher(name);
                if(releaseMatcher.matches()){
                    releaseName = releaseMatcher.group("name");
                    release_id = Long.parseLong(releaseMatcher.group("releaseid"));
                    review_id = Long.parseLong(releaseMatcher.group("reviewid"));
                }

                Pattern userPat = Pattern.compile("<a href=\"" + BASE_URL + "/users/.*\" class=\"profileMenu\">(?<user>.*)</a>");
                Matcher userMatcher = userPat.matcher(user);
                if(userMatcher.matches()){
                    userName = userMatcher.group("user");
                }

                Release release = new Release(release_id, releaseName);
                Review review = new Review(review_id);
                review.setDate(date);
                review.setScore(score);
                review.setAuthor(userName);
                review.setRelease(release);

                reviews.add(review);
            }
        }
        band.setReviews(reviews);
        long endTime = System.nanoTime();
        System.out.println("\nTime to get reviews : " + (endTime - startTime)/1000000 + " ms");
    }

    private void parseRecommendations(Band band) throws IOException {
        String RECS_URL = "" + BASE_URL + "/band/ajax-recommendations/id/" + band.getId() + "/showMoreSimilar/1";
        Document recs_doc = Jsoup.connect(RECS_URL).get();
        Element noArtist = doc.getElementById("no_artists"); /* we check if there are no recommendations */
        if(noArtist == null) {
            Element recs = recs_doc.getElementsByTag("tbody").first();
            Elements rec_bands = recs.getElementsByTag("tr");
            rec_bands.remove(rec_bands.size() - 1); /* we remove the last <tr> tag */
            /* each element is a recommended band */
            for (Element rec_band : rec_bands) {
                Elements item_elements = rec_band.getElementsByTag("td");
                  /* we take each band element and put it into a string */
                List<String> infos = item_elements.stream().map(element -> element.html()).collect(Collectors.toList());

                String name = "";
                long id = 0;

                Pattern nameIdPat = Pattern.compile("<a href=\"" + BASE_URL + "/bands/.*/(?<id>[0-9]+)\">(?<name>.*)</a>");
                Matcher nameIdMatcher = nameIdPat.matcher(infos.get(0));
                if (nameIdMatcher.matches()) {
                    name = nameIdMatcher.group("name");
                    id = Long.parseLong(nameIdMatcher.group("id"));
                }

                Country country = Country.parseCountry(infos.get(1));
                String genre = infos.get(2);
                int score = Integer.parseInt(infos.get(3).replaceAll("<span id=\".*\">([0-9]+) </span>", "$1"));

                Band b = new Band(id, name);
                b.setCountry(country);
                b.setGenre(genre);

                band.addRecommendation(b, score);
            }
        }
    }
}
