package edu.java.bot.service.link.factory;

import edu.java.bot.service.link.Link;
import edu.java.bot.service.link.LinkResource;
import edu.java.bot.service.link.repository.LinkRepositoryKey;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Matcher;
import org.springframework.stereotype.Service;

@Service
public class SimpleLinkFactory implements LinkFactory {
    private static final int MODULO = 100;
    private static final int OK_CODE = 2;

    @Override
    public Link create(Long userId, String urlString) throws URISyntaxException {
        LinkRepositoryKey key = createKey(userId, urlString);
        Link link = new Link(key);
        link.setUrl(new URI(urlString));
        link.setResource(key.resource());
        link.setIdOnResource(key.idOnResource());
        return link;
    }

    @Override
    public LinkRepositoryKey createKey(Long userId, String urlString) {
        if (!validate(urlString)) {
            throw new IllegalArgumentException("Incorrect Url");
        }
        LinkResource resource = getResource(urlString);
        return new LinkRepositoryKey(userId, resource, getIdOnResource(resource, urlString));
    }

    private boolean validate(String url) {
        if (!isAvailable(url)) {
            return false;
        }
        return getResource(url) != null;
    }

    private boolean isAvailable(String urlString) {
        try {
            URI uri = new URI(urlString);
            URL url = uri.toURL();

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            connection.disconnect();
            return responseCode / MODULO == OK_CODE;
        } catch (Exception e) {
            return false;
        }
    }

    private LinkResource getResource(String url) {
        for (LinkResource linkResource : LinkResource.values()) {
            Matcher matcher = linkResource.regex().matcher(url);
            if (matcher.find()) {
                return linkResource;
            }
        }
        return null;
    }

    private String getIdOnResource(LinkResource resource, String url) {
        Matcher matcher = resource.regex().matcher(url);
        if (matcher.find()) {
            return matcher.group(resource.idOnResourceGroupNumber());
        }
        return null;
    }
}
