package ch.ubervison.metallum.parse.search;

import ch.ubervison.metallum.entity.AbstractEntity;
import ch.ubervison.metallum.search.SearchUtils;

import javax.json.JsonObject;
import java.util.List;

/**
 * @author ubervison
 */
public abstract class AbstractSimpleSearchParser<E extends AbstractEntity> {

    protected final String JSON_DATA_KEY = "aaData";
    protected JsonObject searchResults;
    public static String BASE_URL = SearchUtils.BASE_URL;

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
