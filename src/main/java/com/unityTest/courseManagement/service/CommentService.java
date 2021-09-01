package com.unityTest.courseManagement.service;

import com.unityTest.courseManagement.entity.*;
import com.unityTest.courseManagement.exception.ElementNotFoundException;
import com.unityTest.courseManagement.models.api.response.Author;
import com.unityTest.courseManagement.models.api.response.CommentView;
import com.unityTest.courseManagement.repository.CommentRepository;
import com.unityTest.courseManagement.utils.specification.AndSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CommentService {

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private KeycloakService keycloakService;

	@Autowired
	private VoteService voteService;

	/**
	 * Save a comment to the repository and update any comment counts across the platform.
	 * 
	 * @param commentToSave Comment to save
	 * @return Saved comment
	 */
	public Comment saveComment(Comment commentToSave) {
		return commentRepository.save(commentToSave);
	}

	/**
	 * Get a list of courses that match the criteria
	 * 
	 * @param sourceType SourceType of item that comment is under
	 * @param sourceItemId Id of source item comment is under
	 * @param authorId Id of author of comment
	 * @param commentId Id of comment
	 * @return List of comments with fields matching the passed arguments
	 */
	public List<Comment> getComments(SourceType sourceType, Integer sourceItemId, String authorId, Integer commentId) {
		return getComments(Pageable.unpaged(), sourceType, sourceItemId, authorId, commentId).getContent();
	}

	/**
	 * Get a Page view of comments that match the criteria passed
	 * 
	 * @param pageable Pageable object specifying page size, sort and index
	 * @param sourceType SourceType of item that comment is under
	 * @param sourceItemId Id of source item comment is under
	 * @param authorId Id of author of comment
	 * @param commentId Id of comment
	 * @return Page view of comments from repository matching passed arguments and formatted using the
	 *         pageable param
	 */
	public Page<Comment> getComments(
			Pageable pageable,
			SourceType sourceType,
			Integer sourceItemId,
			String authorId,
			Integer commentId) {
		Specification<Comment> spec = new AndSpecification<Comment>()
			.equal(sourceType, Comment_.SOURCE_TYPE)
			.equal(sourceItemId, Comment_.SOURCE_ITEM_ID)
			.equal(authorId, Comment_.AUTHOR_ID)
			.equal(commentId, Comment_.ID)
			.getSpec();
		return commentRepository.findAll(spec, pageable);
	}

	/**
	 * Convert a Comment page to a CommentView page to be used by the api
	 * 
	 * @param page Comment page
	 * @return Equivalent CommentView page
	 */
	public Page<CommentView> toCommentViewPage(Page<Comment> page) {
		return page.map(comment -> {
			Author author = keycloakService.getAuthorDetails(comment.getAuthorId());
			return new CommentView(comment, author);
		});
	}

	/**
	 * Update the body of comment
	 * 
	 * @param commentToUpdate Comment to update
	 * @param newBody New body of comment as a plain string
	 * @return Updated comment with new body
	 */
	public Comment updateCommentBody(Comment commentToUpdate, String newBody) {
		commentToUpdate.setLastEdited(new Date());
		commentToUpdate.setContent(newBody);
		return commentRepository.save(commentToUpdate);
	}

	/**
	 * Delete a comment. Deleted comments have their body and author redacted.
	 * 
	 * @param commentToDelete Comment to delete
	 */
	public void deleteComment(Comment commentToDelete) {
		commentToDelete.setContent("[deleted]");
		commentToDelete.setAuthorId("");
		commentRepository.save(commentToDelete);
	}

	/**
	 * Update a comment with a new upvote count
	 * 
	 * @param commentId Id of comment to update
	 * @param upvoteCount New upvote count to assign to comment
	 */
	public void updateCommentWithVoteCount(Integer commentId, int upvoteCount) {
		Optional<Comment> existingComment = commentRepository.findById(commentId);

		if (!existingComment.isPresent())
			throw new ElementNotFoundException(Comment.class, "id", String.valueOf(commentId));
		Comment commentToUpdate = existingComment.get();
		commentToUpdate.setUpvoteCount(upvoteCount);
		saveComment(commentToUpdate);
	}
}
