package com.unityTest.courseManagement.models.api.response;

import com.unityTest.courseManagement.entity.AssignmentAttribute;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Collection of values returned as the attributes of a course
 */

@Slf4j
@ApiModel(value = "AssignmentAttributeView", description = "Collection of assignment attributes")
@Data
@NoArgsConstructor
public class AssignmentAttributeView {
	@ApiModelProperty(value = "Assignment description", example = "Assignment 1 about intro to Java basics.")
	private String description;

	@ApiModelProperty(value = "Assignment weight", example = "5%")
	private String weight;

	@ApiModelProperty(
		value = "Assignment URL link",
		example = "http://www.cas.mcmaster.ca/~cs2c03/2020/as1-2020full.pdf")
	private String url;

	@ApiModelProperty(value = "Assignment due date", example = "2021-09-01 11:59 PM")
	private String dueDate;


	// Assignment attribute view constructor
	// Build an AssignmentAttributeView from a list of AssignmentAttributes
	public AssignmentAttributeView(List<AssignmentAttribute> attributes) {
		for (AssignmentAttribute attribute : attributes) {
			try {
				Field field = AssignmentAttributeView.class.getDeclaredField(attribute.getName().toString());
				field.set(this, attribute.getValue());
			} catch (NoSuchFieldException | IllegalAccessException e) {
				String errMsg = "Failed to find field %s with error %s.";
				log.error(String.format(errMsg, attribute.getName().toString(), e.getLocalizedMessage()));

			}
		}
	}
}
