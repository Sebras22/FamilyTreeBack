package com.familytree.repository;

import com.familytree.model.FamilyTree;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FamilyTreeRepository extends JpaRepository<FamilyTree, Long> {
}
