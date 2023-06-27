package service;

import entity.Contact;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import repository.ContactRepo;

import java.util.List;
import java.util.Optional;

@Service
public class ContactServiceImpl implements ContactService {
    private final ContactRepo contactRepo;

    public ContactServiceImpl(ContactRepo contactRepo) {
        this.contactRepo = contactRepo;
    }

    @Override
    public Contact create(Contact contact) {
        return contactRepo.save(contact);

    }

    @Override
    public ResponseEntity<Contact> getContactById(Long id) {

        Optional<Contact> byId = contactRepo.findById(id);
        return byId.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());

    }

    @Override
    public ResponseEntity<Contact> updateContact(Long id, Contact contact) {
        Contact existingContact = contactRepo.findById(id).orElse(null);
        if (existingContact != null) {
            existingContact.setFirstName(contact.getFirstName());
            existingContact.setLastName(contact.getLastName());
            existingContact.setEmail(contact.getEmail());
            existingContact.setPhoneNumber(contact.getPhoneNumber());
            return new ResponseEntity<>(contactRepo.save(existingContact), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Boolean> deleteContact(Long id) {

        Optional<Contact> contact = contactRepo.findById(id);
        if (contact.isPresent()) {
            contactRepo.delete(contact.get());
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.OK);
    }

    @Override
    public List<Contact> getAllContacts() {

        return contactRepo.findAll();

    }

    @Override
    public List<Contact> searchContacts(String keyword) {
        return contactRepo.searchContactsByKeywordIgnoreCase(keyword.toLowerCase());


    }

    @Override
    public List<Contact> searchContactsByFirstName(String firstName) {
        return contactRepo.findByFirstNameContainingIgnoreCase(firstName);

    }

    @Override
    public List<Contact> searchContactsByLastName(String lastName) {
        return contactRepo.findByLastNameContainingIgnoreCase(lastName);

    }

    @Override
    public List<Contact> searchContactsByEmail(String email) {
        return contactRepo.findByEmailContainingIgnoreCase(email);
    }
}
