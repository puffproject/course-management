package com.unityTest.courseManagement.service;

import com.unityTest.courseManagement.entity.*;
import com.unityTest.courseManagement.repository.AssignmentEnrollmentRepository;
import com.unityTest.courseManagement.utils.specification.AndSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

	@Autowired
	private AssignmentEnrollmentRepository assignmentEnrollmentRepository;

	@Autowired
	private VoteService voteService;

	/**
	 * get a list of Assignment Enrollments that match the passed arguments
	 *
	 * @param id Id of course to fetch
	 * @param name Assignment name
	 * @param code Course code
	 * @param term Course term
	 * @param academicYear acadamic year of the course
	 * @param userId User id of the user
	 * @param isPinned True if user has pinned the course
	 * @return List of assignment enrollments with fields matching the passed arguments
	 */
	public List<AssignmentEnrollment> getAssignmentEnrollments(
			Integer id,
			String name,
			String code,
			String term,
			Integer academicYear,
			String userId,
			Boolean isPinned) {
		return getAssignmentEnrollments(Pageable.unpaged(), id, name, code, term, academicYear, userId, isPinned)
			.getContent();
	}

	/**
	 * Get a Page view of Assignment Enrollments that match the passed arguments
	 *
	 * @param pageable Pageable object specifying page size, sort and index
	 * @param id Id of course to fetch
	 * @param name Assignment name
	 * @param code Course code
	 * @param term Course term
	 * @param academicYear acadamic year of the course
	 * @param userId User id of the user
	 * @param isPinned True if user has pinned the course
	 * @return Page view of assignment enrollments from repository matching passed arguments and
	 *         formatted using the pageable param
	 */
	public Page<AssignmentEnrollment> getAssignmentEnrollments(
			Pageable pageable,
			Integer id,
			String name,
			String code,
			String term,
			Integer academicYear,
			String userId,
			Boolean isPinned) {
		Specification<AssignmentEnrollment> spec = new AndSpecification<AssignmentEnrollment>()
			.equal(id, AssignmentEnrollment_.ID)
			.equal(name, AssignmentEnrollment_.ASSIGNMENT, Assignment_.NAME)
			.equal(code, AssignmentEnrollment_.ASSIGNMENT, Assignment_.COURSE, Course_.CODE)
			.equal(term, AssignmentEnrollment_.ASSIGNMENT, Assignment_.COURSE, Course_.TERM)
			.equal(academicYear, AssignmentEnrollment_.ASSIGNMENT, Assignment_.COURSE, Course_.ACADEMIC_YEAR)
			.equal(userId, AssignmentEnrollment_.USER_ID)
			.equal(isPinned, AssignmentEnrollment_.IS_PINNED)
			.getSpec();
		return assignmentEnrollmentRepository.findAll(spec, pageable);
	}

	/**
	 * Save a new Vote to the repository, or update an existing Vote to respect the unique constraints
	 * of the table.
	 *
	 * @param assignment The assignment to enroll into
	 * @param toPin whether to pin or not the assignment enrollment
	 * @return Created or updated assignment enrollment
	 */
	public AssignmentEnrollment enrollUser(String userId, Assignment assignment, Boolean toPin) {
		// check if there is an AssignmentEnrollment with same user and assignment already exists
		Specification<AssignmentEnrollment> spec = new AndSpecification<AssignmentEnrollment>()
			.equal(userId, AssignmentEnrollment_.USER_ID)
			.equal(assignment.getId(), AssignmentEnrollment_.ASSIGNMENT, Assignment_.ID)
			.getSpec();
		Optional<AssignmentEnrollment> existingEnrollment = assignmentEnrollmentRepository.findOne(spec);


		// create assignment enrollment
		AssignmentEnrollment enrollment = new AssignmentEnrollment();
		enrollment.setId(existingEnrollment.map(AssignmentEnrollment::getId).orElse(0));
		enrollment.setAssignment(assignment);
		enrollment.setUserId(userId);
		enrollment.setPinned(toPin);

		return assignmentEnrollmentRepository.save(enrollment);
	}

	/**
	 * Unenroll a user from an assignment
	 *
	 * @param userId Id of user enrolled
	 * @param assignmentId Id of assignment to unenroll from
	 */
	public void unenrollUser(String userId, int assignmentId) {
		Specification<AssignmentEnrollment> spec = new AndSpecification<AssignmentEnrollment>()
			.equal(userId, AssignmentEnrollment_.USER_ID)
			.equal(assignmentId, AssignmentEnrollment_.ASSIGNMENT, Assignment_.ID)
			.getSpec();
		Optional<AssignmentEnrollment> enrollment = assignmentEnrollmentRepository.findOne(spec);

		if (!enrollment.isPresent())
			return;

		assignmentEnrollmentRepository.deleteById(enrollment.get().getId());
	}


	/**
	 * get the vote action of a user on a target source item. Returns null if no vote is found
	 *
	 * @param sourceType SourceType of target source item
	 * @param sourceItemId Source item id
	 * @param authorId Id of target user
	 * @return Vote action of the user, null if vote doesn't exist
	 */
	public VoteAction getVoteAction(SourceType sourceType, Integer sourceItemId, String authorId) {
		return voteService.getVoteAction(sourceType, sourceItemId, authorId);
	}



}