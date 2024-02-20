package edu.java.scrapper.service.update;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SuppressWarnings("RegexpSinglelineJava")
@EnableScheduling
public class LinkUpdaterScheduler {
    @Scheduled(fixedDelayString = "#{@schedulerInterval.toMillis()}")
    public void update() {
        System.out.println("There is nothing we can do. But some day there will be links updating.");
    }
}
