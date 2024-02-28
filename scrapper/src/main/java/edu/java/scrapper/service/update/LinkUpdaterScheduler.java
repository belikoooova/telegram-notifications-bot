package edu.java.scrapper.service.update;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@SuppressWarnings("RegexpSinglelineJava")
@EnableScheduling
public class LinkUpdaterScheduler {
    @Scheduled(fixedDelayString = "${app.scheduler.interval}")
    public void update() {
        log.info("There is nothing we can do. But some day there will be links updating.");
    }
}
