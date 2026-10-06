package com.familytree.controller;

import com.familytree.dto.TreeRequest;
import com.familytree.model.FamilyTree;
import com.familytree.repository.FamilyTreeRepository;
import com.familytree.repository.PersonRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/trees")
public class TreeController {

    public record TreeResponse(Long id, String name) {
        static TreeResponse from(FamilyTree t) {
            return new TreeResponse(t.getId(), t.getName());
        }
    }

    private final FamilyTreeRepository trees;
    private final PersonRepository persons;

    public TreeController(FamilyTreeRepository trees, PersonRepository persons) {
        this.trees = trees;
        this.persons = persons;
    }

    @GetMapping
    public List<TreeResponse> list() {
        return trees.findAll().stream().map(TreeResponse::from).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TreeResponse create(@Valid @RequestBody TreeRequest req) {
        return TreeResponse.from(trees.save(new FamilyTree(req.name())));
    }

    @PutMapping("/{id}")
    public TreeResponse rename(@PathVariable Long id, @Valid @RequestBody TreeRequest req) {
        FamilyTree tree = trees.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        tree.setName(req.name());
        return TreeResponse.from(trees.save(tree));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void delete(@PathVariable Long id) {
        if (!trees.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        persons.deleteByTreeId(id);
        trees.deleteById(id);
    }
}
