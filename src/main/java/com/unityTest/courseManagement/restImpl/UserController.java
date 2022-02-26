package com.unityTest.courseManagement.restImpl;

import com.unityTest.courseManagement.entity.*;
import com.unityTest.courseManagement.models.api.response.page.AssignmentEnrollmentPage;
import com.unityTest.courseManagement.restApi.UserApi;
import com.unityTest.courseManagement.service.UserService;
import com.unityTest.courseManagement.utils.Utils;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class UserController implements UserApi {

	@Autowired
	private UserService userService;

	// @Autowired
	// private AssignmentRepository assignmentRepository;


	@Override
	public ResponseEntity<AssignmentEnrollmentPage> getAssignmentEnrollments(
			Pageable pageable,
			Principal principal,
			Integer id,
			String name,
			String code,
			String term,
			Integer academicYear,
			Boolean pinned) {

		// get user id
		AccessToken token = Utils.getAuthToken(principal);
		String userId = token.getSubject();


		Page<AssignmentEnrollment> enrollmentPage;

		if (pinned != null) {
			enrollmentPage =
				userService.getAssignmentEnrollments(pageable, id, name, code, term, academicYear, userId, pinned);

			AssignmentEnrollmentPage page = new AssignmentEnrollmentPage(enrollmentPage);
			return new ResponseEntity<>(page, HttpStatus.OK);
		}

		// get a list of pinned, and a list of unpinned enrollments
		List<AssignmentEnrollment> pinnedEnrollment =
			userService.getAssignmentEnrollments(id, name, code, term, academicYear, userId, true);
		List<AssignmentEnrollment> notPinnedEnrollment =
			userService.getAssignmentEnrollments(id, name, code, term, academicYear, userId, false);

		// combine the two lists
		List<AssignmentEnrollment> enrollmentList =
			Stream.concat(pinnedEnrollment.stream(), notPinnedEnrollment.stream()).collect(Collectors.toList());

		// convert list to page
		enrollmentPage = new PageImpl<>(enrollmentList, pageable, enrollmentList.size());


		AssignmentEnrollmentPage page = new AssignmentEnrollmentPage(enrollmentPage);
		return new ResponseEntity<>(page, HttpStatus.OK);

	}

	/**
	 * TODO can't be implemented until Assignment endpoint is implemented
	 */
	@Override
	public ResponseEntity<AssignmentEnrollment> enrollUser(Principal principal, Boolean pin, Integer assignmentId) {

		// // get user id
		// AccessToken token = Utils.getAuthToken(principal);
		// String userId = token.getSubject();
		//
		// Specification<Assignment> spec = new AndSpecification<Assignment>()
		// .equal(assignmentId, Assignment_.ID)
		// .getSpec();
		// Optional<Assignment> existingEnrollment = assignmentRepository.findOne(spec);
		// return new ResponseEntity<>(userService.enrollUser(userId,assignment, pin), HttpStatus.CREATED);
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	}

	@Override
	public void unenrollUser(Principal principal, Integer assignmentId) {
		// get user id
		AccessToken token = Utils.getAuthToken(principal);
		String userId = token.getSubject();

		userService.unenrollUser(userId, assignmentId);
	}


	@Override
	public ResponseEntity<VoteAction> getVoteAction(Principal principal, SourceType sourceType, Integer sourceItemId) {
		// get user id
		AccessToken token = Utils.getAuthToken(principal);
		String userId = token.getSubject();

		VoteAction voteAction = userService.getVoteAction(sourceType, sourceItemId, userId);

		return new ResponseEntity<>(voteAction, HttpStatus.OK);
	}



}
