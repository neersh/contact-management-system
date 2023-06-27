package repository;

import entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepo extends JpaRepository<Contact, Long> {

    @Query("SELECT c FROM Contact c WHERE LOWER(c.firstName) LIKE %:keyword% OR LOWER(c.lastName) LIKE %:keyword% OR LOWER(c.email) LIKE %:keyword%")
    List<Contact> searchContactsByKeywordIgnoreCase(String toLowerCase);

    List<Contact> findByFirstNameContainingIgnoreCase(String firstName);

    List<Contact> findByLastNameContainingIgnoreCase(String lastName);

    List<Contact> findByEmailContainingIgnoreCase(String email);
}
