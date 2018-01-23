package ch.ubervison.metallum.parse.site;

import ch.ubervison.metallum.entity.AbstractEntity;
import ch.ubervison.metallum.entity.Accessible;
import ch.ubervison.metallum.search.SearchUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;

/**
 * An abstract representation of a site parser. A site parser parses the page of a given entity and returns the same entity with added details found on the page.
 */
public abstract class AbstractSiteParser<T extends AbstractEntity & Accessible> {

    protected final String JSON_DATA_KEY = "aaData";
    protected Document doc;
    protected T entity;

    public String BASE_URL = SearchUtils.BASE_URL;

    /**
     * Create a new site parser for the given entity.
     *
     * @param entity the entity whose page we wish to parse
     * @throws IOException
     */
    public AbstractSiteParser(T entity) throws IOException{
        this.entity = entity;
        System.out.println("Connecting to entity at address: " + entity.getURL());
        this.doc = Jsoup.connect(entity.getURL().toString()).get();
    }

    /**
     * Parse the given entity's page and complete it with the additional details.
     *
     * @return the same entity with new details found on the page, to enable chained calls.
     * @throws IOException
     */
    abstract public T parse() throws IOException ;
}
