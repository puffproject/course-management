package com.unityTest.courseManagement.repository;

import com.unityTest.courseManagement.entity.SourceType;
import com.unityTest.courseManagement.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Integer>, JpaSpecificationExecutor<Vote> {

	@Transactional
	void deleteBySourceTypeAndSourceItemIdAndAuthorId(SourceType sourceType, Integer sourceItemId, String authorId);
}
