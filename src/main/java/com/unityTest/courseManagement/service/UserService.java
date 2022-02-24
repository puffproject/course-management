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
	 * @param assignment Assignment user is enrolled in
	 * @param userId User id of the user
	 * @param isPinned True if user has pinned the course
	 * @return List of assignment enrollments with fields matching the passed arguments
	 */
	public List<AssignmentEnrollment> getAssignmentEnrollments(
			Integer id,
			Assignment assignment,
			String userId,
			Boolean isPinned) {
		return getAssignmentEnrollments(Pageable.unpaged(), id, assignment, userId, isPinned).getContent();
	}

	/**
	 * Get a Page view of Assignment Enrollments that match the passed arguments
	 *
	 * @param pageable Pageable object specifying page size, sort and index
	 * @param id Id of course to fetch
	 * @param assignment Assignment user is enrolled in
	 * @param userId User id of the user
	 * @param isPinned True if user has pinned the course
	 * @return Page view of assignment enrollments from repository matching passed arguments and
	 *         formatted using the pageable param
	 */
	public Page<AssignmentEnrollment> getAssignmentEnrollments(
			Pageable pageable,
			Integer id,
			Assignment assignment,
			String userId,
			Boolean isPinned) {
		Specification<AssignmentEnrollment> spec = new AndSpecification<AssignmentEnrollment>()
			.equal(id, AssignmentEnrollment_.ID)
			.equal(assignment, AssignmentEnrollment_.ASSIGNMENT)
			.equal(userId, AssignmentEnrollment_.USER_ID)
			.equal(isPinned, AssignmentEnrollment_.IS_PINNED)
			.getSpec();
		return assignmentEnrollmentRepository.findAll(spec, pageable);
	}

	/**
	 * Save a new Vote to the repository, or update an existing Vote to respect the unique constraints
	 * of the table.
	 *
	 * @param enrollment The assignment enrollment
	 * @param toPin whether to pin or not the assignment enrollment
	 * @return Created or updated assignment enrollment
	 */
	public AssignmentEnrollment enrollUser(AssignmentEnrollment enrollment, Boolean toPin) {
		// check if there is an AssignmentEnrollment with same user and assignment already exists
		Specification<AssignmentEnrollment> spec = new AndSpecification<AssignmentEnrollment>()
			.equal(enrollment.getUserId(), AssignmentEnrollment_.USER_ID)
			.equal(enrollment.getAssignment(), AssignmentEnrollment_.ASSIGNMENT)
			.getSpec();
		Optional<AssignmentEnrollment> existingEnrollment = assignmentEnrollmentRepository.findOne(spec);
		enrollment.setId(existingEnrollment.map(AssignmentEnrollment::getId).orElse(0));

		enrollment.setPinned(toPin);

		return assignmentEnrollmentRepository.save(enrollment);
	}

	/**
	 * Unenroll a user from an assignment
	 *
	 * @param id Id of assignment to unenroll from
	 */
	public void unenrollUser(int id) {
		assignmentEnrollmentRepository.deleteById(id);
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