package com.unityTest.courseManagement.models.api.response;

import com.unityTest.courseManagement.entity.Comment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@ApiModel(value = "Comment", description = "Models a user comment under a source item")
@Data
public class CommentView {
	@ApiModelProperty(value = "Id", required = true)
	private int id;

	@ApiModelProperty(value = "Creation datetime", required = true)
	private Date created;

	@ApiModelProperty(value = "Last edited datetime")
	private Date lastEdited;

	@ApiModelProperty(value = "Author information")
	private Author author;

	@ApiModelProperty(value = "Comment body", required = true, example = "This is a sample comment")
	private String content;

	@ApiModelProperty(value = "Upvote score", required = true, example = "10")
	private int upvoteCount;

	public CommentView(Comment comment, Author author) {
		this(comment.getId(), comment.getCreated(), comment.getLastEdited(), author, comment.getContent(),
				comment.getUpvoteCount());
	}
}