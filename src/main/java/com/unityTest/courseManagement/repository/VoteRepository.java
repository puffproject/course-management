package com.unityTest.courseManagement.repository;

import com.unityTest.courseManagement.entity.Vote;
import com.unityTest.courseManagement.entity.VoteAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Integer>, JpaSpecificationExecutor<Vote> {
	int countBySourceTypeAndSourceItemIdAndAction(SourceType sourceType, Integer sourceItemId, VoteAction action);
}
