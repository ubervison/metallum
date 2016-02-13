package ch.ubervison.metallum.enums;

/**
 * An enumeration of possible band statuses.
 *
 * @author ubervison
 */
public enum BandStatus {
    BVRNED_AND_DIED("Bvrned and died"), // Default status. should not happen
    ACTIVE("Active"), ON_HOLD("On hold"), SPLIT_UP("Split-up"), UNKNOWN("Unknown"), CHANGED_NAME("Changed name");

    private String asString;

    private BandStatus(String asString){
        this.asString = asString;
    }

    public String asString(){
        return asString;
    }

    public static BandStatus parseBandStatus(String status){
        for(BandStatus bs : BandStatus.values()){
            if(bs.asString().equals(status)){
                return bs;
            }
        }
        return BVRNED_AND_DIED;
    }

    @Override
    public String toString(){
        return asString;
    }
}
