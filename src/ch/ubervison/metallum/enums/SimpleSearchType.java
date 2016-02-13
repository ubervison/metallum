package ch.ubervison.metallum.enums;

/**
 * An enumeration of the possibles search types.
 *
 * @author ubervison
 */
public enum SimpleSearchType {
    BAND_NAME("Band name"), MUSIC_GENRE("Music genre"), LYRICAL_THEMES("Lyrical themes"), ALBUM_TITLE("Album title"), SONG_TITLE("Song title"), LABEL("Label"), ARTIST("Artist"), USER_PROFILE("User profile");

    private String asString;

    private SimpleSearchType(String asString){
        this.asString = asString;
    }
}
