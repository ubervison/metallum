package ch.ubervison.metallum.parse.site;

import ch.ubervison.metallum.entity.Label;
import ch.ubervison.metallum.entity.Release;
import ch.ubervison.metallum.entity.Track;
import ch.ubervison.metallum.enums.ReleaseFormat;
import ch.ubervison.metallum.enums.ReleaseType;
import ch.ubervison.metallum.search.SearchUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A class that parses the page of a given entity
 *
 * @author ubervison
 */
public class ReleaseSiteParser extends AbstractSiteParser<Release> {

    public ReleaseSiteParser(Release release) throws IOException{
        super(release);
    }

    public Release parse() throws IOException {
        parseInfos(entity);
        parseTrackList(entity);
        parseAllLyrics(entity);
        entity.printData();
        return entity;
    }

    private void parseInfos(Release release){
        Element release_stats = doc.getElementById("album_info");
        Elements dd = release_stats.getElementsByTag("dd"); /* list of <dd> tags. order matters */
        List<String> infos = dd.stream().map(element -> element.html()).collect(Collectors.toList());

        release.setType(ReleaseType.parseReleaseType(infos.get(0)));
        release.setReleaseDate(infos.get(1));
        release.setCatalogId(infos.get(2));

        String labelRaw = infos.get(3);
        String labelName = labelRaw.replaceAll("<a href=\".*\">|</a>", "");
        long labelId = 0;
        if(!labelRaw.equals("Independent")){
            labelId = Long.parseLong(labelRaw.replaceAll("<a href=\"" + BASE_URL + "/labels/.*/([0-9]+)#label_tabs_albums\">.*</a>", "$1"));
        }
        Label label = new Label(labelId, labelName);
        release.setLabel(label);

        release.setFormat(ReleaseFormat.parseReleaseFormat(infos.get(4)));

        String reviewRaw = infos.get(5).replace("\n", "");
        release.setReviewCount(0);
        release.setHasReviews(false);
        if(!reviewRaw.equals("None yet")){
            String[] reviewCountAndAverage = reviewRaw.replaceAll("([0-9]+) <a href=\".*\">reviews</a> \\(avg. ([0-9]+)%\\)", "$1" + "," + "$2").split(",");
            release.setReviewCount(Integer.parseInt(reviewCountAndAverage[0]));
            release.setReviewAverage(Integer.parseInt(reviewCountAndAverage[1]));
            release.setHasReviews(true);
        }
    }

    private void parseTrackList(Release release){
        List<Track> tracklist = new ArrayList<>();
        String tracklist_div = "album_tabs_tracklist";
        Element tracklist_div_elem = doc.getElementById(tracklist_div);
        Elements tracks = tracklist_div_elem.select(".even, .odd");
        for(Element track : tracks){
            List<String> infos = track.select("td").stream().map(info -> info.html()).collect(Collectors.toList());
            String[] idNumber = infos.get(0).replaceAll("<a name=\"([0-9]+)[A-Z]*\" class=\"anchor\"> </a>([0-9]+).", "$1" + "," + "$2").split(","); //
            long id = Long.parseLong(idNumber[0]);
            int number = Integer.parseInt(idNumber[1]);
            String title = infos.get(1);
            String playTime = infos.get(2);
            Track t = new Track(id, title);
            t.setTrackNumber(number);
            t.setPlayTime(playTime);
            t.setFromRelease(release);
            tracklist.add(t);
        }
        entity.setTracklist(tracklist);
    }

    private void parseAllLyrics(Release release) throws IOException {
        for(Track t : release.getTracklist()){
            t.setLyrics(SearchUtils.getLyrics(t));
        }
    }
}
