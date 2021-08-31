package com.unityTest.courseManagement.restImpl;

import com.unityTest.courseManagement.entity.SourceType;
import com.unityTest.courseManagement.entity.Vote;
import com.unityTest.courseManagement.entity.VoteAction;
import com.unityTest.courseManagement.exception.UnsupportedVoteActionException;
import com.unityTest.courseManagement.models.api.request.CommentBody;
import com.unityTest.courseManagement.models.api.request.VoteActionOptions;
import com.unityTest.courseManagement.models.api.response.CommentView;
import com.unityTest.courseManagement.models.api.response.page.CommentPage;
import com.unityTest.courseManagement.restApi.EngagementApi;
import com.unityTest.courseManagement.service.VoteService;
import com.unityTest.courseManagement.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import java.security.Principal;

/**
 * Rest controller for the /engagement endpoints
 */
@RestController
public class EngagementController implements EngagementApi {

	@Autowired
	private VoteService voteService;

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
	}

	@Override
	public ResponseEntity<CommentView> commentOnSourceItem(
			Principal principal,
			SourceType sourceType,
			Integer sourceItemId,
			@Valid CommentBody commentBody) {
		return null;
	}

	@Override
	public ResponseEntity<CommentPage> getCommentsOnSourceItem(
			Pageable pageable,
			SourceType sourceType,
			Integer sourceItemId) {
		return null;
	}

	@Override
	public ResponseEntity<CommentView> updateCommentOnSourceItem(
			Principal principal,
			SourceType sourceType,
			Integer sourceItemId,
			Integer commentId,
			@Valid CommentBody commentBody) {
		return null;
	}

	@Override
	public void deleteCommentOnSourceItem(
			Principal principal,
			SourceType sourceType,
			Integer sourceItemId,
			Integer commentId) {

	}
}
