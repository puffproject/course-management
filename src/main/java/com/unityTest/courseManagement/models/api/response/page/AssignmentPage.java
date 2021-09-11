package com.unityTest.courseManagement.models.api.response.page;

import com.unityTest.courseManagement.entity.Assignment;
import io.swagger.annotations.ApiModel;
import org.springframework.data.domain.Page;

@ApiModel(value = "AssignmentPage", description = "Page request for assignments")
public class AssignmentPage extends BasePage<Assignment> {
	public AssignmentPage(Page<Assignment> page) {
		super(page);
	}
}
