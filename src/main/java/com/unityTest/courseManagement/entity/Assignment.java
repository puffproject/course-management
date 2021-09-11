package com.unityTest.courseManagement.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * Models a course assignment for a given semester
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Assignment", description = "Models a course assignment")
@Entity
@Table(name = "ASSIGNMENT")
public class Assignment {

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "sequence-generator")
	@GenericGenerator(
		name = "sequence-generator",
		strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
		parameters = {
			@org.hibernate.annotations.Parameter(name = "sequence_name", value = "ASSIGNMENT_SEQUENCE"),
			@org.hibernate.annotations.Parameter(name = "initial_value", value = "1000"),
			@org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
	private int id;

	// Course belonging to assignment
	@ApiModelProperty(value = "Assignment course", required = true)
	@NotBlank
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSE_ID", referencedColumnName = "ID", nullable = false)
	private Course course;

	// Course code
	@ApiModelProperty(value = "Assignment name", required = true, example = "Assignment 1")
	@NotBlank
	@Column(name = "NAME")
	private String name;
}

