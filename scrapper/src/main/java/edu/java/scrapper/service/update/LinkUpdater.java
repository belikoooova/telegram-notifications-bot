package edu.java.scrapper.service.update;

import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.entity.dto.LinkUpdateRequest;
import edu.java.scrapper.exception.UnsupportedResourceException;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.client.BotClient;
import edu.java.scrapper.service.client.WebsiteClient;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@SuppressWarnings("RegexpSinglelineJava")
@Component
@RequiredArgsConstructor
public class LinkUpdater {
    private final ApplicationConfig applicationConfig;
    private final LinkService linkService;
    private final List<WebsiteClient> clients;
    private final BotClient botClient;

    @Scheduled(fixedDelayString = "PT${app.link-updater-scheduler.interval}")
    public void update() {
        linkService.listAllOldChecked(applicationConfig.linkUpdaterScheduler().interval())
            .forEach(link -> {
                boolean isHandled = false;
                for (WebsiteClient client : clients) {
                    if (client.canHandle(link.getUrl().toString())) {
                        isHandled = true;
                        for (LinkUpdateRequest request : client.handle(link)) {
                            botClient.sendUpdate(request);
                        }
                        break;
                    }
                }
                linkService.updateLastCheckedTime(link, OffsetDateTime.now());
                if (!isHandled) {
                    throw new UnsupportedResourceException();
                }
            });
    }
}
