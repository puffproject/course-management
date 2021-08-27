package com.unityTest.courseManagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Models a vote on a source item
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "VOTE")
public class Vote {

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "sequence-generator")
	@GenericGenerator(
		name = "sequence-generator",
		strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
		parameters = {
			@org.hibernate.annotations.Parameter(name = "sequence_name", value = "VOTE_SEQUENCE"),
			@org.hibernate.annotations.Parameter(name = "initial_value", value = "10000"),
			@org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
	private int id;

	// Type of source being voted on
	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(name = "SOURCE_TYPE")
	private SourceType sourceType;

	// Id of source item being voted on
	@NotNull
	@Column(name = "ITEM_ID")
	private int itemId;

	// Id of author who voted
	@NotNull
	@Column(name = "AUTHOR_ID")
	private String authorId;

	// Vote action
	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(name = "ACTION")
	private VoteAction action;
}
