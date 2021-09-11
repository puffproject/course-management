package com.unityTest.courseManagement.models.api.response.page;

import com.unityTest.courseManagement.entity.Comment;
import com.unityTest.courseManagement.models.api.response.CommentView;
import io.swagger.annotations.ApiModel;
import org.springframework.data.domain.Page;

@ApiModel(value = "CommentPage", description = "Page request for comments")
public class CommentPage extends BasePage<CommentView> {
	public CommentPage(Page<CommentView> page) {
		super(page);
	}
}
