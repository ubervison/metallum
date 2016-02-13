package ch.ubervison.metallum.enums;

/**
 * An enumeration of possible release types.
 *
 * @author ubervison
 */
public enum ReleaseType {
    PARCHMENT("Deciphering Scorched Parchment Neath Torchlight", false), FULL_LENGTH("Full-length", false), LIVE_ALBUM("Live album", false), DEMO("Demo", false), SINGLE("Single", false), EP("EP", false), VIDEO("Video", false), BOXED_SET("Boxed set", false), SPLIT("Split", true), VIDEO_VHS("Video/VHS (legacy)", false), COMPILATION("Compilation", false), SPLIT_VIDEO("Split video", true), COLLABORATION("Collaboration", true);

    private final String asString;
    private final boolean isSplit;

    private ReleaseType(final String asString, final boolean isSplit){
        this.asString = asString;
        this.isSplit = isSplit;
    }

    public String asString(){
        return this.asString;
    }

    public static ReleaseType parseReleaseType(String asString){
        for(ReleaseType t : ReleaseType.values()){
            if(t.asString().equals(asString)){
                return t;
            }
        }
        return PARCHMENT;
    }

    public static boolean isSplit(final ReleaseType type){
        if (type == null){
            return false;
        }
        return type.isSplit;
    }

    @Override
    public String toString(){
        return this.asString;
    }
}
