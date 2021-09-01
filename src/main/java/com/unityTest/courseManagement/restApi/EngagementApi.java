package com.unityTest.courseManagement.restApi;

import com.unityTest.courseManagement.entity.SourceType;
import com.unityTest.courseManagement.entity.VoteAction;
import com.unityTest.courseManagement.models.api.request.CommentBody;
import com.unityTest.courseManagement.models.api.response.CommentView;
import com.unityTest.courseManagement.models.api.response.page.CommentPage;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import java.security.Principal;

@Api(value = "Engagement API", tags = "Engagement API", description = "Perform and manage engagement actions")
@Validated
@RequestMapping(value = "/engagement/{sourceType}/{sourceItemId}")
public interface EngagementApi extends BaseApi {

	/**
	 * POST endpoint to vote on a source item
	 */
	@ApiOperation(value = "Vote on a source item", nickname = "voteOnSourceItem")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@PostMapping(value = "/vote")
	void voteOnSourceItem(
			@ApiIgnore Principal principal,
			@ApiParam(value = "Type of source", required = true, allowableValues = "case,suite,comment")
			@PathVariable(value = "sourceType") SourceType sourceType,
			@ApiParam(value = "Id of source item", required = true)
			@PathVariable(value = "sourceItemId") Integer sourceItemId,
			@ApiParam(value = "Vote action", required = true) @RequestParam(value = "action") VoteAction action);

	/**
	 * POST endpoint to add a comment under a source item
	 *
	 * @return Created comment
	 */
	@ApiOperation(
		value = "Add a comment on a source item",
		nickname = "postComment",
		response = CommentView.class,
		produces = MediaType.APPLICATION_JSON_VALUE,
		consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses({@ApiResponse(code = 201, message = "Created", response = CommentView.class)})
	@PostMapping(
		value = "/comment",
		produces = MediaType.APPLICATION_JSON_VALUE,
		consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<CommentView> commentOnSourceItem(
			@ApiIgnore Principal principal,
			@ApiParam(value = "Type of source", required = true, allowableValues = "case")
			@PathVariable(value = "sourceType") SourceType sourceType,
			@ApiParam(value = "Id of source item", required = true)
			@PathVariable(value = "sourceItemId") Integer sourceItemId,
			@ApiParam(value = "Comment details to post", required = true) @Valid @RequestBody CommentBody commentBody);

	/**
	 * GET endpoint to retrieve comments under a source item
	 * 
	 * @return Pageable view of comments posted under the source item
	 */
	@ApiOperation(
		value = "Retrieve comments under a source item",
		nickname = "getComments",
		response = CommentPage.class,
		produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/comments", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<CommentPage> getCommentsOnSourceItem(
			Pageable pageable,
			@ApiParam(value = "Type of source", required = true, allowableValues = "case")
			@PathVariable(value = "sourceType") SourceType sourceType,
			@ApiParam(value = "Id of source item", required = true)
			@PathVariable(value = "sourceItemId") Integer sourceItemId,
			@ApiParam(value = "Author id") @RequestParam(value = "authorId", required = false) String authorId);

	/**
	 * PATCH endpoint to update the contents of a comment under a source item. Restricted to owner of
	 * comment.
	 *
	 * @param commentId Id of comment to update
	 * @return Updated comment
	 */
	@ApiOperation(
		value = "Update a comment found under a source item",
		nickname = "updateCommentBody",
		response = CommentView.class,
		produces = MediaType.APPLICATION_JSON_VALUE,
		consumes = MediaType.APPLICATION_JSON_VALUE)
	@PatchMapping(
		value = "/comment/{commentId}",
		produces = MediaType.APPLICATION_JSON_VALUE,
		consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<CommentView> updateCommentOnSourceItem(
			@ApiIgnore Principal principal,
			@ApiParam(value = "Type of source", required = true, allowableValues = "case")
			@PathVariable(value = "sourceType") SourceType sourceType,
			@ApiParam(value = "Id of source item", required = true)
			@PathVariable(value = "sourceItemId") Integer sourceItemId,
			@ApiParam(value = "Id of comment to delete", required = true)
			@PathVariable(value = "commentId") Integer commentId,
			@ApiParam(value = "Comment details to update") @Valid @RequestBody CommentBody commentBody);

	/**
	 * DELETE endpoint to remove a comment. Restricted to administrators and the author of the comment.
	 *
	 * @param commentId Id of comment to delete
	 */
	@ApiOperation(value = "Delete a comment on a source item", nickname = "deleteComment")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@DeleteMapping(value = "/comment/{commentId}")
	void deleteCommentOnSourceItem(
			@ApiIgnore Principal principal,
			@ApiParam(value = "Type of source", required = true, allowableValues = "case")
			@PathVariable(value = "sourceType") SourceType sourceType,
			@ApiParam(value = "Id of source item", required = true)
			@PathVariable(value = "sourceItemId") Integer sourceItemId,
			@ApiParam(value = "Id of comment to delete", required = true)
			@PathVariable(value = "commentId") Integer commentId);
}
