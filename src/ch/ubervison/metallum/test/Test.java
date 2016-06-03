package ch.ubervison.metallum.test;

import ch.ubervison.metallum.entity.Release;
import ch.ubervison.metallum.enums.SimpleSearchType;
import ch.ubervison.metallum.parse.search.BandSearchParser;
import ch.ubervison.metallum.parse.search.ReleaseSearchParser;
import ch.ubervison.metallum.parse.site.AbstractSiteParser;
import ch.ubervison.metallum.parse.site.BandSiteParser;
import ch.ubervison.metallum.parse.site.ReleaseSiteParser;
import ch.ubervison.metallum.search.SimpleSearchQuery;

import java.util.List;

/**
 * Created by ubervison on 25.10.15.
 */
public class Test {

    public static void main(String[] args) throws Exception {

        long startTime = System.nanoTime();

        AbstractSiteParser siteParser = new ReleaseSiteParser(new ReleaseSearchParser(new SimpleSearchQuery("Reign in Blood", SimpleSearchType.ALBUM_TITLE).getJSONData()).getEntityList().get(0));
        siteParser.parse();

        long endTime = System.nanoTime();
        long running_time = endTime - startTime;
        System.out.println("\nRunning time : " + running_time/1000000 + " ms");
    }
}
