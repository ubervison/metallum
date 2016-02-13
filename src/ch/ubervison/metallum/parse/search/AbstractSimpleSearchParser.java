package ch.ubervison.metallum.parse.search;

import ch.ubervison.metallum.entity.AbstractEntity;

import javax.json.JsonObject;
import java.util.List;

/**
 * @author ubervison
 */
public abstract class AbstractSimpleSearchParser<E extends AbstractEntity> {

    protected final String JSON_DATA_KEY = "aaData";
    protected JsonObject searchResults;

    public AbstractSimpleSearchParser(JsonObject searchResults){
        this.searchResults = searchResults;
    }

    /**
     * Returns the resuts in a list of entites.
     *
     * @return
     */
    public abstract List<E> getEntityList();
}
