package com.unityTest.courseManagement.repository;

import com.unityTest.courseManagement.entity.AssignmentEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentEnrollmentRepository
		extends JpaRepository<AssignmentEnrollment, Integer>, JpaSpecificationExecutor<AssignmentEnrollment> {
}
