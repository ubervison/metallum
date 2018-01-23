package ch.ubervison.metallum.entity;

import ch.ubervison.metallum.search.SearchUtils;

/**
 * An abstract representation of an entity. An entity is an entry in the archives such as a band, an artist or a record label.
 *
 * @author ubervison
 */
public abstract class AbstractEntity {

    private String name;
    private long id;
    private String addedBy;
    private String modifiedBy;
    private String addedOn;
    private String lastModifiedOn;

    public static String BASE_URL = SearchUtils.BASE_URL;

    /**
     * Create a new Entity with the given id and name.
     *
     * @param id the entity's unique id on the metal archives
     * @param name the entity's name on the metal archives
     */
    public AbstractEntity(final long id, final String name){
        this.id = id;
        this.name = name;
    }

    /** Create a new Entity with the given id.
     *
     * @param id the entity's unique id on the metal archives
     */
    public AbstractEntity(final long id){
        this.id = id;
    }

    /**
     * Get the name of this entity.
     *
     * @return the name of this entity
     */
    public String getName(){
        return this.name;
    }

    /**
     * Get this entity's unique id.
     *
     * @return the id of this entity
     */
    public long getId(){
        return this.id;
    }

    public String getAddedBy(){
        return this.addedBy;
    }

    public String getModifiedBy(){
        return this.modifiedBy;
    }

    public String getAddedOn(){
        return this.addedOn;
    }

    public String getLastModifiedOn(){
        return this.lastModifiedOn;
    }

    public void setName(final String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return ((this.name != null ? this.name : "") + " [id = " + this.id + "]");
    }
}
