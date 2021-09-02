package com.unityTest.courseManagement.service;

import com.unityTest.courseManagement.entity.*;
import com.unityTest.courseManagement.models.AssignmentAttributeName;
import com.unityTest.courseManagement.models.Term;
import com.unityTest.courseManagement.exception.ElementNotFoundException;
import com.unityTest.courseManagement.repository.AssignmentAttrRepository;
import com.unityTest.courseManagement.repository.AssignmentRepository;
import com.unityTest.courseManagement.repository.CourseAttrRepository;
import com.unityTest.courseManagement.repository.CourseRepository;
import com.unityTest.courseManagement.utils.specification.AndSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AssignmentService {
    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private AssignmentAttrRepository assignmentAttrRepository;

    /**
     * Create or update an assignment
     *
     * @param assignment Assignment to create or update
     * @return Assignment created
     */
    public Assignment createAssignment(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    /**
     * Get a list of courses that match the passed arguments
     *
     * @param id Id of assignment to fetch
     * @param courseId Course id to match
     * @param name Assignment name to match
     * @param dueDate Assignment due date to match
     * @return List of assignments with fields matching the passed arguments
     */
    public List<Assignment> getAssignments(Integer id, Integer courseId, String name, Date dueDate) {
        return getAssignments(Pageable.unpaged(), id, courseId, name, dueDate).getContent();
    }

    /**
     * Get a Page view of assignments that match the passed arguments
     *
     * @param id Id of assignment to fetch
     * @param courseId Course id to match
     * @param name Assignment name to match
     * @param dueDate Assignment due date to match
     * @return Page view of assignments from repository matching passed arguments and formatted using the
     *         pageable param
     */
    public Page<Assignment> getAssignments(
            Pageable pageable,
            Integer id,
            Integer courseId,
            String name,
            Date dueDate) {
        Specification<Course> spec = new AndSpecification<Course>()
                .equal(id, Course_.ID)
                .equal(courseId, Course_.CODE)
                .equal(level, Course_.LEVEL)
                .equal(term, Course_.TERM)
                .equal(academicYear, Course_.ACADEMIC_YEAR)
                .getSpec();
        return courseRepository.findAll(spec, pageable);
    }

}
