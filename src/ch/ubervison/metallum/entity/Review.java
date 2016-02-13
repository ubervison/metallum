package ch.ubervison.metallum.entity;

/**
 * Represents a review.
 *
 * @author ubervison
 */
public class Review extends AbstractEntity {

    private String author;
    private int score;
    private String content;
    private String date;
    private Release release;

    public Review(long id){
        super(id);
    }

    public Review(long id, String name){
        super(id, name);
    }

    public void setAuthor(String author){
        this.author = author;
    }

    public void setScore(int score){
        this.score = score;
    }

    public void setDate(String date){
        this.date = date;
    }

    public void setRelease(Release release){
        this.release = release;
    }

    public String getAuthor(){
        return author;
    }

    public String getReleaseName(){
        return release.getName();
    }

    public int getScore(){
        return score;
    }
}
