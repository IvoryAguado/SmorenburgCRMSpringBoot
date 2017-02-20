package me.smorenburg.api.rest.persons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${jwt.routes.apiendpoint}")
public class PersonRestService {

    @Autowired
    private PersonsRepository personsRepository;

    @RequestMapping(path = "/persons", method = RequestMethod.GET)
    public ResponseEntity<List<Person>> getPersons() throws Exception {
        return ResponseEntity.ok(personsRepository.findAll());
    }

    @RequestMapping(path = "/persons/{id}", method = RequestMethod.GET)
    public ResponseEntity<Person> getPerson(@PathVariable("id") Long id) throws Exception {
        Person byId = personsRepository.findOne(id);
//        if (byId == null)
//            throw new ResourceNotFoundException("Person not found with id: " + id);
        return ResponseEntity.ok(byId);
    }

    @RequestMapping(path = "/persons/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Person> editPerson(@PathVariable("id") Long id, @RequestBody Person person) throws Exception {
        person.setId(id);
        Person byId = personsRepository.findOne(id);
        if (byId == null)
            throw new ResourceNotFoundException("Person to update not found with id: " + id);
        return ResponseEntity.ok(personsRepository.saveAndFlush(person));
    }

    @RequestMapping(path = "/persons", method = RequestMethod.POST)
    public ResponseEntity<Person> createPerson(@RequestBody Person person) throws Exception {
        person.setId(-1L);//Always save one
        return ResponseEntity.ok(personsRepository.saveAndFlush(person));
    }

    @RequestMapping(path = "/persons/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Person> deletePerson(@PathVariable("id") Long id) throws Exception {
        Person byId = personsRepository.findOne(id);
//        if (byId == null)
//            throw new ResourceNotFoundException("Person to delete not found with id: " + id);
        personsRepository.delete(id);
        return ResponseEntity.ok(byId);
    }

}
