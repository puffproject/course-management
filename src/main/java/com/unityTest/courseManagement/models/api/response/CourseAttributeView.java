package com.unityTest.courseManagement.models.api.response;

import com.unityTest.courseManagement.entity.CourseAttribute;
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
@ApiModel(value = "CourseAttributeView", description = "Collection of course attributes")
@Data
@NoArgsConstructor
public class CourseAttributeView {

    @ApiModelProperty(value = "Course title", example = "Introduction to functional programming")
    private String title;

    @ApiModelProperty(value = "Course description", example = "Basic computability models; The Church-Turing thesis, complexity classes; P versus NP;")
    private String description;

    @ApiModelProperty(value = "Course professor", example = "Dr. William Farmer")
    private String professorName;

    @ApiModelProperty(value = "Course sections", example = "C01, C02")
    private String sections;

    @ApiModelProperty(value = "Course weight", example = "3 units")
    private String weight;

    @ApiModelProperty(value = "Course prerequisites", example = "COMPSCI 2FA3, COMPSCI 2CO3")
    private String prerequisites;

    // Course attribute view constructor
    // Build a CourseAttributeView from a list of CourseAttributes
    public CourseAttributeView(List<CourseAttribute> attributes) {
        for (CourseAttribute attribute : attributes) {
            try {
                Field field = CourseAttributeView.class.getDeclaredField(attribute.getName().toString());
                field.set(this, attribute.getValue());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                log.error(String.format("Failed to find field %s with error %s.", attribute.getName().toString(), e.getLocalizedMessage()));
            }
        }
    }
}

