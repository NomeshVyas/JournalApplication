package net.engineeringdigest.journalApp.scheduler;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.enums.Sentiment;
import net.engineeringdigest.journalApp.repository.UserRepositoryImpl;
import net.engineeringdigest.journalApp.service.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {
    private final UserRepositoryImpl userRepository;
    private final EmailService emailService;

    public UserScheduler(UserRepositoryImpl userRepository, EmailService emailService) {
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    @Scheduled(cron = "0 0 9 * * SUN")
    public void sendSentimentAnalysisMail() {
        List<User> usersForSentimentAnalysis = userRepository.getUserForSentimentAnalysis();
        for (User user: usersForSentimentAnalysis) {
            List<Sentiment> filteredSentiments = user.getJournalEntries()
                    .stream()
                    .filter(
                            x -> x.getDate()
                            .isAfter(LocalDateTime.now().minusDays(7))
                    )
                    .map(JournalEntry::getSentiment)
                    .collect(Collectors.toList());
            Map<Sentiment, Integer> sentimentOccurrence = getSentimentOccurrence(filteredSentiments);
            Sentiment maxOccuredSentiment = getMostFrequentSentiment(sentimentOccurrence);
            if (maxOccuredSentiment != null)
                emailService.sendEmail(user.getEmail(), "Sentiment for last 7 days", maxOccuredSentiment.toString());
        }
    }

    private Map<Sentiment, Integer> getSentimentOccurrence(List<Sentiment> sentiments) {
        Map<Sentiment, Integer> sentimentOccurrence = new HashMap<>();
        for (Sentiment sentiment: sentiments)
            if (sentiment != null)
                sentimentOccurrence.put(sentiment, sentimentOccurrence.getOrDefault(sentiment, 0));

        return sentimentOccurrence;
    }

    private Sentiment getMostFrequentSentiment(Map<Sentiment, Integer> sentimentOccurrence) {
        Sentiment maxOccuredSentiment = null;
        int maxCount = 0;
        for (Map.Entry<Sentiment, Integer> entry: sentimentOccurrence.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                maxOccuredSentiment = entry.getKey();
            }
        }
        return maxOccuredSentiment;
    }
}
