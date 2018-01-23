package ch.ubervison.metallum.entity;

import ch.ubervison.metallum.enums.Country;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a label.
 *
 * @author ubervison
 */
public class Label extends AbstractEntity implements Accessible {

    private String specialization;
    private Country country;
    private Label parentLabel;
    private List<Label> subLabels = new ArrayList<Label>();
    private String address;
    private String phoneNumber;
    private int foundingDate;
    private boolean onlineShopping;
    private Link webSiteURL;
    private String email;
    private List<Band> currentRoster = new ArrayList<>();
    private Map<Band, Integer> pastRoster = new HashMap<>();
    private Map<Band, List<Release>> releases = new HashMap<>();
    private List<Link> links;
    private String details;

    public Label(final long id){
        super(id);
    }

    public Label(final long id, final String name){
        super(id, name);
    }

    public void setSpecialization(String specialization){
        this.specialization = specialization;
    }

    public void setCountry(Country country){
        this.country = country;
    }

    public void setParentLabel(Label parentLabel){
        this.parentLabel = parentLabel;
    }

    public void addSubLabel(Label subLabel){
        this.subLabels.add(subLabel);
    }

    public void setAddress(String adresds){
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public void setFoundingDate(int foundingDate){
        this.foundingDate = foundingDate;
    }

    public void setOnlineShopping(boolean onlineShopping){
        this.onlineShopping = onlineShopping;
    }

    public void setWebSiteURL(Link webSiteURL){
        this.webSiteURL = webSiteURL;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void addCurrentRosterBand(Band band){
        this.currentRoster.add(band);
    }

    public void addPastRosterBand(Band band, int releaseCount){
        pastRoster.put(band, releaseCount);
    }

    public void addRelease(Release release){
        List<Release> releaseList = releases.get(release.getBand());
        if(releaseList == null){
            List<Release> listToAdd = new ArrayList<Release>();
            listToAdd.add(release);
            releases.put(release.getBand(), listToAdd);
        }
        else {
            releaseList.add(release);
        }
    }

    public void addLinks(Link... links){
        for(Link link : links) {
            this.links.add(link);
        }
    }

    public void setDetails(String details){
        this.details = details;
    }

    public String getSpecialization() {
        return specialization;
    }

    public Country getCountry() {
        return country;
    }

    public Label getParentLabel() {
        return parentLabel;
    }

    public List<Label> getSubLabels() {
        return subLabels;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getFoundingDate() {
        return foundingDate;
    }

    public boolean isOnlineShopping() {
        return onlineShopping;
    }

    public Link getWebSiteURL() {
        return webSiteURL;
    }

    public String getEmail() {
        return email;
    }

    public List<Band> getCurrentRoster() {
        return currentRoster;
    }

    public Map<Band, Integer> getPastRoster() {
        return pastRoster;
    }

    public Map<Band, List<Release>> getReleases() {
        return releases;
    }

    public List<Link> getLinks() {
        return links;
    }

    public String getDetails() {
        return details;
    }

    public URL getURL() throws MalformedURLException {
        String baseURL = "" + BASE_URL + "";
        String typeSuffix = "/labels";
        String trimmedName = getName().replaceAll(" ", "_");

        return new URL(baseURL + typeSuffix + "/" + trimmedName + "/" + getId());
    }

    public void printData(){
        System.out.println("Printing data for " + this);
        System.out.println("> Specialization : " + specialization);
        System.out.println("> Country : " + country);
    }
}
