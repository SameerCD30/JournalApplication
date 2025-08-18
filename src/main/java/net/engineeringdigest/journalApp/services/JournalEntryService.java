package net.engineeringdigest.journalApp.services;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.repository.JournalEntryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepo journalentryrepo;

    public void saveEntry(JournalEntry journalEntry){
        journalentryrepo.save(journalEntry);
    }
}
 /*     controller --> service --> repository     */