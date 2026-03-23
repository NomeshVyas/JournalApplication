package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;
    @Autowired
    private UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String username) {
        User user = userService.findByUsername(username);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<JournalEntry> all = user.getJournalEntries();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @PostMapping("/{username}")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry journalEntry,  @PathVariable String username) {
        try {
            User user = userService.findByUsername(username);
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            journalEntryService.saveEntry(journalEntry, username);
            return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{id}")
    public ResponseEntity<?> getJournalEntryById(@PathVariable ObjectId id) {
        Optional<JournalEntry> journalEntry = journalEntryService.getJournalEntryById(id);
        return journalEntry
                .map(entity -> new ResponseEntity<>(entity, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("{username}/{id}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable String username, @PathVariable ObjectId id) {
        journalEntryService.deleteJournalEntryById(id, username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("{username}/{id}")
    public ResponseEntity<?> updateJournalEntryById(@PathVariable String username, @PathVariable ObjectId id, @RequestBody JournalEntry newJournalEntry) {
        Optional<JournalEntry> savedJournalEntry = journalEntryService.getJournalEntryById(id);
        if(savedJournalEntry.isPresent()) {
            savedJournalEntry.get().setTitle(!newJournalEntry.getTitle().isEmpty() ? newJournalEntry.getTitle() : savedJournalEntry.get().getTitle());
            savedJournalEntry.get().setContent(newJournalEntry.getContent() != null && !newJournalEntry.getContent().isEmpty() ? newJournalEntry.getContent() : savedJournalEntry.get().getContent());
            journalEntryService.saveEntry(savedJournalEntry.get());
            return new ResponseEntity<>(savedJournalEntry.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
