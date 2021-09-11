package com.unityTest.courseManagement.apiClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
	name = "test-runner",
	url = "${api.client.test-runner.url}",
	configuration = KeycloakClientServiceConfiguration.class)
public interface TestRunnerClient {
	@PutMapping("/case/{caseId}/vote")
	void updateVoteScoreOnTestCase(@PathVariable("caseId") Integer caseId, @RequestParam("count") Integer count);

	@PutMapping("/suite/{suiteId}/vote")
	void updateVoteScoreOnTestSuite(@PathVariable("suiteId") Integer suiteId, @RequestParam("count") Integer count);

	@PostMapping("/case/{caseId}/addComment")
	void incrementCommentCountOnTestCase(@PathVariable("caseId") Integer caseId);
}
