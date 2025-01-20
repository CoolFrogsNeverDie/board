package com.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.board.entity.PostEntity;

/**
 * PostRepository
 * 
 * 게시글 레파지토리
 */
@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long>{

	/**
	 * pageable 객체로 게시글 리스트 찾기
	 * @param pageable
	 * @return Page<PostEntity>
	 */
	Page<PostEntity> findAllByOrderByCreateDateDesc(Pageable pageable);
}
