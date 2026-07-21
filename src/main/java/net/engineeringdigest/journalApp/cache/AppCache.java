package net.engineeringdigest.journalApp.cache;

import net.engineeringdigest.journalApp.entity.ConfigJournalApp;
import net.engineeringdigest.journalApp.repository.ConfigJournalAppRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {

    public enum keys {
        WEATHER_API("weather_api");

        private final String value;

        keys(String value){
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    private final ConfigJournalAppRepository configJournalAppRepository;
    private Map<String, String> appCache;

    public AppCache(ConfigJournalAppRepository configJournalAppRepository) {
        this.configJournalAppRepository = configJournalAppRepository;
    }

    @PostConstruct
    public void init() {
        appCache = new HashMap<>();
        List<ConfigJournalApp> allConfigEntries = configJournalAppRepository.findAll();
        for (ConfigJournalApp configJournalAppEntry: allConfigEntries)
            appCache.put(configJournalAppEntry.getKey(), configJournalAppEntry.getValue());
    }

    public String getWeatherApi() {
        return appCache.get(keys.WEATHER_API.toString());
    }
}
