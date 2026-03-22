package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<JournalEntry> all = journalEntryService.getAll();
        if (all != null && !all.isEmpty())
            return new ResponseEntity<>(all, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry journalEntry) {
        try {
            journalEntry.setDate(LocalDateTime.now());
            journalEntryService.saveEntry(journalEntry);
            return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
//        journalEntry.setDate(LocalDateTime.now());
//        journalEntryService.saveEntry(journalEntry);
//        return journalEntry;
    }

    @GetMapping("id/{id}")
    public ResponseEntity<?> getJournalEntryById(@PathVariable ObjectId id) {
        Optional<JournalEntry> journalEntry = journalEntryService.getJournalEntryById(id);
//        return journalEntry.isPresent() ? new ResponseEntity<>(journalEntry.get(), HttpStatus.NOT_FOUND) : new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
//        if (journalEntry.isPresent())
//            return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return journalEntry
                .map(entity -> new ResponseEntity<>(entity, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("id/{id}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId id) {
        journalEntryService.deleteJournalEntryById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("id/{id}")
    public ResponseEntity<?> updateJournalEntryById(@PathVariable ObjectId id, @RequestBody JournalEntry newJournalEntry) {
        Optional<JournalEntry> savedJournalEntry = journalEntryService.getJournalEntryById(id);
        if(savedJournalEntry.isPresent()) {
            savedJournalEntry.get().setTitle(newJournalEntry.getTitle() != null && !newJournalEntry.getTitle().isEmpty() ? newJournalEntry.getTitle() : savedJournalEntry.get().getTitle());
            savedJournalEntry.get().setContent(newJournalEntry.getContent() != null && !newJournalEntry.getContent().isEmpty() ? newJournalEntry.getContent() : savedJournalEntry.get().getContent());
            journalEntryService.saveEntry(savedJournalEntry.get());
            return new ResponseEntity<>(savedJournalEntry.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
