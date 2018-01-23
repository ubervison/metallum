package ch.ubervison.metallum.entity;

import ch.ubervison.metallum.enums.Country;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a band member, that is, an artist.
 *
 * @author ubervison
 */
public class Member extends AbstractEntity implements  Accessible {

    private String realName;
    private int age;
    private String gender;
    private Country country;
    private String province;
    private Map<Band, Map<Release, String>> activeBands = new HashMap<>();
    private Map<Band, Map<Release, String>> pastBands = new HashMap<>();
    private Map<Band, Map<Release, String>> guestSessionBands = new HashMap<>();
    private Map<Band, Map<Release, String>> miscBands = new HashMap<>();
    private List<Link> links;
    private String details;
    private String aka = "";

    public Member(final long id){
        super(id);
    }

    public Member(final long id, final String name){
        super(id, name);
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void addActiveRelease(Release release, String info){
        Map<Release, String> activeBandReleases = this.activeBands.get(release.getBand());
        if(activeBandReleases == null){
            activeBandReleases = new HashMap<Release, String>();
        }
        activeBandReleases.put(release, info);
        this.activeBands.put(release.getBand(), activeBandReleases);
    }

    public void addPastRelease(Release release, String info){
        Map<Release, String> pastBandReleases = this.pastBands.get(release.getBand());
        if(pastBandReleases == null){
            pastBandReleases = new HashMap<Release, String>();
        }
        pastBandReleases.put(release, info);
        this.activeBands.put(release.getBand(), pastBandReleases);
    }

    public void addGuestSessionRelease(Release release, String info){
        Map<Release, String> guestSessionBandReleases = this.pastBands.get(release.getBand());
        if(guestSessionBandReleases == null){
            guestSessionBandReleases = new HashMap<Release, String>();
        }
        guestSessionBandReleases.put(release, info);
        this.activeBands.put(release.getBand(), guestSessionBandReleases);
    }

    public void addMiscRelease(Release release, String info){
        Map<Release, String> miscBandReleases = this.miscBands.get(release.getBand());
        if(miscBandReleases == null){
            miscBandReleases = new HashMap<Release, String>();
        }
        miscBandReleases.put(release, info);
        this.activeBands.put(release.getBand(), miscBandReleases);
    }

    public void addLinks(Link... links){
        for (Link link : links){
            this.links.add(link);
        }
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setAka(String aka){
        this.aka = aka;
    }

    public String getAka(){
        return this.aka;
    }

    public URL getURL() throws MalformedURLException {
        String baseURL = "" + BASE_URL + "";
        String typeSuffix = "/artists";
        String trimmedName = getName().replaceAll(" ", "_");

        return new URL(baseURL + typeSuffix + "/" + trimmedName + "/" + getId());
    }

    public void printData(){
        System.out.println("Printing data for " + this);
        System.out.println("> Real name : " + realName + aka);
        System.out.println("> Age : " + age);
        System.out.println("> Gender : " + gender);
        System.out.println("> Coutry : " + country);
        System.out.println("> Province : " + province);
        System.out.println("> Active bands : ");
        for(Map.Entry<Band, Map<Release, String>> bandEntry : activeBands.entrySet()){
            System.out.println("> - " + bandEntry.getKey());
            for(Map.Entry<Release, String> releaseEntry : bandEntry.getValue().entrySet()){
                System.out.println(">   |-- " + releaseEntry.getKey() + " (" + releaseEntry.getValue() + ")");
            }
        }
    }
}
