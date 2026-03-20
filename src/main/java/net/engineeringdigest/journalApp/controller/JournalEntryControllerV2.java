package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping
    public List<JournalEntry> getAll() {
        return journalEntryService.getAll();
    }

    @PostMapping
    public JournalEntry createEntry(@RequestBody JournalEntry journalEntry) {
        journalEntry.setDate(LocalDateTime.now());
        journalEntryService.saveEntry(journalEntry);
        return journalEntry;
    }

    @GetMapping("id/{id}")
    public JournalEntry getJournalEntryById(@PathVariable ObjectId id) {
        return journalEntryService.getJournalEntryById(id);
    }

    @DeleteMapping("id/{id}")
    public boolean deleteJournalEntryById(@PathVariable ObjectId id) {
        journalEntryService.deleteJournalEntryById(id);
        return true;
    }

    @PutMapping("id/{id}")
    public JournalEntry updateJournalEntryById(@PathVariable ObjectId id, @RequestBody JournalEntry newJournalEntry) {
        JournalEntry savedJournalEntry = journalEntryService.getJournalEntryById(id);
        if(savedJournalEntry != null) {
            savedJournalEntry.setTitle(newJournalEntry.getTitle() != null && !newJournalEntry.getTitle().isEmpty() ? newJournalEntry.getTitle() : savedJournalEntry.getTitle());
            savedJournalEntry.setContent(newJournalEntry.getContent() != null && !newJournalEntry.getContent().isEmpty() ? newJournalEntry.getContent() : savedJournalEntry.getContent());
        }
        journalEntryService.saveEntry(savedJournalEntry);
        return savedJournalEntry;
    }
}
