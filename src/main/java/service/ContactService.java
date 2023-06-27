package service;

import dto.SearchContactsCriteria;
import entity.Contact;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ContactService {
    Contact create(Contact contact);

    ResponseEntity<Contact> getContactById(Long id);

    ResponseEntity<Contact> updateContact(Long id, Contact contact);

    ResponseEntity<Boolean> deleteContact(Long id);

    List<Contact> getAllContacts();

    List<Contact> searchContacts(String keyword);

    List<Contact> searchContactsByFirstName(String firstName);

    List<Contact> searchContactsByLastName(String lastName);

    List<Contact> searchContactsByEmail(String email);

    List<Contact> search(SearchContactsCriteria searchContactsCriteria);
}
