package com.unityTest.courseManagement.repository;

import com.unityTest.courseManagement.entity.AssignmentAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentAttrRepository
        extends JpaRepository<AssignmentAttribute, Integer>, JpaSpecificationExecutor<AssignmentAttribute> {
}
