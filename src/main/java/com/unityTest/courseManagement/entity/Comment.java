package com.unityTest.courseManagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Models a comment on a source item
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "COMMENT")
public class Comment {

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "sequence-generator")
	@GenericGenerator(
		name = "sequence-generator",
		strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
		parameters = {
			@org.hibernate.annotations.Parameter(name = "sequence_name", value = "COMMENT_SEQUENCE"),
			@org.hibernate.annotations.Parameter(name = "initial_value", value = "1000"),
			@org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
	private int id;

	// Type of source being commented on
	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(name = "SOURCE_TYPE")
	private SourceType sourceType;

	// Id of source item being commented on
	@NotNull
	@Column(name = "SOURCE_ITEM_ID")
	private int sourceItemId;

	// Id of author who commented
	@NotNull
	@Column(name = "AUTHOR_ID")
	private String authorId;

	// Content of comment, in plain string
	@Column(name = "CONTENT")
	private String content;

	@CreationTimestamp
	@Column(name = "CREATED")
	private Date created;

	// True if the comment has been edited
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_EDITED")
	private Date lastEdited;

	// Upvote/downvote count on comment
	@Column(name = "UPVOTE_COUNT")
	private int upvoteCount = 0;
}
