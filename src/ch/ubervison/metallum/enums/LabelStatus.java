package ch.ubervison.metallum.enums;

/**
 * An enumeration of possible label statuses.
 *
 *@author ubervison
 */
public enum LabelStatus {
    ACTIVE("Active"), UNKNOWN("Unknown"), CLOSED("Closed"), CHANGED_NAME("Changed name");

    private String asString;

    private LabelStatus(String asString){
        this.asString = asString;
    }
}
