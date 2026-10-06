package com.familytree.repository;

import com.familytree.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findByTreeIdOrderByBirthDateAscIdAsc(Long treeId);

    @Modifying
    @Query("update Person p set p.father = null where p.father.id = :id")
    void detachAsFather(Long id);

    @Modifying
    @Query("update Person p set p.mother = null where p.mother.id = :id")
    void detachAsMother(Long id);

    void deleteByTreeId(Long treeId);
}
