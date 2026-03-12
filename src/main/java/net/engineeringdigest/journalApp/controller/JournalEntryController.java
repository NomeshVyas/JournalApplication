package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {
    private Map<Long, JournalEntry> journalEntries = new HashMap<>();

    @GetMapping
    public List<JournalEntry> getAll() {
        return new ArrayList<>(journalEntries.values());
    }

    @PostMapping
    public boolean createEntry(@RequestBody JournalEntry journalEntry) {
        journalEntries.put(journalEntry.getId(), journalEntry);
        return true;
    }

    @GetMapping("id/{id}")
    public JournalEntry getJournalEntryById(@PathVariable Long id) {
        return journalEntries.get(id);
    }

    @DeleteMapping("id/{id}")
    public boolean deleteJournalEntryById(@PathVariable Long id) {
        journalEntries.remove(id);
        return true;
    }

    @PutMapping("id/{id}")
    public JournalEntry updateJournalEntryById(@PathVariable Long id, @RequestBody JournalEntry journalEntry) {
        return journalEntries.put(id, journalEntry);
    }
}
