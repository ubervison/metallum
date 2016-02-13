package ch.ubervison.metallum.entity;

import ch.ubervison.metallum.enums.LinkCategory;

/**
 * Represents a link.
 *
 * @author ubervison
 */
public class Link {
    private String url;
    private LinkCategory category;
    private String name;

    public Link(String url, String name){
        this.url = url;
        this.name = name;
    }

    public Link(String name){
        this.name = name;
    }

    public void setURL(String url){
        this.url = url;
    }

    public void setCategory(LinkCategory category){
        this.category = category;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getURL(){
        return this.url;
    }

    public LinkCategory getCategory(){
        return this.category;
    }

    public String getName(){
        return this.name;
    }
}
