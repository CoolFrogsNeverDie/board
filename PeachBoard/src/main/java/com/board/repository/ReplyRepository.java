package com.board.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.board.entity.PostEntity;
import com.board.entity.ReplyEntity;

/**
 * ReplyRepository
 * 
 * 댓글 레파지토리
 */
@Repository
public interface ReplyRepository extends JpaRepository<ReplyEntity, Long>{

	// 부모 댓글 없는 댓글 객체 조회
	@Query("SELECT r FROM ReplyEntity r WHERE r.postEntity = :postEntity AND r.parent IS NULL ORDER BY r.createDate ASC")
	List<ReplyEntity> findByPostEntityOrderByCreateDateDesc(@Param("postEntity") PostEntity postEntity);

	// 특정 부모 댓글을 기준으로 자식 댓글을 등록일 역순으로 조회하는 메서드
	List<ReplyEntity> findByParentOrderByCreateDateAsc(ReplyEntity parent);

	// 특정 ID로 댓글을 조회하는 메서드
	Optional<ReplyEntity> findById(Integer id);
}
