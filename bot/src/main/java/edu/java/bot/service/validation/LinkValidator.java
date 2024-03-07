package edu.java.bot.service.validation;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.regex.Matcher;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class LinkValidator {
    public String getValidatedAndNormalizedUrl(String url) {
        TrackingResource resource = validate(url);
        if (resource == null) {
            throw new IllegalArgumentException("Incorrect Url");
        }
        return normalize(url, resource);
    }

    private TrackingResource validate(String url) {
        if (!isAvailable(url)) {
            return null;
        }
        return getResource(url);
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
            connection.setRequestMethod(HttpMethod.GET.name());
            return HttpStatus.valueOf(connection.getResponseCode()).is2xxSuccessful();
        } catch (Exception e) {
            return false;
        } finally {
            connection.disconnect();
        }
    }

    private TrackingResource getResource(String url) {
        for (TrackingResource resource : TrackingResource.values()) {
            Matcher matcher = resource.getRegex().matcher(url);
            if (matcher.find()) {
                return resource;
            }
        }
        return null;
    }

    private String normalize(String url, TrackingResource resource) {
        Matcher matcher = resource.getRegex().matcher(url);
        if (matcher.find()) {
            return resource.baseUrl() + matcher.group(resource.pathOnResourceGroupNumber());
        }
        throw new IllegalArgumentException("Incorrect url");
    }
}
