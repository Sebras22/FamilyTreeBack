package com.familytree.controller;

import com.familytree.dto.PersonRequest;
import com.familytree.dto.PersonResponse;
import com.familytree.model.FamilyTree;
import com.familytree.model.Person;
import com.familytree.repository.FamilyTreeRepository;
import com.familytree.repository.PersonRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PersonController {

    private final FamilyTreeRepository trees;
    private final PersonRepository persons;

    public PersonController(FamilyTreeRepository trees, PersonRepository persons) {
        this.trees = trees;
        this.persons = persons;
    }

    @GetMapping("/trees/{treeId}/persons")
    @Transactional(readOnly = true)
    public List<PersonResponse> list(@PathVariable Long treeId) {
        return persons.findByTreeIdOrderByBirthDateAscIdAsc(treeId).stream()
                .map(PersonResponse::from).toList();
    }

    @PostMapping("/trees/{treeId}/persons")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public PersonResponse create(@PathVariable Long treeId, @Valid @RequestBody PersonRequest req) {
        FamilyTree tree = trees.findById(treeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tree not found"));
        Person person = new Person();
        person.setTree(tree);
        apply(person, req, treeId);
        return PersonResponse.from(persons.save(person));
    }

    @PutMapping("/persons/{id}")
    @Transactional
    public PersonResponse update(@PathVariable Long id, @Valid @RequestBody PersonRequest req) {
        Person person = persons.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        apply(person, req, person.getTree().getId());
        return PersonResponse.from(persons.save(person));
    }

    @DeleteMapping("/persons/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void delete(@PathVariable Long id) {
        if (!persons.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        persons.detachAsFather(id);
        persons.detachAsMother(id);
        persons.deleteById(id);
    }

    private void apply(Person person, PersonRequest req, Long treeId) {
        person.setFirstName(req.firstName());
        person.setLastName(req.lastName());
        person.setBirthDate(req.birthDate());
        person.setDeathDate(req.deathDate());
        person.setFather(resolveParent(req.fatherId(), treeId, person));
        person.setMother(resolveParent(req.motherId(), treeId, person));
    }

    private Person resolveParent(Long parentId, Long treeId, Person child) {
        if (parentId == null) {
            return null;
        }
        if (parentId.equals(child.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A person cannot be their own parent");
        }
        Person parent = persons.findById(parentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parent not found"));
        if (!parent.getTree().getId().equals(treeId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parent belongs to another tree");
        }
        return parent;
    }
}
