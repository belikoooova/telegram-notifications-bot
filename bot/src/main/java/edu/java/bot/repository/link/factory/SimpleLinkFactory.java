package edu.java.bot.repository.link.factory;

import edu.java.bot.repository.link.Link;
import edu.java.bot.repository.link.LinkResource;
import edu.java.bot.repository.link.repository.LinkRepositoryKey;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Matcher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class SimpleLinkFactory implements LinkFactory {
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

            return isOkConnection(url);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isOkConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            return HttpStatus.valueOf(responseCode).is2xxSuccessful();
        } catch (Exception e) {
            return false;
        } finally {
            connection.disconnect();
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
