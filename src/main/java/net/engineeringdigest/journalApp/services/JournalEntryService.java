package net.engineeringdigest.journalApp.services;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepo journalEntryRepo;

    @Autowired
    private UserService userService;


    // eg : if a journal entry is created but...could not be saved to a user due to some error... then it would revert back the post
    @Transactional // if things fail it will revert back the changes
    public void saveEntry(JournalEntry journalEntry, String userName) {
        try{
            User user = userService.findByuserName(userName);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepo.save(journalEntry);
            user.getJournalEntries().add(saved);
            userService.saveUser(user);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while saving the entry",e);
        }

    }
    public void saveEntry(JournalEntry journalEntry) {
        journalEntryRepo.save(journalEntry);
    }

    public List<JournalEntry> getAll() {
        return journalEntryRepo.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id) {
        return journalEntryRepo.findById(id);
    }

    public void deleteById(ObjectId entryId, String userName) {
        // 1. Delete entry from journal_entries
        journalEntryRepo.deleteById(entryId);

        // 2. Remove reference from user (DBref wala)
        User user = userService.findByuserName(userName);
        if (user != null) {
            user.getJournalEntries().removeIf(entry -> entry.getId().equals(entryId));
            userService.saveUser(user); // save updated user without the deleted reference
        }
    }
}
