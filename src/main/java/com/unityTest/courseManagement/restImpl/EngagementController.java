package com.unityTest.courseManagement.restImpl;

import com.unityTest.courseManagement.entity.Comment;
import com.unityTest.courseManagement.entity.SourceType;
import com.unityTest.courseManagement.entity.Vote;
import com.unityTest.courseManagement.entity.VoteAction;
import com.unityTest.courseManagement.exception.ElementNotFoundException;
import com.unityTest.courseManagement.exception.UnsupportedActionException;
import com.unityTest.courseManagement.models.api.request.CommentBody;
import com.unityTest.courseManagement.models.api.response.Author;
import com.unityTest.courseManagement.models.api.response.CommentView;
import com.unityTest.courseManagement.models.api.response.page.CommentPage;
import com.unityTest.courseManagement.restApi.EngagementApi;
import com.unityTest.courseManagement.service.CommentService;
import com.unityTest.courseManagement.service.VoteService;
import com.unityTest.courseManagement.utils.Utils;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;
import java.util.Iterator;

/**
 * Rest controller for the /engagement endpoints
 */
@RestController
public class EngagementController implements EngagementApi {

	@Autowired
	private VoteService voteService;

<<<<<<< HEAD
	@Autowired
	private CommentService commentService;

	@Override
	public void voteOnSourceItem(Principal principal, SourceType sourceType, Integer sourceItemId, VoteAction action) {
		String authorId = Utils.getAuthToken(principal).getSubject();
		Vote vote =
			new Vote(0, sourceType, sourceItemId, authorId, Utils.parseToEnum(action.toString(), VoteAction.class));
		voteService.saveOrUpdateVote(vote);
		// Also need to synchronize the upvote count across the platform depending on source type
		voteService.updateVoteCountForSourceItem(sourceType, sourceItemId);
=======
	@Override
	public void voteOnSourceItem(
			Principal principal,
			SourceType sourceType,
			Integer sourceItemId,
			VoteActionOptions action) {
		String authorId = Utils.getAuthToken(principal).getSubject();
		switch (action) {
			case NOVOTE:
				voteService.removeVote(sourceType, sourceItemId, authorId);
				return;
			case UPVOTE:
			case DOWNVOTE:
				voteService
					.saveOrUpdateVote(
						new Vote(
								0, sourceType, sourceItemId, authorId,
								Utils.parseToEnum(action.toString(), VoteAction.class)));
				return;
			default:
				throw new UnsupportedVoteActionException(action.toString());
		}
>>>>>>> 84729ea... Implement vote endpoint for api
	}

	@Override
	public ResponseEntity<CommentView> commentOnSourceItem(
			Principal principal,
			SourceType sourceType,
			Integer sourceItemId,
			@Valid CommentBody commentBody) {
<<<<<<< HEAD
		if (sourceType != SourceType.CASE)
			throw new UnsupportedActionException(String.format("Comments are not supported for %s", sourceType));

		AccessToken token = Utils.getAuthToken(principal);
		String authorId = token.getSubject();
		Comment commentToPost =
			new Comment(0, sourceType, sourceItemId, authorId, commentBody.getContent(), new Date(), null, 0);
		Comment savedComment = commentService.saveComment(commentToPost);
		// Call api to update comment count for cases
		return new ResponseEntity<>(new CommentView(savedComment, Author.of(token)), HttpStatus.CREATED);
=======
		return null;
>>>>>>> 84729ea... Implement vote endpoint for api
	}

	@Override
	public ResponseEntity<CommentPage> getCommentsOnSourceItem(
			Pageable pageable,
			SourceType sourceType,
<<<<<<< HEAD
			Integer sourceItemId,
			String authorId) {
		Page<Comment> commentPage = commentService.getComments(pageable, sourceType, sourceItemId, authorId, null);
		return new ResponseEntity<>(new CommentPage(commentService.toCommentViewPage(commentPage)), HttpStatus.OK);
=======
			Integer sourceItemId) {
		return null;
>>>>>>> 84729ea... Implement vote endpoint for api
	}

	@Override
	public ResponseEntity<CommentView> updateCommentOnSourceItem(
			Principal principal,
			SourceType sourceType,
			Integer sourceItemId,
			Integer commentId,
			@Valid CommentBody commentBody) {
<<<<<<< HEAD
		Iterator<Comment> iter = commentService.getComments(sourceType, sourceItemId, null, commentId).iterator();
		if (!iter.hasNext())
			throw new ElementNotFoundException(
					Comment.class, "source type", sourceType.toString(), "source item id", sourceItemId.toString(),
					"id", String.valueOf(commentId));
		// Only the author of the comment should be allowed to update it
		AccessToken token = Utils.getAuthToken(principal);
		Comment commentToUpdate = iter.next();
		if (!Utils.isAuthor(token, commentToUpdate.getAuthorId()))
			throw new AccessDeniedException("Access Denied");

		return new ResponseEntity<>(
				new CommentView(
						commentService.updateCommentBody(commentToUpdate, commentBody.getContent()), Author.of(token)),
				HttpStatus.OK);
=======
		return null;
>>>>>>> 84729ea... Implement vote endpoint for api
	}

	@Override
	public void deleteCommentOnSourceItem(
			Principal principal,
			SourceType sourceType,
			Integer sourceItemId,
			Integer commentId) {
<<<<<<< HEAD
		Iterator<Comment> iter = commentService.getComments(sourceType, sourceItemId, null, commentId).iterator();
		if (!iter.hasNext())
			throw new ElementNotFoundException(
					Comment.class, "source type", sourceType, "source item id", sourceItemId, "id", commentId);
		// Need to check that the user deleting is the author or admin
		Comment commentToDelete = iter.next();
		if (!Utils.isAuthorOrAdmin(Utils.getAuthToken(principal), commentToDelete.getAuthorId()))
			throw new AccessDeniedException("Access Denied");

		commentService.deleteComment(commentToDelete);
	}
}
=======

	}
}
>>>>>>> 84729ea... Implement vote endpoint for api
