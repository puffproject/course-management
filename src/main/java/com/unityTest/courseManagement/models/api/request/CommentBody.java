package com.unityTest.courseManagement.models.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Request body to post a comment
 */
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "CommentBody")
@Data
public class CommentBody {
	@ApiModelProperty(value = "Comment contents", required = true, example = "This is a sample comment")
	@NotBlank
	@Size(max = 8000)
	public String content;
}