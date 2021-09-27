package com.unityTest.courseManagement.models.api.response.page;

import com.unityTest.courseManagement.entity.AssignmentEnrollment;
import io.swagger.annotations.ApiModel;
import org.springframework.data.domain.Page;

@ApiModel(value = "AssignmentEnrollmentPage", description = "Page request for enrollment of assignments")
public class AssignmentEnrollmentPage extends BasePage<AssignmentEnrollment> {
	public AssignmentEnrollmentPage(Page<AssignmentEnrollment> page) {
		super(page);
	}
}
