package ch.ubervison.metallum.enums;

import ch.ubervison.metallum.entity.Release;

/**
 * An enumeration of possible release formats.
 *
 * @author ubervison
 */
public enum ReleaseFormat {
    PARCHMENT("Deciphering Scorched Parchment Neath Torchlight"), CD("CD"), CASSETTE("Cassette"), VINYL("Vinyl"), VHS("VHS"), DVD("DVD"), DIGITAL("Digital"), BLU_RAY("Blu-Ray"), OTHER("Other");

    private String asString;

    private ReleaseFormat(String asString){
        this.asString = asString;
    }

    public static ReleaseFormat parseReleaseFormat(String asString){
        for(ReleaseFormat f : ReleaseFormat.values()){
            if(f.asString.equals(asString)){
                return f;
            }
        }
        return PARCHMENT;
    }
}
