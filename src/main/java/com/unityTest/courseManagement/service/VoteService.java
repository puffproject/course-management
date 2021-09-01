package com.unityTest.courseManagement.service;

import com.unityTest.courseManagement.entity.SourceType;
import com.unityTest.courseManagement.entity.Vote;
import com.unityTest.courseManagement.entity.VoteAction;
import com.unityTest.courseManagement.entity.Vote_;
import com.unityTest.courseManagement.repository.VoteRepository;
import com.unityTest.courseManagement.utils.specification.AndSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VoteService {

	@Autowired
	private VoteRepository voteRepository;

	@Autowired
	private CommentService commentService;

	/**
	 * Save a new Vote to the repository, or update an existing Vote to respect the unique constraints
	 * of the table.
	 * 
	 * @param voteToSaveOrUpdate Vote to create or update
	 * @return Created or updated vote
	 */
	public Vote saveOrUpdateVote(Vote voteToSaveOrUpdate) {
		Optional<Vote> existingVote = findExistingVote(
			voteToSaveOrUpdate.getSourceType(), voteToSaveOrUpdate.getSourceItemId(), voteToSaveOrUpdate.getAuthorId());
		voteToSaveOrUpdate.setId(existingVote.map(Vote::getId).orElse(0));
		return voteRepository.save(voteToSaveOrUpdate);
	}

	/**
	 * Check if a user has made a vote on a source item
	 * 
	 * @param sourceType SourceType of source item to check
	 * @param sourceItemId Id of source item to check
	 * @param authorId Id of author who might have voted
	 * @return Optional vote, containing the vote the author may have made
	 */
	public Optional<Vote> findExistingVote(SourceType sourceType, Integer sourceItemId, String authorId) {
		// Need to check if vote matching unique constraints already exists
		Specification<Vote> spec = new AndSpecification<Vote>()
			.equal(sourceType, Vote_.SOURCE_TYPE)
			.equal(sourceItemId, Vote_.SOURCE_ITEM_ID)
			.equal(authorId, Vote_.AUTHOR_ID)
			.getSpec();
		return voteRepository.findOne(spec);
	}

	/**
	 * Update the target source item with an updated vote count. Need to keep tables updated with counts
	 * so queries can sort by upvotes.
	 * 
	 * @param sourceType SourceType of target source item
	 * @param sourceItemId Source item id
	 */
	public void updateVoteCountForSourceItem(SourceType sourceType, Integer sourceItemId) {
		int upvotes =
			voteRepository.countBySourceTypeAndSourceItemIdAndAction(sourceType, sourceItemId, VoteAction.UPVOTE);
		int downvotes =
			voteRepository.countBySourceTypeAndSourceItemIdAndAction(sourceType, sourceItemId, VoteAction.DOWNVOTE);
		int upvoteCount = upvotes - downvotes;

		switch (sourceType) {
			case COMMENT:
				commentService.updateCommentWithVoteCount(sourceItemId, upvoteCount);
				return;
			case CASE:
				// Call api
				return;
			case SUITE:
				// Call api
				return;
		}
	}
}