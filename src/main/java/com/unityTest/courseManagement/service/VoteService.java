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
		// Need to check if vote matching unique constraints already exists
		Specification<Vote> spec = new AndSpecification<Vote>()
			.equal(voteToSaveOrUpdate.getSourceType(), Vote_.SOURCE_TYPE)
			.equal(voteToSaveOrUpdate.getSourceItemId(), Vote_.SOURCE_ITEM_ID)
			.equal(voteToSaveOrUpdate.getAuthorId(), Vote_.AUTHOR_ID)
			.getSpec();
		Optional<Vote> existingVote = voteRepository.findOne(spec);
		voteToSaveOrUpdate.setId(existingVote.map(Vote::getId).orElse(0));
		return voteRepository.save(voteToSaveOrUpdate);
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