package ch.ubervison.metallum.entity;

import ch.ubervison.metallum.enums.ReleaseFormat;
import ch.ubervison.metallum.enums.ReleaseType;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a release, such as an EP, Full-length or Compilation.
 *
 * @author ubervison
 */
public class Release extends AbstractEntity implements Accessible {

    private ReleaseType type;
    private ReleaseFormat format;
    private List<Track> tracklist = new ArrayList<>();
    private List<Review> reviewList;
    private String releaseDate;
    private String catalogId;
    private Band band;
    private Label label;
    private List<Band> splitBands = new ArrayList<>();
    private List<Member> lineup;
    private List<Member> guestMembers;
    private List<Member> otherStaff;
    private boolean hasReviews;
    private int reviewAverage;
    private int reviewCount;

    public Release(final long id){
        super(id);
    }

    public Release(final long id, final String name){
        super(id, name);
    }

    public void setType(ReleaseType type){
        this.type = type;
    }

    public void setFormat(ReleaseFormat format){
        this.format = format;
    }

    public void setTracklist(List<Track> trackList){
        this.tracklist = trackList;
    }

    public void addReview(Review review){
        this.reviewList.add(review);
    }

    public void setReleaseDate(String releaseDate){
        this.releaseDate = releaseDate;
    }

    public void setCatalogId(String catalogId){
        this.catalogId = catalogId;
    }

    public void setBand(Band band){
        this.band = band;
    }

    public void setLabel(Label label){
        this.label = label;
    }

    public void addSplitBands(Band... bands){
        for(Band b : bands){
            splitBands.add(b);
        }
    }

    public void setHasReviews(boolean hasReviews){
        this.hasReviews = hasReviews;
    }

    public void setReviewAverage(int reviewAverage){
        this.reviewAverage = reviewAverage;
    }

    public void setReviewCount(int reviewCount){
        this.reviewCount = reviewCount;
    }

    public Band getBand(){
        return this.band;
    }

    public List<Track> getTracklist(){
        return this.tracklist;
    }

    public URL getURL() throws MalformedURLException{
        String baseURL = "" + BASE_URL + "";
        String typeSuffix = "/albums";
        String trimmedReleaseName = getName().replaceAll(" ", "_").replaceAll("/", "-");
        String trimmedBandName = getBand().getName().replaceAll(" ", "_");

        return new URL(baseURL + typeSuffix + "/" + trimmedBandName + "/" + trimmedReleaseName + "/" + getId());
    }

    public void printData(){
        System.out.println("Printing data for " + this);
        System.out.println("> Band : " + band);
        System.out.print("> Split bands : ");
        splitBands.forEach(band -> System.out.print(band+", "));
        System.out.println();
        System.out.println("> Type : " + type);
        System.out.println("> Format : " + format);
        System.out.println("> Release date : " + releaseDate);
        System.out.println("> Label : " + label);
        System.out.println("> Catalog ID : " + catalogId);
        System.out.println("** Tracklist");
        for(Track t : tracklist){
            System.out.println("> " + t.getTrackNumber() + ". " + t.getName() + " (" + t.getPlayTime() + ")");
            System.out.println("> Lyrics : ");
            System.out.println(t.getLyrics());
        }
    }
}
