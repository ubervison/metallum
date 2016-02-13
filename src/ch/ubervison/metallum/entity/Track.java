package ch.ubervison.metallum.entity;

/**
 * Represents a Track
 *
 * @author ubervison
 */
public class Track extends AbstractEntity {

    private Release fromRelease;
    private String splitBandName = "";
    private int trackNumber;
    private String playTime;
    private String lyrics;

    public Track(long id){
        super(id);
    }

    public Track(long id, String name){
        super(id, name);
    }

    public void setFromRelease(Release fromRelease){
        this.fromRelease = fromRelease;
    }

    public void setSplitBandName(String splitBandName){
        this.splitBandName = splitBandName;
    }

    public void setTrackNumber(int trackNumber){
        this.trackNumber = trackNumber;
    }

    public void setPlayTime(String playTime){
        this.playTime = playTime;
    }

    public void setLyrics(String lyrics){
        this.lyrics = lyrics;
    }

    public String getSplitBandName(){
        return this.splitBandName;
    }

    public int getTrackNumber(){
        return this.trackNumber;
    }

    public String getPlayTime(){
        return this.playTime;
    }

    public String getLyrics(){
        return this.lyrics;
    }

    public void printData(){
        System.out.println("Printing data for " + this);
        System.out.println("> From release : " + fromRelease.toString());
        System.out.println("> Split band name : " + splitBandName);
        System.out.println("> Track number : " + trackNumber);
        System.out.println("> Playtime : " + playTime);
    }

}
