package ch.ubervison.metallum.entity;

import ch.ubervison.metallum.enums.BandStatus;
import ch.ubervison.metallum.enums.Country;

import java.lang.management.MemoryNotificationInfo;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Represents a band.
 *
 * @author ubervison
 */
public class Band extends AbstractEntity implements Accessible {

    private String genre;
    private Country country;
    private String location;
    private BandStatus status;
    private String lyricalThemes;
    private Label label;
    private int yearFormed;
    private String yearsActive;
    private String info;
    private List<Release> releases = new ArrayList<Release>();
    private Map<Member, String> currentLineup = new HashMap<Member, String>();
    private Map<Member, String> pastLineup = new HashMap<Member, String>();
    private Map<Member, String> liveLineup = new HashMap<Member, String>();
    private List<Link> officialLinks = new ArrayList<Link>();
    private List<Link> officialMerchLinks = new ArrayList<Link>();
    private List<Link> unofficialLinks = new ArrayList<Link>();
    private List<Link> tablatureLinks = new ArrayList<Link>();
    private List<Link> labelLinks = new ArrayList<Link>();
    private List<Review> reviews = new ArrayList<>();
    private Map<Band, Integer> recommendations = new LinkedHashMap<>(); /* linked hash map to preserve ordering of recommendations */
    private String aka;

    /**
     * Create a new Band with the given id.
     *
     * @param id the band's unique id on the metal archives
     */
    public Band(long id){
        super(id);
    }

    /**
     * Create a new band with the given id and name.
     *
     * @param id the band's unique id on the metal archives
     * @param name the band's name on the metal archives
     */
    public Band(long id, String name){
        super(id, name);
    }

    /** Set the genre of this band.
     *
     * @param genre the new genre of this band
     */
    public void setGenre(String genre){
        this.genre = genre;
    }

    /**
     * Set the country of origin of this band.
     *
     * @param country the new country of origin
     */
    public void setCountry(Country country){
        this.country = country;
    }

    /**
     * Set the more precise location of this band. Usually a state/province and a city.
     *
     * @param location the new location of the band
     */
    public void setLocation(String location){
        this.location = location;
    }

    /**
     * Set the current status of thjs band.
     *
     * @param status the new current status of the band.
     */
    public void setStatus(BandStatus status){
        this.status = status;
    }

    /**
     * Set the lyrical themes of this band.
     *
     * @param lyricalThemes the new lyrical themes of the band
     */
    public void setLyricalThemes(String lyricalThemes){
        this.lyricalThemes = lyricalThemes;
    }

    /**
     * Set the current label of this band.
     *
     * @param label the new current label of the band
     */
    public void setLabel(Label label){
        this.label = label;
    }

    /**
     * Set the year in which the band was formed.
     *
     * @param yearFormed the year in which the band was formed
     */
    public void setYearFormed(int yearFormed){
        this.yearFormed = yearFormed;
    }

    /**
     * Set the years in which this band was active
     *
     * @param yearsActive the years in which the band was active
     */
    public void setYearsActive(String yearsActive){
        this.yearsActive = yearsActive;
    }

    /**
     * Set the additional informative text on the band's page.
     *
     * @param info the new additional text.
     */
    public void setInfo(String info){
        this.info = info;
    }

    /**
     * Add a new release to this band's discography.
     *
     * @param release the release to add
     */
    public void addRelease(Release release){
        this.releases.add(release);
    }

    /**
     * Set the current lineup of this band.
     *
     * @param currentLineup the new current lineup
     */
    public void setCurrentLineup(Map<Member, String> currentLineup){
        this.currentLineup = currentLineup;
    }

    /**
     * Set the past lineup, that is, members that are no longer part of this band.
     *
     * @param pastLineup the new past lineup
     */
    public void setPastLineup(Map<Member, String> pastLineup){
        this.pastLineup = pastLineup;
    }

    /**
     * Set the live lineup, that is, members that are only live musicians for this band.
     *
     * @param liveLineup the new live lineup
     */
    public void setLiveLineup(Map<Member, String> liveLineup){
        this.liveLineup = liveLineup;
    }


    /**
     * Add 1 or more links from the "related links" section on the band's page.
     *
     * @param links the links to add
     */
    public void addLinks(Link... links){
        for (Link link : links){
            switch(link.getCategory()){
                case OFFICIAL:
                    this.officialLinks.add(link);
                    break;
                case OFFICIAL_MERCH:
                    this.officialMerchLinks.add(link);
                    break;
                case UNOFFICIAL:
                    this.unofficialLinks.add(link);
                    break;
                case TABLATURES:
                    this.tablatureLinks.add(link);
                    break;
                case LABEL:
                    this.labelLinks.add(link);
                    break;
                default:
                    throw new UnsupportedOperationException("Maybe you forgot to implement a switch case for one link category");
            }
        }
    }

    /**
     * Set the list of reviews for this band.
     *
     * @param reviews A list of reviews
     */
    public void setReviews(List<Review> reviews){
        this.reviews = reviews;
    }

    /**
     * Add a recommended band.
     *
     * @param band the band to add to the map of recommended bands
     * @param score the "similarity score" of this band
     */
    public void addRecommendation(Band band, int score){
        recommendations.put(band, score);
    }

    /**
     * Set an alternative spelling for this band.
     *
     * @param aka the new alternative spelling
     */
    public void setAlternateSpelling(String aka){
        this.aka = aka;
    }

    /**
     * Get the URL of this band's page on the metal archives.
     *
     * @return the URL of the band's page
     * @throws MalformedURLException
     */
    public URL getURL() throws MalformedURLException{
        String baseURL = "" + BASE_URL + "";
        String typeSuffix = "/bands";
        String trimmedName = getName().replaceAll(" ", "_");

        return new URL(baseURL + typeSuffix + "/" + trimmedName + "/" + getId());
    }

    /**
     * Print a pretty-formatted view of this band's data.
     */
    public void printData(){
        System.out.println("Printing data for " + this);
        if(aka != null){
            System.out.println("> Alternate spelling : " + aka);
        }
        System.out.println("> Genre : " + genre);
        System.out.println("> Country : " + country);
        System.out.println("> Province : " + location);
        System.out.println("> Status : " + status);
        System.out.println("> Lyrical themes : " + lyricalThemes);
        System.out.println("> Label : " + label);
        System.out.println("> Formed in : " + yearFormed);
        System.out.println("> Years active : " + yearsActive);
        System.out.println("** Additional info : \n" + info);
        System.out.println();
        System.out.println("** Releases : ");
        for (Release release : releases){
            System.out.println("> " + release);
        }
        System.out.println();
        System.out.println("** Members :");
        System.out.println("> Current members : ");
        for (Map.Entry<Member, String> entry : currentLineup.entrySet()){
            System.out.println("> - " + entry.getKey() + entry.getValue());
        }
        System.out.println("> Past members : ");
        for (Map.Entry<Member, String> entry : pastLineup.entrySet()){
            System.out.println("> - " + entry.getKey() + entry.getValue());
        }
        System.out.println("> Live members : ");
        for (Map.Entry<Member, String> entry : liveLineup.entrySet()){
            System.out.println("> - " + entry.getKey() + entry.getValue());
        }
        System.out.println();
        System.out.println("** Links :");
        System.out.println("> Official links : ");
        for(Link link : officialLinks){
            System.out.println("> - " + link.getName() + ": "+link.getURL() + " (" + link.getCategory() + ")");
        }
        System.out.println("> Official merch links : ");
        for(Link link : officialMerchLinks){
            System.out.println("> - " + link.getName() + ": "+link.getURL() + " (" + link.getCategory() + ")");
        }
        System.out.println("> Unofficial links : ");
        for(Link link : unofficialLinks){
            System.out.println("> - " + link.getName() + ": "+link.getURL() + " (" + link.getCategory() + ")");
        }
        System.out.println("> Tablature links : ");
        for(Link link : tablatureLinks){
            System.out.println("> - " + link.getName() + ": "+link.getURL() + " (" + link.getCategory() + ")");
        }
        System.out.println("> Label links : ");
        for(Link link : labelLinks){
            System.out.println("> - " + link.getName() + ": "+link.getURL() + " (" + link.getCategory() + ")");
        }
        System.out.println();
        System.out.println("** Reviews");
        for(Review r : reviews){
            System.out.println("> " + r.getReleaseName() + " - " + r.getScore() + "%" + " (written by " + r.getAuthor() + ")");
        }
        System.out.println();
        System.out.println("** Similar bands");
        for(Map.Entry<Band, Integer> entry : recommendations.entrySet()){
            System.out.println("> " + entry.getKey() + " (score = " + entry.getValue() + ")");
        }
    }

}
