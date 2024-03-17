package edu.java.scrapper.service.update;

import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.entity.dto.LinkUpdateRequest;
import edu.java.scrapper.exception.UnsupportedResourceException;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.client.BotClient;
import edu.java.scrapper.service.client.WebsiteClient;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@SuppressWarnings("RegexpSinglelineJava")
@EnableScheduling
@RequiredArgsConstructor
public class LinkUpdater {
    private final ApplicationConfig applicationConfig;
    private final LinkService linkService;
    private final List<WebsiteClient> clients;
    private final BotClient botClient;

    @Scheduled(fixedDelayString = "${app.scheduler.interval}")
    public void update() {
        linkService.listAllOldChecked(applicationConfig.linkUpdaterScheduler().interval())
            .forEach(l -> {
                boolean isHandled = false;
                for (WebsiteClient client : clients) {
                    if (client.canHandle(l.getUrl().toString())) {
                        isHandled = true;
                        Optional<LinkUpdateRequest> optionalRequest = client.handle(l);
                        if (optionalRequest.isEmpty()) {
                            continue;
                        }
                        botClient.sendUpdate(optionalRequest.get());
                    }
                }
                if (!isHandled) {
                    throw new UnsupportedResourceException();
                }
            });
    }
}
