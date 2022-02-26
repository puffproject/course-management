package com.unityTest.courseManagement.restApi;

import com.unityTest.courseManagement.entity.AssignmentEnrollment;
import com.unityTest.courseManagement.entity.SourceType;
import com.unityTest.courseManagement.entity.VoteAction;
import com.unityTest.courseManagement.models.api.response.page.AssignmentEnrollmentPage;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.awt.print.Pageable;
import java.security.Principal;

@Api(value = "User Management API", tags = "User API", description = "Manage user resources")
@Validated
@RequestMapping(value = "/user")
public interface UserApi extends BaseApi {


	/**
	 * GET endpoint to retrieve the user's enrolled assignments, filtered by assignment id, assignment
	 * name, course code, course term, and course academic year
	 *
	 * @return List of assignment enrollment objects matching criterion
	 */
	@ApiOperation(
		value = "retrieves a list of enrolled assignments",
		nickname = "getAssignmentEnrollments",
		response = AssignmentEnrollmentPage.class,
		produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/assignments", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<AssignmentEnrollmentPage> getAssignmentEnrollments(
			Pageable pageable,
			@ApiIgnore Principal principal,
			@ApiParam("Assignment id") @RequestParam(value = "assignment.id", required = false) Integer id,
			@ApiParam("Assignment name") @RequestParam(value = "assignment.name", required = false) String name,
			@ApiParam("Course code") @RequestParam(value = "assignment.course.code", required = false) String code,
			@ApiParam("Course term") @RequestParam(value = "assignment.course.term", required = false) String term,
			@ApiParam("Course academic year")
			@RequestParam(value = "assignment.course.academicYear", required = false) Integer academicYear,
			@ApiParam("pinned") @RequestParam(value = "pinned", required = false) Boolean pinned);


	/**
	 * POST endpoint to create an assignment enrollment for an assignment
	 *
	 * @return Create assignment enrollment
	 */
	@ApiOperation(
		value = "Create assignment enrollment",
		nickname = "enrollUser",
		response = AssignmentEnrollment.class,
		produces = MediaType.APPLICATION_JSON_VALUE,
		consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses({@ApiResponse(code = 201, message = "Enrolled", response = AssignmentEnrollment.class)})
	@PostMapping(
		value = "/assignment/{assignmentId}/enroll",
		consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<AssignmentEnrollment> enrollUser(
			@ApiIgnore Principal principal,
			@ApiParam(value = "pin") Boolean pin,
			@ApiParam(value = "assignment id", required = true)
			@PathVariable(value = "assignmentId") Integer assignmentId);


	/**
	 * DELETE endpoint to unenroll a user from an assignment
	 *
	 * @param assignmentId Id of assignment to unenroll from
	 */
	@ApiOperation(value = "Delete assignment enrollment", nickname = "unenrollUser")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@DeleteMapping(value = "/assignment/{assignmentId}/unenroll")
	void unenrollUser(
			@ApiIgnore Principal principal,
			@ApiParam(value = "assignment id", required = true)
			@PathVariable(value = "assignmentId") Integer assignmentId);


	/**
	 * GET endpoint to retrieve the vote action of a user on a target source item.
	 *
	 * @return Vote action of the user, null if vote doesn't exist
	 */
	@ApiOperation(
		value = "retrieves the vote action on a target item",
		nickname = "getVoteAction",
		response = VoteAction.class,
		produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/engagement/{sourceType}/{sourceItemId}/vote", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<VoteAction> getVoteAction(
			@ApiIgnore Principal principal,
			@ApiParam(value = "source type", required = true) @PathVariable(value = "sourceType") SourceType sourceType,
			@ApiParam(value = "source item id", required = true)
			@PathVariable(value = "sourceItemId") Integer sourceItemId

	);


}
