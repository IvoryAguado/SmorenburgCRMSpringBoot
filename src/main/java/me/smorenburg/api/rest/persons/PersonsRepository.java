package me.smorenburg.api.rest.persons;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by stephan on 20.03.16.
 */
public interface PersonsRepository extends JpaRepository<Person, Long> {
    Person findByName(String name);

    List<Person> findAll();

    Person findByEmail(String email);
}
