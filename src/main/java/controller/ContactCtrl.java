package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ContactDTO;
import entity.Contact;
import exception.IdNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.ContactService;

import java.util.List;

/*
The controller includes the following endpoints:
        GET /contacts: Retrieves all contacts.
        GET /contacts/{id}: Retrieves a contact by its ID.
        POST /contacts: Creates a new contact.
        PUT /contacts/{id}:Updates an existing contact.
        DELETE /contacts/{id}: Deletes a contact.
        GET /contacts/search:Searches contacts based on provided parameters (keyword, firstName, lastName, email).
*/


@RestController
@Slf4j
@RequestMapping(name = "/contacts")
public class ContactCtrl {
    private final ModelMapper modelMapper;

    public ContactCtrl(ModelMapper modelMapper, ObjectMapper objectMapper, ContactService contactService) {
        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
        this.contactService = contactService;
    }

    private final ObjectMapper objectMapper;

    private final ContactService contactService;

    @PostMapping(value = "/save", consumes = "application/json")
    public ResponseEntity<Contact> createContact(ContactDTO contactDto) {
        Contact createdContact = null;
        try {
            log.info("Creating a new contact : {}", objectMapper.writeValueAsString(contactDto));
            if (contactDto != null) {
                Contact contact = modelMapper.map(contactDto, Contact.class);

                if (contact != null) {
                    createdContact = contactService.create(contact);
                }
            }
        } catch (Exception e) {
            log.error("UnExcepted error" + e.getMessage());

        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdContact);
    }

    @GetMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<Contact> getContactById(@PathVariable Long id) throws IdNotFoundException {
        log.info("Retrieving contact by ID: {}", id);

        if (id == null) {
            throw new IdNotFoundException();
        }
        ResponseEntity<Contact> contactById = contactService.getContactById(id);
        return contactById;

    }

    @PutMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<Contact> updateContact(@PathVariable Long id, @RequestBody Contact contact) throws IdNotFoundException {
        log.info("Updating contact with ID: {}", id);
        if (id == null) {
            throw new IdNotFoundException();
        }
        return contactService.updateContact(id, contact);

    }


    @DeleteMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<Boolean> deleteContact(@PathVariable Long id) throws IdNotFoundException {
        log.info("Deleting contact with ID: {}", id);
        if (id == null) {
            throw new IdNotFoundException();
        }
        return contactService.deleteContact(id);
    }


    @GetMapping
    public ResponseEntity<List<Contact>> getAllContacts() {
        log.info("Getting all contacts");
        List<Contact> contacts = contactService.getAllContacts();
        return ResponseEntity.ok(contacts);

    }

    @GetMapping(value = "/search", consumes = "application/json")
    public ResponseEntity<List<Contact>> searchContacts(@RequestParam(required = false) String keyword,
                                                        @RequestParam(required = false) String firstName,
                                                        @RequestParam(required = false) String lastName,
                                                        @RequestParam(required = false) String email) {
        log.info("Getting all contacts By Keyword, firstName,lastName,email");
        List<Contact> contacts;
        if (keyword != null) {
            contacts = contactService.searchContacts(keyword);
        } else if (firstName != null) {
            contacts = contactService.searchContactsByFirstName(firstName);
        } else if (lastName != null) {
            contacts = contactService.searchContactsByLastName(lastName);
        } else if (email != null) {
            contacts = contactService.searchContactsByEmail(email);
        } else {
            contacts = contactService.getAllContacts();
        }
        return ResponseEntity.ok(contacts);
    }


}
