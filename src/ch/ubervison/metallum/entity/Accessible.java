package ch.ubervison.metallum.entity;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * An interface that describes a type that is accessible, that is, it has an URL associated to it that we can access.
 *
 * @author ubervison
 */
public interface Accessible {

    /**
     * Return the URL of this entity on the Metal Archives.
     *
     * @return the URL of this entity
     * @throws MalformedURLException
     */
    URL getURL() throws MalformedURLException;
}
