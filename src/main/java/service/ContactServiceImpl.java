package service;

import dto.SearchContactsCriteria;
import entity.Contact;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import repository.ContactRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ContactServiceImpl implements ContactService {
    private final ContactRepo contactRepo;
    @PersistenceContext
    private EntityManager entityManager;

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

    //Dynamic Query
    @Override
    public List<Contact> search(SearchContactsCriteria searchCriteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Contact> query = criteriaBuilder.createQuery(Contact.class);
        Root<Contact> root = query.from(Contact.class);
        query.select(root);

        List<Predicate> predicates = new ArrayList<>();

        if (searchCriteria.getFirstName() != null) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + searchCriteria.getFirstName().toLowerCase() + "%"));
        }

        if (searchCriteria.getLastName() != null) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + searchCriteria.getLastName().toLowerCase() + "%"));
        }

        if (searchCriteria.getEmail() != null) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + searchCriteria.getEmail().toLowerCase() + "%"));
        }

        if (!predicates.isEmpty()) {
            query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        }

        TypedQuery<Contact> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }

}

