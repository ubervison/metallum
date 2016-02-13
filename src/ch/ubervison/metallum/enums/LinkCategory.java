package ch.ubervison.metallum.enums;

/**
 * An enumeration of possible link categories.
 *
 * @author ubervison
 */
public enum LinkCategory {
    OFFICIAL("Official"), OFFICIAL_MERCH("Official merch"), UNOFFICIAL("Unofficial"), TABLATURES("Tablatures"), LABEL("Label");

    private String asString;

    private LinkCategory(String asString){
        this.asString = asString;
    }
}
