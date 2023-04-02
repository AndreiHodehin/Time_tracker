package com.example.trecking_time;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@PWA(
        name = "Tracking time",
        shortName = "Tracking",
        offlinePath = "offline.html",
        offlineResources = {"./images/offline.png"}
)
public class TrackingTimeApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(TrackingTimeApplication.class, args);
    }

}
