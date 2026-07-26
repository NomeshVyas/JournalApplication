package net.engineeringdigest.journalApp.scheduler;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepositoryImpl;
import net.engineeringdigest.journalApp.service.EmailService;
import net.engineeringdigest.journalApp.service.SentimentAnalysisService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserScheduler {
    private final UserRepositoryImpl userRepository;
    private final EmailService emailService;
    private final SentimentAnalysisService sentimentAnalysisService;

    public UserScheduler(UserRepositoryImpl userRepository, EmailService emailService, SentimentAnalysisService sentimentAnalysisService) {
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.sentimentAnalysisService = sentimentAnalysisService;

    }

    @Scheduled(cron = "0 0 9 * * SUN")
    public void sendSentimentAnalysisMail() {
        List<User> usersForSentimentAlalysis = userRepository.getUserForSentimentAnalysis();
        for (User user: usersForSentimentAlalysis) {
            List<String> filteredJournalEntryContents = user.getJournalEntries()
                    .stream()
                    .filter(
                            x -> x.getDate()
                            .isAfter(LocalDateTime.now().minusDays(7))
                    )
                    .map(JournalEntry::getContent)
                    .collect(Collectors.toList());
            String sentiment = sentimentAnalysisService.getSentiment(String.join(" ", filteredJournalEntryContents));
            emailService.sendEmail(user.getEmail(), "Sentiment for last 7 days", sentiment);
        }
    }
}
