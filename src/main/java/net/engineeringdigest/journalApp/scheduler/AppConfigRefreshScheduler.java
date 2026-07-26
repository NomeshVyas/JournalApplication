package net.engineeringdigest.journalApp.scheduler;

import net.engineeringdigest.journalApp.cache.AppCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AppConfigRefreshScheduler {
    private final AppCache appCache;

    public AppConfigRefreshScheduler(AppCache appCache) {
        this.appCache = appCache;
    }

    @Scheduled(cron = "0 0/10 * ? * *")
    public void refreshAppCache() {
        appCache.init();
    }
}
